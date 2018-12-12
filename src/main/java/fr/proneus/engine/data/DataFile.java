package fr.proneus.engine.data;

import com.google.gson.*;

import java.io.*;

public class DataFile {

    private String name;

    private String fileJson;

    private Gson gson;

    private File file;

    protected DataFile(String name) {
        this.name = name;
        this.file = new File(name + ".data");
        try {
            this.file.createNewFile();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        this.gson = new Gson();
        refreshJson();
        // System.out.println("PATH: " + file.getAbsolutePath());

    }

    public void setData(String key, Object value) {
        key = key.toLowerCase();
        JsonElement element = new JsonParser().parse(gson.toJson(value));
        JsonObject object = fileJson.length() > 0 ? gson.fromJson(fileJson, JsonObject.class) : new JsonObject();
        if (object.has(key))
            object.remove(key);
        object.add(key.toLowerCase(), element);
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

    public <T> T getObject(String key, Class<T> clazz) {
        JsonElement element = new JsonParser().parse(fileJson);
        try {
            JsonObject object = element.getAsJsonObject();
            object = object.getAsJsonObject(key.toLowerCase());
            return gson.fromJson(object, clazz);
        } catch (IllegalStateException e) {
            return null;
        }
    }

    public <T> T getPrimitive(String key, Class<T> clazz) {
        JsonElement element = new JsonParser().parse(fileJson);
        JsonObject object = element.getAsJsonObject();
        JsonPrimitive primitive = object.getAsJsonPrimitive(key.toLowerCase());
        return gson.fromJson(primitive, clazz);
    }

    public String getString(String key) {
        return getPrimitive(key, String.class);
    }

    public Integer getInteger(String key) {
        return getPrimitive(key, Integer.class);
    }

    public Long getLong(String key) {
        return getPrimitive(key, Long.class);
    }

    public Float getFloat(String key) {
        return getPrimitive(key, Float.class);
    }

    public Double getDouble(String key) {
        return getPrimitive(key, Double.class);
    }

    public String getFileName() {
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

        this.fileJson = result;
    }

}
