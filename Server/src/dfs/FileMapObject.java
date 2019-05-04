package dfs;

import com.google.gson.JsonElement;
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
	
	public void emit(String key, JsonElement value) {
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
		private TreeMap<String, List<JsonElement>> data;
		private String lowerBoundInterval;
		private String pageId;
		
		public Page(String pageId, String lowerBoundInterval) {
			this.lowerBoundInterval = lowerBoundInterval;
			this.pageId = pageId;
			data = new TreeMap<String, List<JsonElement>>();
		}
		
		public void addKeyValue(String key, JsonElement value) {
			if(!data.containsKey(key)) {
				List<JsonElement> tempList = new ArrayList<JsonElement>();
				tempList.add(value);
				data.put(key,  tempList);
			}
			else{
				List<JsonElement> tempList = data.get(key);
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
			for (Map.Entry<String, List<JsonElement>> entry : data.entrySet()) {
			     System.out.print("Key: " + entry.getKey());
			     for(JsonElement obj : entry.getValue()) {
			    	 System.out.print(obj.toString() + " ");
			     }
			     System.out.println();
			}
		}
	}
}
