package com.work.printerapp;


import com.work.printerapp.converters.ConverterToJPEG;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class App {
    Printer printer = new Printer();
    App(ArrayList<String> sourcePaths, String targetPath) {


        Path tempFile = Path.of(sourcePaths.get(0) + "_ARHIVE");
        if (Files.notExists(tempFile)) {
            try { Files.createDirectory(tempFile); }
            catch (Exception e ) { e.printStackTrace(); }
        }

        List<Path> pdfsInDir;
        for(var sourcePath : sourcePaths) {
            try (Stream<Path> walk = Files.list(Paths.get(sourcePath))) {
                pdfsInDir = walk.
                        map(Path::normalize).
                        filter((path) -> path.getFileName()
                                .toString()
                                .endsWith("pdf"))
                        .collect(Collectors
                                .toCollection(ArrayList::new));
               pdfsInDir.parallelStream().forEach(path -> {
                            ConverterToJPEG converter = new ConverterToJPEG(targetPath, path.toFile());
                            converter.convert();
                    try {
                        Files.move(path, tempFile.resolve(path.getFileName()), REPLACE_EXISTING);
                    } catch (IOException e) {
                        System.err.println("Error to move file. Bad path");
                    }
                });

            } catch (IOException e) {
               System.err.println("Error to parse dir. Bad path");
            }
        }
       try {
        printer.printImages(targetPath);
       } catch (IOException e) {
           System.err.println("error to find jpg for print. ");
       }
    }
}



