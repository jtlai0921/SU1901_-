package lucene.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;

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

import lucene.ik.IKAnalyzer6x;

public class IndexDocs {

	public static void main(String[] args) throws IOException {

		File newsfile = new File("testfile/news.txt");

		String text1 = textToString(newsfile);

		// Analyzer smcAnalyzer = new SmartChineseAnalyzer(true);
		Analyzer smcAnalyzer = new IKAnalyzer6x(true);

		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(smcAnalyzer);
		indexWriterConfig.setOpenMode(OpenMode.CREATE);
		// 索引的儲存路徑
		Directory directory = null;
		// 索引的增刪改由indexWriter建立
		IndexWriter indexWriter = null;
		directory = FSDirectory.open(Paths.get("indexdir"));
		indexWriter = new IndexWriter(directory, indexWriterConfig);

		// 新增FieldType,用於指定字段索引時的訊息
		FieldType type = new FieldType();
		// 索引時儲存文件、詞項頻率、位置訊息、偏移訊息
		type.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
		type.setStored(true);// 原始字串全部被儲存在索引中
		type.setStoreTermVectors(true);// 儲存詞項量
		type.setTokenized(true);// 詞條化
		Document doc1 = new Document();
		Field field1 = new Field("content", text1, type);
		doc1.add(field1);
		indexWriter.addDocument(doc1);
		indexWriter.close();
		directory.close();
	}

	public static String textToString(File file) {
		StringBuilder result = new StringBuilder();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));// 建構一個BufferedReader類別來讀取檔案
			String str = null;
			while ((str = br.readLine()) != null) {// 使用readLine方法，一次讀一行
				result.append(System.lineSeparator() + str);
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result.toString();
	}

}
