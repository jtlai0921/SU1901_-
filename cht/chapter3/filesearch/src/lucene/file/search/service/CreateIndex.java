package lucene.file.search.service;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;

import lucene.file.search.model.FileModel;

public class CreateIndex {

	public static void main(String[] args) throws Exception {
		// IK分詞器物件
		Analyzer analyzer = new IKAnalyzer6x();
		IndexWriterConfig icw = new IndexWriterConfig(analyzer);
		icw.setOpenMode(OpenMode.CREATE);
		Directory dir = null;
		IndexWriter inWriter = null;
		Path indexPath = Paths.get("WebContent/indexdir");

		FieldType fileType = new FieldType();
		fileType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
		fileType.setStored(true);
		fileType.setTokenized(true);
		fileType.setStoreTermVectors(true);
		fileType.setStoreTermVectorPositions(true);
		fileType.setStoreTermVectorOffsets(true);
		Date start = new Date();// 開始時間
		if (!Files.isReadable(indexPath)) {
			System.out.println(indexPath.toAbsolutePath() + "不存在或是不讀取，請檢查!");
			System.exit(1);
		}
		dir = FSDirectory.open(indexPath);
		inWriter = new IndexWriter(dir, icw);
		ArrayList<FileModel> fileList = (ArrayList<FileModel>) extractFile();
		// 檢查fileList,建立索引
		for (FileModel f : fileList) {
			Document doc = new Document();
			doc.add(new Field("title", f.getTitle(), fileType));
			doc.add(new Field("content", f.getContent(), fileType));
			inWriter.addDocument(doc);
		}

		inWriter.commit();
		inWriter.close();
		dir.close();

		Date end = new Date();// 結束時間
		// 列印索引耗時
		System.out.println("索引文件完成,共耗時:" + (end.getTime() - start.getTime()) + "毫秒.");
	}

	/*
	 * 功能:列出WebContent/files目錄下的索所有檔案 參數:無 傳回值:FileModel型態的清單
	 */
	public static List<FileModel> extractFile() throws Exception {

		ArrayList<FileModel> list = new ArrayList<FileModel>();
		File fileDir = new File("WebContent/files");
		if (!fileDir.exists()) {
			System.out.println("資料夾路徑錯誤!");
		}
		File[] allFiles = fileDir.listFiles();

		for (File f : allFiles) {
			FileModel sf = new FileModel(f.getName(), parserFile(f));
			list.add(sf);
		}
		return list;
	}

	/*
	 * 功能:使用Tika分析檔案內容 參數：檔案物件 傳回值: String格式的文件內容
	 */
	public static String parserFile(File file) throws Exception {
		String fileContent = "";// 接收文件內容
		BodyContentHandler handler = new BodyContentHandler();
		Parser parser = new AutoDetectParser();// 自動解析器接口
		Metadata metadata = new Metadata();
		FileInputStream inputStream;
			inputStream = new FileInputStream(file);
			ParseContext context = new ParseContext();
			parser.parse(inputStream, handler, metadata, context);
			fileContent = handler.toString();

		return fileContent;
	}
}
