package com.capgemini.tasklet;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

public class FileDeletingTasklet implements Tasklet {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Resource directory;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws IOException {

        File dir = directory.getFile();

        File[] files = dir.listFiles();
        for (int i = 0; i < files.length; i++) {

            String extension = "";

            int index = files[i].getName().lastIndexOf('.');
            if (index > 0) {
                extension = files[i].getName().substring(index + 1);
            }

            if (0 == extension.compareTo("csv") || 0 == extension.compareTo("json")) {
                Files.delete(Paths.get(files[i].getPath()));

                log.info("{} is deleted!", files[i].getPath());
            }

        }
        return RepeatStatus.FINISHED;
    }

    public Resource getDirectory() {
        return directory;
    }

    public void setDirectory(Resource directory) {
        this.directory = directory;
    }
}
