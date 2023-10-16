package fileHandler;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonHandler  {

	private Object obj;
	private String resourceLocation;

	public JsonHandler(String JsonLocation) throws IOException {
		JSONParser parser = new JSONParser();
		FileReader file = new FileReader(JsonLocation);
		try {
			obj = parser.parse(file);
		} catch (IOException | ParseException e) {
			System.out.println(e.toString());
		}
		resourceLocation = (JsonLocation.split("resources"))[1].replace("\\", "/");
		file.close();
	}

	public synchronized <T> T getPojo(Class<T> clazz) {
		Gson gson = new GsonBuilder().setPrettyPrinting().create();
		String fileData = null;
		try {

			fileData = IOUtils.toString(JsonHandler.class.getResourceAsStream(resourceLocation),
					StandardCharsets.UTF_8);
		} catch (Exception e) {
			System.out.println(e.getStackTrace().toString());
		}
		if (fileData == null) {
			throw new NullPointerException("data not available. check file: " + resourceLocation
					+ " in resources for availability and valid data");
		}

		return gson.fromJson(fileData, clazz);
	}

	public JSONObject getObject() {
		return (JSONObject) obj;
	}

	public String getValue(String JsonKey) {
		return getObject().get(JsonKey).toString();
	}

	public Object[] getKeys() {
		return getObject().keySet().toArray();

	}

	public Map<String, String> getAllValues() {
		Map<String, String> map=new TreeMap<String, String>();
		Object[] keys = getKeys();
		for(Object obj :keys ) {
			map.put(obj.toString(), getValue(obj.toString()));
		}
		return map;
	}

	public JSONArray getArray(String ArrayKey) {
		return (JSONArray) getObject().get(ArrayKey);
	}

	public JSONObject getObjFromArray(JSONArray jsonArray, int index) {
		return (JSONObject) jsonArray.get(index);
	}

	public String getValue(String testCaseName, String key) {
		for (Object object : getKeys()) {
			if(object.toString().equalsIgnoreCase(testCaseName)) {
				JSONObject arrayObj = getObjFromArray(getArray(object.toString()), 1);
				for (Object obj : arrayObj.keySet()) {
					if(obj.toString().equalsIgnoreCase(key)) {
						return arrayObj.get(obj.toString()).toString();
					}
				}
			}
		}
		return null;
	}

	public Object[][] getData(String testCaseName) {
		Object[][] objData = null;
		for (Object object : getKeys()) {
			if(object.toString().equalsIgnoreCase(testCaseName)) {
				JSONArray array = getArray(object.toString());
				objData = new Object[array.size()][1];
				HashMap<String,String> table=null;
				for (int i = 0; i < array.size(); i++) {
					table = new HashMap<String,String>();
					JSONObject arrayObj = getObjFromArray(array, i);
					for (Object obj : arrayObj.keySet()) {
						table.put(obj.toString(), arrayObj.get(obj.toString()).toString());
					}
					objData[i][0] = table;
				}

			}
		}
		return objData;	
	}
}