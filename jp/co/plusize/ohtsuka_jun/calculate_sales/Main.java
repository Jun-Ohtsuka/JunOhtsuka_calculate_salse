package jp.co.plusize.ohtsuka_jun.calculate_sales;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


class InvalidException extends Exception{
	InvalidException(String message){
		super(message);
	}//InvalidException
}//class


class OpenFile extends Exception {
	@SuppressWarnings("finally")
	boolean Open(String argsPass, String filePass, HashMap<String, String> nameMap, String fileName) {
		boolean a = true;
		//System.out.println(x);//デバッグ用
		try {
			//支店定義ファイルのデータを読み込む
			File file = new File (argsPass,filePass);
			FileReader fileRead = new FileReader(file);
			BufferedReader buffRead = new BufferedReader(fileRead);
			String str;
			String[] list;
			try{
				while((str = buffRead.readLine()) != null){
					//","で分割してHashMapに追加
					list = str.split(",");
					//ファイルの中身の確認
					if(list.length != 2){
						System.out.println(fileName + "定義ファイルのフォーマットが不正です");
						a = false;
						return a;
					}//if(list.length)~~
					//ファイルのコードフォーマットが適切かどうかの確認
					if(fileName == "支店"){
						String checkName =  "^\\d{3}$";
						Pattern p = Pattern.compile(checkName);
						//System.out.println(p);//デバッグ用
						//String name = list[0];//デバッグ用
						//System.out.println(list[0]);//デバッグ用
						Matcher m = p.matcher(list[0]);
						//System.out.println(m.find() == false);//デバッグ用
						if(m.find() == false){
							System.out.println(fileName + "定義ファイルのフォーマットが不正です");
							a = false;
							return a;
						}//if(errorCheck)//支店
					} else
					if(fileName == "商品"){
						String checkName =  "^[a-zA-Z]{3}\\d{5}$";
						Pattern p = Pattern.compile(checkName);
						String name = list[0];
						Matcher m = p.matcher(name);
						if(m.find() == false){
							System.out.println(fileName + "定義ファイルのフォーマットが不正です");
							a = false;
							return a;
						}
					}//if(z ==)
					nameMap.put(list[0],list[1]);
				}//while((s = br.readLine()) != null)
			}finally{
				buffRead.close();
				fileRead.close();
			}
		}catch (IOException e){
			System.out.println(fileName + "定義ファイルが存在しません");
			a = false;
		}finally{
			return a;
		}
	}//boolean Open
}//class


class JoinCode{
	HashMap<String, Long> Join(HashMap<String,String> nameMap, HashMap<String, Long> sumName, String fileName){
		for (int i = 0; i < 99999; i++){
			String key = String.valueOf(i + 1);
			if(fileName == "商品"){
				while (key.length() < 5){
					key ="0" + key;
				}//while(num.length)
				key ="SFT" + key;
			}else if(fileName == "支店"){
				while (key.length() < 3){
					key ="0" + key;
				}//while(num.length)
			}//if(z ==)
			if(nameMap.containsKey(key)){
				sumName.put(key, 0L);
			}//if(x.containsKey)~~
			if(sumName.size() == nameMap.size()){
				return sumName;
			}//if(y,size() ==)~~
		}//for(int i;~~)
		return sumName;
	}
}


class CheckCode extends Exception{
	boolean Check(HashMap<String, String> nameMap, ArrayList<String> methodListRcd, HashMap<String, Long> sumName, HashMap<String, String> methodRcdName, int loopNum, String fileName){
		int counter = 0;
		boolean a = true;
		for (int j = 0; j <= 99999; j++){
			String key = String.valueOf(j + 1);
			int code = -1;
			if(fileName == "商品"){
				while (key.length() < 5){
					key ="0" + key;
				}//while(key.len)
				key = "SFT" + key;
				code = 1;
			} else if(fileName == "支店"){
				while (key.length() < 3){
					key ="0" + key;
				}//while(key.len)
				code = 0;
			}//if(n == )~~
			if(j == 99999 && counter <= 0){
				//一つもコードが一緒じゃなかったとき
				System.out.println(methodRcdName.get(String.valueOf(loopNum + 1)) + "の" + fileName +"コードが不正です");
				a = false;
				return a;
			}else if(methodListRcd.get(code).equals(key)){
				//一致しているので合計に計算
				if(nameMap.containsKey(key)){
					Long getSum = Long.valueOf(sumName.get(key));
					Long getVal = Long.valueOf(methodListRcd.get(2));
					Long sum = getSum + getVal;
					//合計が10桁以下かどうか
					//System.out.println("sum : " + sum);//デバッグ用
					if(String.valueOf(sum).length() > 10){
						System.out.println("合計金額が10桁を超えました");
						a = false;
						return a;
					}//if(String.valueOf(sum))~~
					sumName.put(key,sum);
					counter++;
				}
				if(counter == nameMap.size()){
					return a;
				}//if(counter ==)~~
			}//if(j == 99999)else if(y.get(code).equals(key))
		}//for(int j;)~~
		return a;
	}//boolean Check
}//class


