package com.capgemini.tasklet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.UnexpectedJobExecutionException;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.core.io.Resource;

public class FileConcatTasklet implements Tasklet {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Resource directory;

    private String outputFilename;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        File dir = directory.getFile();

        File[] files = dir.listFiles();

        OutputStream os = new FileOutputStream(new File(outputFilename), false);

        Integer concatedFile = 0;

        for (int i = 0; i < files.length; i++) {

            if (files[i].getName().lastIndexOf("paged-") == 0) {

                InputStream in = new FileInputStream(files[i]);

                byte[] buffer = new byte[1 << 20]; // loads 1 MB of the file

                int count;

                while ((count = in.read(buffer)) != -1) {

                    if (count > 0 && i + 1 != files.length && buffer[count - 1] == ']') {
                        buffer[count - 1] = ',';
                    }

                    if (count > 0 && concatedFile != 0 && buffer[0] == '[') {
                        buffer[0] = ' ';
                    }
                    os.write(buffer, 0, count);
                    os.flush();
                }

                in.close();

                boolean deleted = files[i].delete();

                if (!deleted) {
                    throw new UnexpectedJobExecutionException("Could not delete file" + files[i].getPath());
                }
                else {
                    log.info(files[i].getPath() + " readed and deleted!");
                }

                concatedFile++;
            }
        }

        os.close();

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
}
