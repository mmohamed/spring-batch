package com.medinvention.writer;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.medinvention.dao.Person;

public class PersonWriterToFile implements ItemWriter<Person> {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonWriterToFile.class);

    private List<Person> collection;

    private Writer writer;

    public PersonWriterToFile() {
        this.collection = new ArrayList<Person>();
    }

    public void write(List<? extends Person> items) throws Exception {

        LOGGER.info("Received the information of " + items.size() + " persons to be writed on file");

        for (Person person : items) {
            this.collection.add(person);
        }

        ObjectMapper mapper = new ObjectMapper();
        String jsonInString = mapper.writeValueAsString(this.collection);

        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("sample-data.json"), "utf-8"));
            writer.write(jsonInString);
        }
        catch (IOException ex) {
            LOGGER.error("Can't print JSON file");
        }
        finally {
            try {
                writer.close();
            }
            catch (Exception ex) {
            }
        }
    }
}
