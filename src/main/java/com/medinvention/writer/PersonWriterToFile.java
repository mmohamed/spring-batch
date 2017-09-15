package com.medinvention.writer;

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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.medinvention.dao.Person;

public class PersonWriterToFile implements ItemWriter<Person> {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private List<Person> collection;

    public void write(List<? extends Person> items) throws Exception {
        for (Person person : items) {
            this.collection.add(person);
        }
    }

    public void initialize() {
        collection = new ArrayList<Person>();
    }

    public void flush(String filename) {
        ObjectMapper mapper = new ObjectMapper();

        DateFormat df = new SimpleDateFormat("d/M/yyyy");

        mapper.setDateFormat(df).writer().withDefaultPrettyPrinter();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            String jsonInString = mapper.writeValueAsString(collection);

            Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "utf-8"));
            writer.write(jsonInString);
            writer.close();
        }
        catch (IOException ex) {
            log.error("Can't print JSON file");
        }
    }
}
