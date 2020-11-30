package com.work.printerapp.input;

import java.util.ArrayList;

public interface Config {
    void read();
    ArrayList<String> getSourcePaths();
    String getTargetPath();
}
