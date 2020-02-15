package tup.tika.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

public class TikaParsePdf {
	public static void main(String[] args) throws IOException, SAXException, TikaException {
		// 檔案路徑
		String filepath = "files/中國人工智慧大會CCAI 2016圓滿落幕.pdf";
		// 新增File物件
		File pdfFile = new File(filepath);
		// 建立內容處理器物件
		BodyContentHandler handler = new BodyContentHandler();
		// 建立元資料物件
		Metadata metadata = new Metadata();

		// 讀入檔案
		FileInputStream inputStream = new FileInputStream(pdfFile);
		// InputStream inputStream=TikaInputStream.get(pdfFile);
		// 建立內容解析器物件
		ParseContext parseContext = new ParseContext();
		// 案例化PDFParser物件
		PDFParser parser = new PDFParser();
		// OOXMLParser parser = new OOXMLParser ();
		// TXTParser parser = new TXTParser();
		// HtmlParser parser = new HtmlParser();
		// 呼叫parse()方法解析檔案
		parser.parse(inputStream, handler, metadata, parseContext);
		// 檢查元資料內容
		System.out.println("檔案屬性訊息:");
		for (String name : metadata.names()) {
			System.out.println(name + ":" + metadata.get(name));
		}
		// 列印pdf檔案中的內容
		System.out.println("pdf檔案中的內容:");
		System.out.println(handler.toString());

	}
}
