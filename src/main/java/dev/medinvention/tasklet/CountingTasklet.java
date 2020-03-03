package dev.medinvention.tasklet;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;
import org.springframework.util.Assert;

public class CountingTasklet implements Tasklet, InitializingBean {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Resource input;

    public void afterPropertiesSet() throws Exception {
        Assert.notNull(input, "input file must be set");
    }

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        log.info("Couting tasklet for {} file.", input.getFilename());

        File dir = input.getFile();

        final Path path = Paths.get(dir.getPath());

        Long couting = 0L;

        try (Stream<String> i = Files.lines(path)) {
            couting = i.count();
        }

        chunkContext.getStepContext().getStepExecution().getJobExecution().getExecutionContext().putInt("chunk.count",
                (int) (couting / 10));

        log.info("Chunck input file is : {}", (int) (couting / 10));

        return RepeatStatus.FINISHED;
    }

    public Resource getInput() {
        return input;
    }

    public void setInput(Resource input) {
        this.input = input;
    }
}
