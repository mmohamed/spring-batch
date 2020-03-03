package dev.medinvention.partitioner;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;

public class DBPartitioner implements Partitioner {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private Integer pageSize;

    public Map<String, ExecutionContext> partition(int gridSize) {

        Map<String, ExecutionContext> result = new HashMap<>();

        for (int i = 0; i < gridSize; i++) {

            ExecutionContext exContext = new ExecutionContext();

            log.info("Starting : Thread [{}] for page : {}", i, i);

            exContext.put("name", "ThreadDBRead" + i);
            exContext.put("page", i);
            exContext.put("size", pageSize);
            
            result.put("partitionDB" + i, exContext);
        }

        return result;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
