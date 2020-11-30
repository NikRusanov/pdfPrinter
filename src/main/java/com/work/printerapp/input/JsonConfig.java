package com.work.printerapp.input;

import java.util.ArrayList;
import java.util.List;

public class JsonConfig {
    private ArrayList<String> sourcePaths;
    String targetPath;


    public ArrayList<String> getSourcePaths() {
        return sourcePaths;
    }

    public String getTargetPath() {
        return targetPath;
    }


    @Override
    public String toString() {
        return "JsonConfig{" +
                "sourcePaths=" + sourcePaths +
                ", targetPath='" + targetPath + '\'' +
                '}';
    }
}
