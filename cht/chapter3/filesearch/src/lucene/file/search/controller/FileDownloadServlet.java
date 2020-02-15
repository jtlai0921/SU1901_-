package lucene.file.search.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileDownloadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final String CONTENT_TYPE = "text/html; charset=GBK";
	public FileDownloadServlet() {
		super();
	}
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		response.reset();
		response.setContentType(CONTENT_TYPE);
		String filename = new String(request.getParameter("filename").getBytes("iso-8859-1"), "UTF-8");
		System.out.println(filename);
		System.out.println("檔案路徑:" + request.getServletContext().getRealPath("/files") + "/" + filename);
		File file = new File(request.getServletContext().getRealPath("/files") + "/" + filename);
		System.out.println(file.getPath());
		// 設定response的解碼模式
		response.setContentType("application/octet-stream");
		// 寫明要下載的檔案的大小
		response.setContentLength((int) file.length());
		// 解決中文亂碼,向用戶端傳送傳回頁面的標頭資訊
		// 1.Content-disposition是MIME協定的延伸
		// 2.attachment --- 作為附屬應用程式下載
		// 3.在用戶端將會出現下載框
		// 4.這個是檔案下載的關鍵程式碼
		response.setHeader("Content-Disposition",
				"attachment;filename=" + new String(filename.getBytes("UTF-8"), "ISO8859-1"));
		// 讀出檔案到i/o流
		FileInputStream fis = new FileInputStream(file);
		BufferedInputStream buff = new BufferedInputStream(fis);
		byte[] b = new byte[1024];// 相當於我們的快取
		int k = 0;// 該值用於計算目前實際下載了多少位元組
		// 從response物件中得到輸出流,準備下載
		OutputStream myout = response.getOutputStream();
		// 開始循環下載
		while (-1 != (k = fis.read(b, 0, b.length))) {
			// 將b中的資料寫到用戶端的記憶體
			myout.write(b, 0, k);
		}
		// 將寫入到用戶端的記憶體的資料,更新到磁碟
		myout.flush();
		fis.close();
		buff.close();
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}
