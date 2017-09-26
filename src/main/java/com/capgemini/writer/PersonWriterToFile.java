package com.capgemini.writer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.capgemini.entity.Person;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class PersonWriterToFile implements ItemWriter<Person> {

    private String outputPath;
    private Boolean autoInitialized;
    private List<Person> collection;

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    public PersonWriterToFile() {
        collection = new ArrayList<Person>();
        autoInitialized = true;
    }

    public void write(List<? extends Person> items) throws Exception {
        for (Person person : items) {
            this.collection.add(person);
        }

        if (autoInitialized) {
            this.flush();
        }
    }

    public void initialize(String filename) {
        autoInitialized = false;
        collection = new ArrayList<Person>();
        outputPath = filename;
    }

    public void flush() {
        ObjectMapper mapper = new ObjectMapper();

        DateFormat df = new SimpleDateFormat("d/M/yyyy");

        mapper.setDateFormat(df).writer().withDefaultPrettyPrinter();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            String jsonInString = mapper.writeValueAsString(collection);

            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.outputPath), "utf-8"));
            writer.write(jsonInString);
            writer.close();
        }
        catch (IOException ex) {
            log.error("Can't print JSON file");
        }
    }

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

}
