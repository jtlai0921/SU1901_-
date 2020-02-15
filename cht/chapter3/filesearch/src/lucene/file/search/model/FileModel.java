package lucene.file.search.model;

public class FileModel {
	private String title;// 檔案標題
	private String content;// 檔案內容
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public FileModel() {
	}
	public FileModel(String title, String content) {
		this.title = title;
		this.content = content;
	}
}
