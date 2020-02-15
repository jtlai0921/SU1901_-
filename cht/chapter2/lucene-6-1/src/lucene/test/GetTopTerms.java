package lucene.test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Terms;
import org.apache.lucene.index.TermsEnum;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

public class GetTopTerms {
	public static void main(String[] args) throws IOException {
		//
		Directory directory = FSDirectory.open(Paths.get("indexdir"));
		IndexReader reader = DirectoryReader.open(directory);
		// 因為只索引了一個文件，所以DocID為0，透過getTermVector取得content字段的詞項
		Terms terms = reader.getTermVector(0, "content");

		// 檢查詞項
		TermsEnum termsEnum = terms.iterator();
		BytesRef thisTerm = null;
		Map<String, Integer> map = new HashMap<String, Integer>();
		while ((thisTerm = termsEnum.next()) != null) {
			// 詞項
			String termText = thisTerm.utf8ToString();
			// 透過totalTermFreq()方法取得詞項頻率
			map.put(termText, (int) termsEnum.totalTermFreq());
		}

		// 按value排序
		List<Map.Entry<String, Integer>> sortedMap = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
		Collections.sort(sortedMap, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
				return (o2.getValue() - o1.getValue());
			}
		});

		// System.out.println(sortedMap);
		getTopN(sortedMap, 10);
	}

	// 取得top-n
	public static void getTopN(List<Entry<String, Integer>> sortedMap, int N) {
		for (int i = 0; i < N; i++) {
			System.out.println(sortedMap.get(i).getKey() + ":" + sortedMap.get(i).getValue());
		}
	}
}
