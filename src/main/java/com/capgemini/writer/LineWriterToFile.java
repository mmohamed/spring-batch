package com.capgemini.writer;

import java.io.File;
import java.io.FileWriter;
import java.util.List;

import org.springframework.batch.item.ItemWriter;

public class LineWriterToFile implements ItemWriter<String> {

    private String outputPath;

    /**
     * @return the outputPath
     */
    public String getOutputPath() {
        return outputPath;
    }

    /**
     * @param outputPath the outputPath to set
     */
    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

    public void write(List<? extends String> items) throws Exception {

        int newFileIndex = new File(outputPath).listFiles().length;

        FileWriter writer = new FileWriter(outputPath + "parsed-" + newFileIndex + ".csv");

        for (String line : items) {
            writer.append(line + "\n");
        }
        writer.close();
    }
}
