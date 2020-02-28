package com.capgemini.partitioner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

public class FilePartitioner implements Partitioner {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String outputPath;

    public Map<String, ExecutionContext> partition(int gridSize) {

        int index = 0;

        File directory = new File(outputPath);

        File[] fList = directory.listFiles();

        Map<String, ExecutionContext> result = new HashMap<>();

        for (File file : fList) {

            String extension = "";

            int i = file.getName().lastIndexOf('.');
            if (i > 0) {
                extension = file.getName().substring(i + 1);
            }

            if (0 == extension.compareTo("csv") && file.isFile()) {

                ExecutionContext exContext = new ExecutionContext();

                log.info("Starting : Thread [{}] for file : {}", index, file.getName());

                exContext.put("name", "Thread" + index);
                exContext.put("file", file.getName());
                
                result.put("partition" + index, exContext);
                
                index++;
            }
        }

        return result;
    }

    public String getOutputPath() {
        return outputPath;
    }

    public void setOutputPath(String outputPath) {
        this.outputPath = outputPath;
    }

}
