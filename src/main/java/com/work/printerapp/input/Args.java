package com.work.printerapp.input;

import com.beust.jcommander.Parameter;

import javax.management.ConstructorParameters;

public class Args {
    @Parameter(
            names = {"-p", "--path"},
            description = "path to json configuration file"
    )
    private String pdfFolderPath;

    public String getPdfFolderPath() {
        return pdfFolderPath;
    }
}
