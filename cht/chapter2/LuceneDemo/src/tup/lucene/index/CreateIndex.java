package tup.lucene.index;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.FieldType;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.index.IndexOptions;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import tup.lucene.ik.IKAnalyzer6x;

public class CreateIndex {

	public static void main(String[] args) {

		// 建立2個News物件
		News news1 = new News();
		news1.setId(1);
		news1.setTitle("安倍晉三本周會晤特朗普 將強調日本對美國益處");
		news1.setContent("日本首相安倍晉三計劃2月10日在華盛頓與美國總統特朗普舉行會晤時提出加大日本在美國投資的設想");
		news1.setReply(672);
		News news2 = new News();
		news2.setId(2);
		news2.setTitle("北大迎4380名新生 農村學生700多人近年最多");
		news2.setContent("昨天，北京大學迎來4380名來自全國各地及數十個國家的本科新生。其中，農村學生共700余名，為近年最多...");
		news2.setReply(995);

		News news3 = new News();
		news3.setId(3);
		news3.setTitle("特朗普宣誓(Donald Trump)就任美國第45任總統");
		news3.setContent("當地時間1月20日，唐納德·特朗普在美國國會宣誓就職，正式成為美國第45任總統。");
		news3.setReply(1872);
		// 開始時間

		Date start = new Date();
		System.out.println("**********開始建立索引**********");
		// 建立IK分詞器
		Analyzer analyzer = new IKAnalyzer6x();
		IndexWriterConfig icw = new IndexWriterConfig(analyzer);
		icw.setOpenMode(OpenMode.CREATE);
		Directory dir = null;
		IndexWriter inWriter = null;
		// 索引目錄
		Path indexPath = Paths.get("indexdir");

		try {
			if (!Files.isReadable(indexPath)) {
				System.out.println("Document directory '" + indexPath.toAbsolutePath()
						+ "' does not exist or is not readable, please check the path");
				System.exit(1);
			}
			dir = FSDirectory.open(indexPath);
			inWriter = new IndexWriter(dir, icw);

			FieldType idType = new FieldType();
			idType.setIndexOptions(IndexOptions.DOCS);
			idType.setStored(true);

			FieldType titleType = new FieldType();
			titleType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
			titleType.setStored(true);
			titleType.setTokenized(true);

			FieldType contentType = new FieldType();
			contentType.setIndexOptions(IndexOptions.DOCS_AND_FREQS_AND_POSITIONS_AND_OFFSETS);
			contentType.setStored(true);
			contentType.setTokenized(true);
			contentType.setStoreTermVectors(true);
			contentType.setStoreTermVectorPositions(true);
			contentType.setStoreTermVectorOffsets(true);

			Document doc1 = new Document();
			doc1.add(new Field("id", String.valueOf(news1.getId()), idType));
			doc1.add(new Field("title", news1.getTitle(), titleType));
			doc1.add(new Field("content", news1.getContent(), contentType));
			doc1.add(new IntPoint("reply", news1.getReply()));
			doc1.add(new StoredField("reply_display", news1.getReply()));
			
			Document doc2 = new Document();
			doc2.add(new Field("id", String.valueOf(news2.getId()), idType));
			doc2.add(new Field("title", news2.getTitle(), titleType));
			doc2.add(new Field("content", news2.getContent(), contentType));
			doc2.add(new IntPoint("reply", news2.getReply()));
			doc2.add(new StoredField("reply_display", news2.getReply()));

			Document doc3 = new Document();
			doc3.add(new Field("id", String.valueOf(news3.getId()), idType));
			doc3.add(new Field("title", news3.getTitle(), titleType));
			doc3.add(new Field("content", news3.getContent(), contentType));
			doc3.add(new IntPoint("reply", news3.getReply()));
			doc3.add(new StoredField("reply_display", news3.getReply()));

			inWriter.addDocument(doc1);
			inWriter.addDocument(doc2);
			inWriter.addDocument(doc3);

			inWriter.commit();

			inWriter.close();
			dir.close();

		} catch (IOException e) {
			e.printStackTrace();
		}
		Date end = new Date();
		System.out.println("索引文件用時:" + (end.getTime() - start.getTime()) + " milliseconds");
		System.out.println("**********索引建立完成**********");
	}
}
