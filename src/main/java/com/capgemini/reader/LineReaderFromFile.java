package com.capgemini.reader;

import java.io.BufferedReader;
import java.io.FileReader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class LineReaderFromFile implements ItemReader<String> {

    private String inputPath;
    private BufferedReader reader = null;

    public String getInputPath() {
        return inputPath;
    }

    public void setInputPath(String inputPath) {
        this.inputPath = inputPath;
    }

    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        if (null == reader) {
            reader = new BufferedReader(new FileReader(this.inputPath));
        }
        return reader.readLine();
    }
}
