package tup.tika.demo;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class ParserExtraction {
	public static void main(String[] args) throws IOException, SAXException, TikaException {
		// 新增存放各種檔案的files資料夾
		File fileDir = new File("files");
		// 若果資料夾路徑錯誤，離開程式
		if (!fileDir.exists()) {
			System.out.println("資料夾不存在, 請檢查!");
			System.exit(0);
		}
		// 取得資料夾下的所有檔案，存放在File陣列中
		File[] fileArr = fileDir.listFiles();
		// 建立內容處理器物件
		BodyContentHandler handler = new BodyContentHandler();
		// 建立元資料物件
		Metadata metadata = new Metadata();
		FileInputStream inputStream = null;
		Parser parser = new AutoDetectParser();
		// 自動檢驗分析器
		ParseContext context = new ParseContext();
		for (File f : fileArr) {
				inputStream = new FileInputStream(f);
				parser.parse(inputStream, handler, metadata, context);
				System.out.println(f.getName() + ":\n" + handler.toString());
		}
	}
}
