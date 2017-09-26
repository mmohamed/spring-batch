package com.capgemini.tasklet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;

import com.capgemini.dao.IPersonRepository;

public class CleanDBTasklet implements Tasklet {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private IPersonRepository iPersonRepository;

    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

        iPersonRepository.deleteAll();

        log.info("BD Cleared");

        return RepeatStatus.FINISHED;
    }

    /**
     * @return the iPersonRepository
     */
    public IPersonRepository getiPersonRepository() {
        return iPersonRepository;
    }

    /**
     * @param iPersonRepository the iPersonRepository to set
     */
    public void setiPersonRepository(IPersonRepository iPersonRepository) {
        this.iPersonRepository = iPersonRepository;
    }

}
