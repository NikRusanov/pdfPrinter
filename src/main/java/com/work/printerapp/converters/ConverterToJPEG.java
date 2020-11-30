package com.work.printerapp.converters;

import com.github.jaiimageio.jpeg2000.impl.J2KImageReaderSpi;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.jbig2.JBIG2ImageReaderSpi;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import javax.imageio.spi.IIORegistry;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConverterToJPEG implements ConverterPDF,Runnable {
    static {
        IIORegistry.getDefaultInstance().registerServiceProvider(new JBIG2ImageReaderSpi());
        IIORegistry.getDefaultInstance().registerServiceProvider(new J2KImageReaderSpi());
    }
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
                if (inputPDF.exists()) {
            try( PDDocument doc = PDDocument.load(inputPDF, MemoryUsageSetting.setupMixed(200000000))) {
                PDFRenderer renderer = new PDFRenderer(doc);
                String fileName = inputPDF.getName().replace(".pdf", "");
                Path savePath = Paths.get(destination +  File.separator + fileName);

                Files.createDirectories(savePath);
                System.out.println("CONVERTER START.....");
                for (int i = 0 ; i < doc.getNumberOfPages(); ++i) {
                    String imageName = fileName + "_" + i + ".jpg";
                    File fileTemp = new File(savePath.toFile() ,   imageName); // jpg or png
                    if(!fileTemp.exists()) {
                        BufferedImage image = renderer.renderImageWithDPI(i, 75, ImageType.RGB);
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

