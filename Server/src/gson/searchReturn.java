package gson;
import java.util.ArrayList;
import java.util.List;

public class searchReturn {
	public List<Songs> data;
	public boolean success;
	public String eventListenerName;
	
	public searchReturn(List<Songs> data, boolean success, String eventListenerName) {
		this.data = data;
		this.success = success;
		this.eventListenerName = eventListenerName;
	}
}
