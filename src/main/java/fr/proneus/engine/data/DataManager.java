package fr.proneus.engine.data;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

	private List<DataFile> files;

	public DataManager() {
		this.files = new ArrayList<>();
	}

	public DataFile getFile(String name) {

		for (DataFile file : files) {
			if (file.getName().equalsIgnoreCase(name)) {
				return file;
			}
		}

		DataFile file = new DataFile(name.toLowerCase());
		files.add(file);

		return file;
	}

	public boolean fileExits(String name) {
		return getFile(name) != null;
	}

}
