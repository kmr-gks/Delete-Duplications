package main;

import java.io.File;
import java.io.FileWriter;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

//日本語たと文字化する可能性がある
public class Main {
	public static void main(String[] args) {
		new DuplicationDeleter().execute();
	}
}

class DuplicationDeleter {
	//検索対象のフォルダパス
	private final String ONEDRIVE_PATH="D:\\Documents";
	//アルファベット大小を区別する。
	private final String[] pcName={"sample_pc_name"};
	//pc名を含むファイルパスのリスト
	ArrayList<String> matchListOriginal=new ArrayList<>();
	ArrayList<String> matchListAnother=new ArrayList<>();
	//内容が異なる場合、またはアクセスできない場合のファイルのリスト
	ArrayList<String> errorFileList=new ArrayList<>();

	public DuplicationDeleter() {}

	public void execute() {
		try {
			File parentFile = new File(ONEDRIVE_PATH);
			Library.puts("parentFile:" + parentFile);
			searchChildFiles(parentFile);

			FileWriter fileWriterResult=new FileWriter(new File("result.txt"),StandardCharsets.UTF_8);
			//削除用のバッチファイル shift-jis
			FileWriter fileWriterBat=new FileWriter(new File("delete.bat"),Charset.forName("SJIS"));
			//windows:crlf \r\n
			fileWriterBat.write("@echo off\r\n");

			//重複ファイルの候補を調べる
			for (int i=0;i<matchListOriginal.size();i++){
				//両方のファイルが存在する。
				Path pathO=Path.of(matchListOriginal.get(i));
				Path pathA=Path.of(matchListAnother.get(i));
				if (Files.exists(pathO)&&Files.exists(pathA)){
					var contentO=Files.readAllBytes(pathO);
					var contentA=Files.readAllBytes(pathA);
					if (Arrays.equals(contentO, contentA)){
						//同じ内容
						fileWriterBat.write("del /a \""+matchListAnother.get(i)+"\"\r\n");
					}else {
						errorFileList.add(matchListOriginal.get(i));
						fileWriterResult.write("内容 異なる:"+matchListOriginal.get(i)+"\n");
					}
				}else{
					errorFileList.add(matchListOriginal.get(i));
					fileWriterResult.write("これと重複するファイルは存在しません。:"+matchListOriginal.get(i)+"\n");
				}
			}
			fileWriterBat.write("pause\r\n");
			fileWriterResult.close();
			fileWriterBat.close();
			Library.puts("Number of files that contains pcname:"+matchListOriginal.size());
		} catch (Exception exception) {
			Library.puts(exception.getMessage());
			exception.printStackTrace();
		}
	}

	public void searchChildFiles(File parentFile){
		var list = parentFile.listFiles();
		if (list==null) return;
		for (var file : list) {
			String path=file.getAbsolutePath();
			String anotherPath=null;
			for (var name:pcName){
				if (path.contains(name)){
					int index=path.indexOf(name);
					anotherPath=path.substring(0,index-1)+path.substring(index+name.length());
					break;
				}
			}
			if (anotherPath!=null){
				//PC名を含むパスの場合
				matchListOriginal.add(anotherPath);
				matchListAnother.add(file.getAbsolutePath());
			}
			searchChildFiles(file);
		}
	}
}