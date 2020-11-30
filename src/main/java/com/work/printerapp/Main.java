
package com.work.printerapp;

import com.beust.jcommander.JCommander;
import com.work.printerapp.input.Args;
import com.work.printerapp.input.Config;
import com.work.printerapp.input.FileConfiguration;

import java.io.IOException;
import java.util.logging.Level;

public class Main {

    public static void main(String[] args) {

        java.util.logging.Logger.getLogger("org.apache.pdfbox")
                .setLevel(Level.SEVERE);
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
            System.err.println("Bad Argument");
        }
    }
}
