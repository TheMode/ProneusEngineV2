package fr.proneus.engine.utils;

import fr.proneus.engine.Game;

import java.io.*;

public class FileUtils {

    public static InputStream getInternalFile(String path) {
        return Game.class.getResourceAsStream(path);
    }

    public static File getExternalFile(String path) {
        return new File(path);
    }

    public static String getInternalFileString(String path) {
        String value = "";
        try {
            String line;
            BufferedReader reader = new BufferedReader(new InputStreamReader(FileUtils.getInternalFile(path)));
            while ((line = reader.readLine()) != null) {
                value += line + "\n";
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String getExternalFileString(String path) {

        String value = "";
        try {
            String line;
            BufferedReader reader = new BufferedReader(new FileReader(FileUtils.getExternalFile(path)));
            while ((line = reader.readLine()) != null) {
                value += line + "\n";
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return value;
    }
}
