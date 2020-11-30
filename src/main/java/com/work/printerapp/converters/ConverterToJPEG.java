package com.work.printerapp.converters;

import org.apache.pdfbox.jbig2.JBIG2ImageReaderSpi;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import  org.apache.pdfbox.tools.imageio.ImageIOUtil;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import java.awt.image.BufferedImage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConverterToJPEG implements ConverterPDF,Runnable {
    private File inputPDF;
    private String destination;
    private String imagesFolder;
    public ConverterToJPEG(String dest, File inputPDF) {
        this.inputPDF = inputPDF;
        destination = dest;
    }

    public void setInputPDF(File inputPDF) {
        this.inputPDF = inputPDF;
    }

public String getImagesFolder() {
        return  imagesFolder;
}
    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void  convert()  {
        IIORegistry.getDefaultInstance().registerServiceProvider(new JBIG2ImageReaderSpi());
        if (inputPDF.exists()) {
            try( PDDocument doc = PDDocument.load(inputPDF)) {
                PDFRenderer renderer = new PDFRenderer(doc);
                String fileName = inputPDF.getName().replace(".pdf", "");
                Path savePath = Paths.get(destination +  File.separator + fileName);

                Files.createDirectories(savePath);
                System.out.println("CONVERTER START.....");
                int pageCounter = 0 ;
                for (PDPage page : doc.getPages()) {
                    String imageName = fileName + "_" + (pageCounter++) + ".jpg";
                    File fileTemp = new File(savePath.toFile() ,   imageName); // jpg or png
                    if(!fileTemp.exists()) {
                        BufferedImage image = renderer.renderImageWithDPI(pageCounter, 75, ImageType.RGB);
                        ImageIO.write(image, "jpg", fileTemp);
                    }
                }
            } catch (SecurityException ex) {
                System.err.println(" destination folder do not found");
            }
            catch (IOException e) {
                System.err.println("error to open file");
            }
        }
    }

    @Override
    public void run() {
        this.convert();
    }
}

