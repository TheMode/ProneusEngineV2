package fr.proneus.engine.data;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONObject;

public class DataFile {

	private String name;

	private String json;

	private File file;

	protected DataFile(String name) {
		this.name = name;
		this.file = new File(name + ".data");
		try {
			this.file.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		refreshJson();
		System.out.println("PATH: " + file.getAbsolutePath());

	}

	public void setData(String key, Object value, boolean crypted) {
		JSONObject object = json.length() > 0 ? new JSONObject(json) : new JSONObject();
		object.put(key.toLowerCase(), value);
		String jsonString = object.toString();
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(jsonString);
			writer.close();
			refreshJson();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}

	public void setData(String key, Object value) {
		setData(key, value, false);
	}

	public <T> Data<T> getData(String key, boolean crypted) {
		JSONObject object = new JSONObject(json);
		Object result = object.get(key.toLowerCase());
		return new Data<>(result);
	}

	public <T> Data<T> getData(String key) {
		return getData(key, false);
	}

	public String getName() {
		return name;
	}

	private void refreshJson() {
		String result = "";
		String line = null;
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while ((line = reader.readLine()) != null) {
				result += line;
			}
			reader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.json = result;
	}

}
