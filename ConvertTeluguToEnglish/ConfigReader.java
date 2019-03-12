import json.*;
import utils.*;
import java.io.File;
	
class ScreenInfo
{
	int screenW;
	int screenH;
	
	ScreenInfo(JSONObject map)
	{
		screenW = map.getInt("screenW");
		screenH = map.getInt("screenH");
	}
	
	JSONObject getJSONObject()
	{
		JSONObject map = new JSONObject();
		map.put("screenW", screenW);
		map.put("screenH", screenH);
		return map;
	}
}

class CurrentPage
{
	int pageNumber;
	int lineNumber;
	
	CurrentPage(JSONObject map)
	{
		pageNumber = map.getInt("pageNumber");
		lineNumber = map.getInt("lineNumber");
	}

	JSONObject getJSONObject()
	{
		JSONObject map = new JSONObject();
		map.put("pageNumber", pageNumber);
		map.put("lineNumber", lineNumber);
		return map;
	}
}


class ConfigReader
{
	ScreenInfo screenInfo;
	CurrentPage currentPage;
	
	ConfigReader(String filePath)
	{
		JSONObject map = new JSONObject(FileUtils.getFileDataAsStr(filePath));
		
		screenInfo = new ScreenInfo((JSONObject)map.getJSONObject("screenInfo"));
		currentPage = new CurrentPage((JSONObject)map.getJSONObject("currentPage"));
	}

	void write(String filePath) throws Exception
	{
		String jsonStr = getJSONObject().toString();
		jsonStr = JSONUtils.getFormattedJSONStr(jsonStr);
		FileUtils.write(new File(filePath), jsonStr);
	}
	
	JSONObject getJSONObject()
	{
		JSONObject map = new JSONObject();
		map.put("screenInfo", screenInfo.getJSONObject());
		map.put("currentPage", currentPage.getJSONObject());
		return map;
	}
}

/*

{
	"screenInfo":
	{
		"screenW" : 1300,
		"screenH" : 600
	},
	
	"currentPage":
	{
		"pageNumber" : 1,
		"lineNumber" : 1
	}
}

*/