class SortMap{
	ArrayList<String> Sort(HashMap<String, Long> sumName, HashMap<String,String> nameMap){
		ArrayList<String> z = new ArrayList<String>();
		//Map.Entry のリストを作る
		List<Entry<String, Long>> entries = new ArrayList<Entry<String, Long>>(sumName.entrySet());
		//Comparator で Map.Entry の値を比較
		Collections.sort(entries, new Comparator<Entry<String, Long>>() {
			//比較関数
			@Override
			public int compare(Entry<String, Long> o1, Entry<String, Long> o2) {
				return o2.getValue().compareTo(o1.getValue());//降順
			}
		});
		//outList.add(entries.get(0));
		//並び替えたエントリーマップを1行ごとに連結してリストに格納
		for (Entry<String, Long> e : entries) {
			String outKey = e.getKey();
			String outName = nameMap.get(outKey);
			String outVal = String.valueOf(e.getValue());
			String out = outKey + "," + outName + "," + outVal;//1行に連結
			z.add(out);
		}//for
		return z;
	}
}


class OutputFile extends Exception{
	boolean OutPut(String argsPass, String filePass, ArrayList<String> outPutList){
		boolean a = true;
		try{
			File file = new File(argsPass, filePass);
			file.createNewFile();
			//System.out.println(y + "ファイルを作成しました");//デバッグ用
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			for(int f = 0; f < outPutList.size(); f++){
				bw.write(outPutList.get(f) + "\r\n");
			}//for(int f;)~~
			bw.close();
			fw.close();
			//System.out.println(y + "ファイルの書き込み完了");//デバッグ用
		}catch(IOException e){
			System.out.println(e);
			a = false;
			return a;
		}//try~~Catch
		return a;
	}//boolean OutPut
}//class


