package dev.medinvention.tasklet;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class CleanDBTasklet implements Tasklet {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @PersistenceContext
    private EntityManager em;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        em.createNativeQuery("DELETE FROM person").executeUpdate();

        log.info("BD Cleared");

        return RepeatStatus.FINISHED;
    }
}
