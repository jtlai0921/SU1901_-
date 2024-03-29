﻿package lucene.file.search.controller;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import lucene.file.search.model.FileModel;
import lucene.file.search.service.IKAnalyzer6x;

public class SearchFileServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//索引路徑
		String indexpathStr = request.getServletContext().getRealPath("/indexdir");
		//接收查詢字串
		String query = request.getParameter("query");
		//解碼格式轉換
		query = new String(query.getBytes("iso8859-1"), "UTF-8");
		if (query.equals("") || query == null) {
			System.out.println("參數錯誤!");
			request.getRequestDispatcher("error.jsp").forward(request, response);
		} else {
			ArrayList<FileModel> hitsList = getTopDoc(query, indexpathStr,100);
			System.out.println("共搜到:" + hitsList.size() + "條資料!");
			request.setAttribute("hitsList", hitsList);
			request.setAttribute("queryback", query);
			request.getRequestDispatcher("result.jsp").forward(request, response);
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}
	

	public static ArrayList<FileModel> getTopDoc(String key, String indexpathStr,int N) {
		ArrayList<FileModel> hitsList = new ArrayList<FileModel>();
		//檢索域
		String[] fields = { "title", "content" };
		Path indexPath = Paths.get(indexpathStr);
		Directory dir;
		try {
			dir = FSDirectory.open(indexPath);
			IndexReader reader = DirectoryReader.open(dir);
			IndexSearcher searcher = new IndexSearcher(reader);
			Analyzer analyzer = new IKAnalyzer6x();
			// QueryParser parser = new QueryParser(field, analyzer);
			MultiFieldQueryParser parser2 = new MultiFieldQueryParser(fields, analyzer);
			// 查詢字串
			Query query = parser2.parse(key);
			TopDocs topDocs = searcher.search(query, N);
			// 定制反白標簽
			SimpleHTMLFormatter fors = new SimpleHTMLFormatter("<span style=\"color:red;font-weight:bold;\">", "</span>");
			QueryScorer scoreTitle = new QueryScorer(query, fields[0]);
			Highlighter hlqTitle = new Highlighter(fors, scoreTitle);// 反白分析器
			QueryScorer scoreContent = new QueryScorer(query, fields[0]);
			Highlighter hlqContent = new Highlighter(fors, scoreTitle);// 反白分析器
			TopDocs hits = searcher.search(query, 100);
			for (ScoreDoc sd : topDocs.scoreDocs) {
				Document doc = searcher.doc(sd.doc);
				String title = doc.get("title");
				String content = doc.get("content");
				TokenStream tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), sd.doc, fields[0],
						new IKAnalyzer6x());// 取得tokenstream
				Fragmenter fragment = new SimpleSpanFragmenter(scoreTitle);
				hlqTitle.setTextFragmenter(fragment);
				String hl_title = hlqTitle.getBestFragment(tokenStream, title);// 取得反白的部分，可以對其數量進行限制
				tokenStream = TokenSources.getAnyTokenStream(searcher.getIndexReader(), sd.doc, fields[1],
						new IKAnalyzer6x());
				fragment = new SimpleSpanFragmenter(scoreContent);
				hlqContent.setTextFragmenter(fragment);
				String hl_content = hlqTitle.getBestFragment(tokenStream, content);// 取得反白的部分，可以對其數量進行限制
				FileModel fm = new FileModel(hl_title != null ? hl_title : title,
						hl_content != null ? hl_content : content);
				hitsList.add(fm);
			}
			dir.close();
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (InvalidTokenOffsetsException e) {
			e.printStackTrace();
		}
		return hitsList;
	}

}
