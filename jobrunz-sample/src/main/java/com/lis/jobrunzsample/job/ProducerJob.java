package com.lis.jobrunzsample.job;

import lombok.extern.slf4j.Slf4j;
import org.jobrunr.jobs.annotations.Job;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProducerJob {
    @Job(retries = 2)
    public void execute(String context){
        log.info(context);
    }
}
