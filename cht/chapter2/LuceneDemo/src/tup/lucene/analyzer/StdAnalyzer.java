package tup.lucene.analyzer;

import java.io.IOException;
import java.io.StringReader;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.tokenattributes.CharTermAttribute;

import tup.lucene.ik.IKAnalyzer6x;

public class StdAnalyzer {
	private static String strCh = "中華人民共和國簡稱中國, 是一個有13億人口的國家";
	private static String strEn = "Dogs can not achieve a place,eyes can reach; ";
	public static void main(String[] args) throws IOException {
		System.out.println("StandardAnalyzer對中文分詞:");
		stdAnalyzer(strCh);
		System.out.println("StandardAnalyzer對英文分詞:");
		stdAnalyzer(strEn);
		
	}
	public static void stdAnalyzer(String str) throws IOException{
		Analyzer analyzer = null;
		analyzer = new StandardAnalyzer();

		StringReader reader = new StringReader(str);
		TokenStream toStream = analyzer.tokenStream(str, reader);
		toStream.reset();
		CharTermAttribute teAttribute = toStream.getAttribute(CharTermAttribute.class);
		System.out.println("分詞結果:");
		while (toStream.incrementToken()) {
			System.out.print(teAttribute.toString() + "|");
		}
		System.out.println("\n");
		analyzer.close();
	}
}
