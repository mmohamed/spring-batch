package com.capgemini.writer;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class LineWriterToFile implements ItemWriter<String> {

    private String outputPath;

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void write(List<? extends String> items) throws Exception {

        int newFileIndex = new File(outputPath).listFiles().length;

        try (FileWriter writer = new FileWriter(outputPath + "parsed-" + newFileIndex + ".csv")) {

            for (String line : items) {
                writer.append(line + "\n");
            }
        }
    }
}
