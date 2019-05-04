package dfs;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class FileMapObject {
	private ArrayList<Page> pages;
	private String fileName;
	
	public FileMapObject(String fileName) {
		this.fileName = fileName;
		pages = new ArrayList<Page>();
	}
	
	public void appendEmptyPage(String pageId, String lowerBoundInterval) {
		Page page = new Page(pageId, lowerBoundInterval);
		pages.add(page);
	}
	
	public void emit(String key, JsonObject value, String pageId) {
		for(Page page : pages) {
			if(page.getId().equals(pageId)) {
				page.addKeyValue(key, value);
			}
		}
	}
	
	public ArrayList<Page> getPages(){
		return pages;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public class Page{
		private TreeMap<String, List<JsonObject>> data;
		private String lowerBoundInterval;
		private String pageId;
		
		public Page(String pageId, String lowerBoundInterval) {
			this.lowerBoundInterval = lowerBoundInterval;
			this.pageId = pageId;
			data = new TreeMap<String, List<JsonObject>>();
		}
		
		public void addKeyValue(String key, JsonObject value) {
			if(!data.containsKey(key)) {
				List<JsonObject> tempList = new ArrayList<JsonObject>();
				tempList.add(value);
				data.put(key,  tempList);
			}
			else{
				List<JsonObject> tempList = data.get(key);
				tempList.add(value);
				data.put(key, tempList);
			}
		}
		
		public String getId() {
			return pageId;
		}
	}
}
