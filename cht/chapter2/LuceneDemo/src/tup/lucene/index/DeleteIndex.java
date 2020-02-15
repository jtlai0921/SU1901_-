package tup.lucene.index;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import tup.lucene.ik.IKAnalyzer6x;

public class DeleteIndex {
	public static void main(String[] args) {
		// 移除title中含有“美國”的文件
		deleteDoc("title", "美國");
	}
	public static void deleteDoc(String field, String key) {
		Analyzer analyzer = new IKAnalyzer6x();
		IndexWriterConfig icw = new IndexWriterConfig(analyzer);
		Path indexPath = Paths.get("indexdir");
		Directory directory;
		try {
			directory = FSDirectory.open(indexPath);
			IndexWriter indexWriter = new IndexWriter(directory, icw);
			indexWriter.deleteDocuments(new Term(field, key));
			indexWriter.commit();
			indexWriter.close();
			System.out.println("移除完成!");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
