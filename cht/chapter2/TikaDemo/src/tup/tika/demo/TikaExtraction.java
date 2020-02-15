package tup.tika.demo;

import java.io.File;
import java.io.IOException;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;

public class TikaExtraction {
	public static void main(String[] args) throws IOException, TikaException {
		Tika tika = new Tika();
		// 新增存放各種檔案的files資料夾
		File fileDir = new File("files");
		// 若果資料夾路徑錯誤，離開程式
		if (!fileDir.exists()) {
			System.out.println("資料夾不存在, 請檢查!");
			System.exit(0);
		}
		// 取得資料夾下的所有檔案，存放在File陣列中
		File[] fileArr = fileDir.listFiles();
		String filecontent;
		for (File f : fileArr) {
			filecontent = tika.parseToString(f);// 自動解析
			System.out.println("Extracted Content: " + filecontent);
		}
	}
}
