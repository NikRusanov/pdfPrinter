
package com.work.printerapp;

import com.beust.jcommander.JCommander;
import com.work.printerapp.input.Args;
import com.work.printerapp.input.Config;
import com.work.printerapp.input.FileConfiguration;

import java.awt.print.PrinterException;
import java.io.IOException;

public class Main {

    public static void main(String[] args) {


        Args arguments = new Args();
        JCommander commander = JCommander.newBuilder()
                .addObject(arguments)
                .build();
        commander.parse(args);
        String configPath = arguments.getPdfFolderPath();
        try {
            if (!configPath.isEmpty()) {
                Config conf = new FileConfiguration(arguments.getPdfFolderPath());

                conf.read();
                new App(conf.getSourcePaths(),conf.getTargetPath());
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
