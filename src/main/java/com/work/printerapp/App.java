package com.work.printerapp;



import com.work.printerapp.converters.ConverterToJPEG;
import com.work.printerapp.exceptions.EmptyArgumentException;

import javax.imageio.ImageIO;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class App {
    Printer printer = new Printer();
    App(ArrayList<String> sourcePaths, String targetPath) throws IOException {
        System.out.print("Enter your destination folder where save image \n");

        Path tempFile = Path.of(sourcePaths.get(0) + "_ARHIVE");
        if (Files.notExists(tempFile)) {
            try { Files.createDirectory(tempFile); }
            catch (Exception e ) { e.printStackTrace(); }
        }
        System.out.print("Enter your selected pdf files name with source folder \n");
        List<Path> pdfsInDir = null;
        for(var sourcePath : sourcePaths) {
            try (Stream<Path> walk = Files.list(Paths.get(sourcePath))) {
                pdfsInDir = walk.
                        map(Path::normalize).
                        filter((path) -> path.getFileName()
                                .toString()
                                .endsWith("pdf"))
                        .collect(Collectors
                                .toCollection(ArrayList::new));
                ForkJoinPool ioPool = new ForkJoinPool(64);
                List<Path> finalPdfsInDir = pdfsInDir;
                ForkJoinTask<?> tasks = ioPool.submit(
                        () ->          finalPdfsInDir.parallelStream().forEach(path -> {
                            ConverterToJPEG converter = new ConverterToJPEG(targetPath, path.toFile());
                            converter.convert();
                    try {
                        Files.move(path, tempFile.resolve(path.getFileName()), REPLACE_EXISTING);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }));
                tasks.join();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        printer.printImages(targetPath);
    }
}



