package com.capgemini.tasklet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

public class FileConcatTasklet implements Tasklet {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Resource directory;

    private String outputFilename;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws IOException {

        File dir = directory.getFile();

        File[] files = dir.listFiles();

        try (OutputStream os = new FileOutputStream(new File(outputFilename), false)) {

            Integer concatedFile = 0;

            for (int i = 0; i < files.length; i++) {

                if (files[i].getName().lastIndexOf("paged-") == 0) {

                    this.concat(os, files[i], files.length, concatedFile, i);

                    Files.delete(Paths.get(files[i].getPath()));

                    log.info("{} readed and deleted!", files[i].getPath());

                    concatedFile++;
                }
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

    public String getOutputFilename() {
        return outputFilename;
    }

    public void setOutputFilename(String outputFilename) {
        this.outputFilename = outputFilename;
    }

    private void concat(OutputStream os, File file, Integer length, Integer concatedFile, Integer index)
            throws IOException {
        try (InputStream in = new FileInputStream(file)) {

            byte[] buffer = new byte[1 << 20]; // loads 1 MB of the file

            int count;

            while ((count = in.read(buffer)) != -1) {

                if (count > 0 && index + 1 != length && buffer[count - 1] == ']') {
                    buffer[count - 1] = ',';
                }

                if (count > 0 && concatedFile != 0 && buffer[0] == '[') {
                    buffer[0] = ' ';
                }
                os.write(buffer, 0, count);
                os.flush();
            }
        }
    }
}
