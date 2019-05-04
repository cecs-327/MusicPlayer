package dfs;

import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
	
	public void emit(String key, JsonObject value) {
		for(int i = 0; i < pages.size(); i++) {
			if(i == pages.size() - 1) {
				pages.get(i).addKeyValue(key, value);
			}else if(pages.get(i).getLowerBound().compareTo(key) < 0 && pages.get(i+1).getLowerBound().compareTo(key) > 0) {
				pages.get(i).addKeyValue(key, value);
			}
		}
	}
	
	public ArrayList<Page> getPages(){
		return pages;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	public void print() {
		System.out.println("Printing .map object");
		System.out.println("FileMapObject: " + fileName);
		for(Page page: pages) {
			page.print();
		}
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
		
		public String getLowerBound() {
			return lowerBoundInterval;
		}
		
		public void print() {
			for (Map.Entry<String, List<JsonObject>> entry : data.entrySet()) {
			     System.out.print("Key: " + entry.getKey());
			     for(JsonObject obj : entry.getValue()) {
			    	 System.out.print(obj.toString() + " ");
			     }
			     System.out.println();
			}
		}
	}
}
