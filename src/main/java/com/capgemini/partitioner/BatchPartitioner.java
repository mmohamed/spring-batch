package com.capgemini.partitioner;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

public class BatchPartitioner implements Partitioner {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private String outputPath;

    public Map<String, ExecutionContext> partition(int gridSize) {

        int index = 0;

        File directory = new File(outputPath);

        File[] fList = directory.listFiles();

        Map<String, ExecutionContext> result = new HashMap<String, ExecutionContext>();

        for (File file : fList) {
            if (file.isFile()) {

                ExecutionContext exContext = new ExecutionContext();
                log.info("Starting : Thread [" + index + "] for file : " + file.getName());
                exContext.put("name", "Thread" + index);
                exContext.put("file", file.getName());
                result.put("partition" + index, exContext);
                index++;
            }
        }

        return result;
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
