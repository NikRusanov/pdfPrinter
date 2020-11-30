package com.work.printerapp.input;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;

public class FileConfiguration implements Config{
    private Gson gson = new Gson();
    private FileReader fl;
    private JsonConfig configPaths;
    public FileConfiguration(String path) throws FileNotFoundException {
        fl = new FileReader(path);
    }

    @Override
    public void read() {
        try {
             configPaths = gson.fromJson(fl, JsonConfig.class);
        } catch (JsonParseException  ex) {
            System.err.println("error json structure");
        }
    }

    @Override
    public ArrayList<String> getSourcePaths() {
        return configPaths.getSourcePaths();
    }

    @Override
    public String getTargetPath() {
        return configPaths.getTargetPath();
    }
}