public class Main {
	public static void main(String[] args) {
		//店舗データを保存するためのHashMap
		HashMap<String, String> branch = new HashMap<>();
		//商品データを保存するためのHashMap
		HashMap<String, String> commodity = new HashMap<>();
		//売上データのファイル名を保存するためのHashMap
		HashMap<String, String> rcdName = new HashMap<>();
		//例外処理を判定する変数
		boolean exception = true;
		if(args.length != 1){
			//System.out.println("args");//デバッグ用
			System.out.println("予期せぬエラーが発生しました");
			return;
		}

		//支店定義ファイルの読み込み
		OpenFile openBran = new OpenFile();
		exception = openBran.Open(args[0] , "branch.lst", branch, "支店");
		//例外を受け取ったかどうかの判定。受け取っていたらfalseなので実行
		if(!exception){
			return;
		}
		//System.out.println(branch);//デバッグ用
		//商品定義ファイルの読み込み
		OpenFile openCom = new OpenFile();
		exception = openCom.Open(args[0] , "commodity.lst", commodity, "商品");
		//例外を受け取ったかどうかの判定。受け取っていたらfalseなので実行
		if(!exception){
			return;
		}

		//売上ファイルの名前読み込み
		String path = args[0];
		File dir = new File(path);
		File[] fileList = dir.listFiles();
		String[] files = dir.list();
		int dirCounter = 0;
		int iCounter = 0;
		ArrayList<String> rcdList = new ArrayList<>();
		//.rcdの名前がついているものだけを抜き出し
		for (int i = 0 ; i < fileList.length ; i++){
			if(files[i].contains("rcd")){
				rcdList.add(files[i]);
			}
		}//for(int i;)~~
		//ディレクトリかファイルかを判定
		for (int i = 0 ; i < rcdList.size() ; i++){
			if(files[i].contains("rcd")){
				if (!fileList[i].isFile()){
					// ディレクトリだった時の処理
					dirCounter = i;
					//System.out.println("dir:" + dirCounter);//デバッグ用
				}//if(files[i])
			}//if(fileList[i])
			iCounter = i;
		}
		//System.out.println(rcdList.get(0));//デバッグ用

		if(dirCounter > 0){//rcdの名前を持つディレクトリが存在する場合
			//rcdListの途中にディレクトリが存在するならエラー
			if(dirCounter != iCounter){
				System.out.println("売上ファイル名が連番ではありません");
				return;
			}else if(dirCounter == iCounter){
				//rcdListの最後がディレクトリなので最後を除きput
				for (int i = 0 ; i < dirCounter; i++){
					//System.out.println("デバッグ！");//デバッグ用
					//連番確認処理
					String checkName =  "^\\d{8}.rcd$";
					Pattern p = Pattern.compile(checkName);
					//System.out.println(p);//デバッグ用
					String name = rcdList.get(i);
					//System.out.println(name);//デバッグ用
					Matcher m = p.matcher(name);
					if(m.find()){
						//System.out.println("できてる！");//デバッグ用
						String key = String.valueOf(i + 1);
						rcdName.put(key,rcdList.get(i));
						//System.out.println(rcdName);//デバッグ用
					}//if(m.find())~~
				}//for(int i;)~~
			}//if(dirCounter !=)~~
		}else{//rcdの名前を持つディレクトリがない場合
			for (int i = 0 ; i < rcdList.size(); i++){
				//System.out.println("デバッグ！");//デバッグ用
				//連番確認処理
				String checkName =  "^\\d{8}.rcd$";
				Pattern p = Pattern.compile(checkName);
				//System.out.println(p);//デバッグ用
				String name = rcdList.get(i);
				//System.out.println(name);//デバッグ用
				Matcher m = p.matcher(name);
				if(m.find()){
					//System.out.println("できてる！");//デバッグ用
					String key = String.valueOf(i + 1);
					rcdName.put(key,rcdList.get(i));
					//System.out.println(rcdName);//デバッグ用
				}//if(m.find())~~
			}//for(int i;)~~
		}//if(dirCounter > 0)~~

		//rcdファイルが0だった場合のエラー処理
		if(rcdName.size() ==0){
			//System.out.print("０だよ");//デバッグ用
			System.out.println("売上ファイル名が連番ではありません");
			return;
		}//if(rudName.size == 0)~~

		//連番確認処理
		for(int i = 1; i <= rcdName.size(); i++){
			String checkName = String.valueOf(i) + ".rcd$";
			Pattern p = Pattern.compile(checkName);
			String name = rcdName.get(String.valueOf(i));
			Matcher m = p.matcher(name);
			if(m.find() == false){
				System.out.println("売上ファイル名が連番ではありません");
				return;
			}//if
		}//for(i = 1; ~~)

		//統計用データ格納作成
		//支店データ統計用HashMap
		HashMap<String, Long> sumBran = new HashMap<>();
		//商品データ統計用HashMap
		HashMap<String, Long> sumCom = new HashMap<>();

		//統計用HashMapに支店コードと合計を結びつける処理
		JoinCode mapCodeBran = new JoinCode();
		mapCodeBran.Join(branch, sumBran,"支店");
		//統計用HashMapに商品コードと合計を結びつける処理
		JoinCode mapCodeCom = new JoinCode();
		mapCodeCom.Join(commodity,sumCom,"商品");

		//売上ファイルのデータ読み込み
		try {
			for(int i = 0; i < rcdName.size(); i++){
				File rcdFile = new File (args[0] + File.separator + rcdName.get(String.valueOf(i + 1)));
				FileReader rcdFileRead = new FileReader(rcdFile);
				BufferedReader rcdBuffRead = new BufferedReader(rcdFileRead);
				String strRcd;
				ArrayList<String> listRcd = new ArrayList<String>();

				//売上ファイルの中身を呼び出し一時格納する処理
				try{
					while((strRcd = rcdBuffRead.readLine()) != null){
						listRcd.add(strRcd);
					}//while((strRcd = brRcd.readLine()) != null)
				} catch(IOException e){
					System.out.println(e);
					return;
				} finally{
					rcdBuffRead.close();
					rcdFileRead.close();
				}//try~~

				//売上ファイルのフォーマット(行数)が適正かどうかの判定
				if(listRcd.size() != 3){
					System.out.println(rcdName.get(String.valueOf(i + 1)) + "のフォーマットが不正です");
					return;
				}//if(strRcd.len)~~
				//System.out.println(listRcd);//デバッグ用

				//支店コードと一致しているかの判定
				CheckCode codeBran = new CheckCode();
				exception = codeBran.Check(branch, listRcd, sumBran, rcdName, i, "支店");
				//例外を受け取ったかどうかの判定。受け取っていたらfalseなので実行
				if(!exception){
					return;
				}
				//商品コードと一致しているかの判定
				CheckCode codeCom = new CheckCode();
				exception = codeCom.Check(commodity, listRcd, sumCom, rcdName, i, "商品");
				//例外を受け取ったかどうかの判定。受け取っていたらfalseなので実行
				if(!exception){
					return;
				}

			}//for(int i;~~)
		} catch(IOException e){
			//System.out.println("IO");//デバッグ用
			System.out.println(e);
			return;
		}//try~catch

		//売上データ出力

		//支店合計出力用にマップをソートする
		ArrayList<String> outBranList = new ArrayList<>();
		SortMap sortBran = new SortMap();
		outBranList = sortBran.Sort(sumBran, branch);
		//System.out.println(outBranList);//デバッグ用

		//商品合計出力用にマップをソートする
		ArrayList<String> outComList = new ArrayList<>();
		SortMap sortCom = new SortMap();
		outComList = sortCom.Sort(sumCom, commodity);

		//ファイル操作
		//branch.out
		OutputFile outBran = new OutputFile();
		exception = outBran.OutPut(args[0], "branch.out", outBranList);
		//例外を受け取ったかどうかの判定。受け取っていたらfalseなので実行
		if(!exception){
			return;
		}
		//commodity.out
		OutputFile outCom = new OutputFile();
		exception = outCom.OutPut(args[0], "commodity.out", outComList);
		//例外を受け取ったかどうかの判定。受け取っていたらfalseなので実行
		if(!exception){
			return;
		}

	}//void main
}//class Main