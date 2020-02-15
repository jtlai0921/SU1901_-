package lucene.file.search.service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexHtml {
	public String delHtmlTag(String line) {
		String regEx_html = "<[^>]+>";
		// 建立 Pattern 物件
		Pattern r = Pattern.compile(regEx_html);
		// 建立 matcher 物件
		Matcher m = r.matcher(line);
		line = m.replaceAll("");
		return line;
	}
}
