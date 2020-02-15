package tup.lucene.analyzer;

import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cjk.CJKAnalyzer;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.core.KeywordAnalyzer;
import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.core.StopAnalyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

public class VariousAnalyzers {
	private static String str ="中華人民共和國簡稱中國,  是一個有13億人口的國家"; 
	public static void main(String[] args) throws IOException {
		Analyzer analyzer = null;

		analyzer = new StandardAnalyzer();// 標准分詞
		System.out.println("標准分詞:" + analyzer.getClass());
		printAnalyzer(analyzer);

		analyzer = new WhitespaceAnalyzer(); // 空格分詞
		System.out.println("空格分詞:" + analyzer.getClass());
		printAnalyzer(analyzer);

		analyzer = new SimpleAnalyzer(); // 簡單分詞
		System.out.println("簡單分詞:" + analyzer.getClass());
		printAnalyzer(analyzer);

		analyzer = new CJKAnalyzer(); // 二分法分詞
		System.out.println("二分法分詞:" + analyzer.getClass());
		printAnalyzer(analyzer);

		analyzer = new KeywordAnalyzer(); // 關鍵字分詞
		System.out.println("關鍵字分詞:" + analyzer.getClass());
		printAnalyzer(analyzer);

		analyzer = new StopAnalyzer(Paths.get("src/stopword.dic"));
		
		// 停用詞分詞
		System.out.println("停用詞分詞:" + analyzer.getClass());
		printAnalyzer(analyzer);

		analyzer = new SmartChineseAnalyzer(); // 中文智慧分詞
		System.out.println("中文智慧分詞:" + analyzer.getClass());
		printAnalyzer(analyzer);
	}

	public static void printAnalyzer(Analyzer analyzer) throws IOException {
		StringReader reader = new StringReader(str);
		TokenStream toStream = analyzer.tokenStream(str, reader);
		toStream.reset();// 清理流
		CharTermAttribute teAttribute = toStream.getAttribute(CharTermAttribute.class);
		while (toStream.incrementToken()) {
			System.out.print(teAttribute.toString() + "|");
		}
		System.out.println("\n");
		analyzer.close();
	}
}
