package com.capgemini.reader;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.batch.item.ItemReader;

public class LineReaderFromFile implements ItemReader<String> {

    private String inputPath;
    private BufferedReader reader = null;

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String read() throws IOException {
        if (null == reader) {
            reader = new BufferedReader(new FileReader(this.inputPath));
        }
        return reader.readLine();
    }
}
