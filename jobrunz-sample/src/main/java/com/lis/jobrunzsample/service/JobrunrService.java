package com.lis.jobrunzsample.service;

import com.lis.jobrunzsample.job.ProducerJob;
import com.lis.jobrunzsample.job.RestJob;
import com.lis.jobrunzsample.model.JobSchedule;
import com.lis.jobrunzsample.repository.JobScheduleRepostory;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.RecurringJobBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
public class JobrunrService {

    private JobScheduleRepostory repo;

    private JobScheduler jobScheduler;

    public JobrunrService(JobScheduleRepostory repo, JobScheduler jobScheduler) {
        this.repo = repo;
        this.jobScheduler = jobScheduler;
    }

    @PostConstruct
    public void scheduleRecurrently() {



        for (JobSchedule entry: repo.findAll())
        {
             log.info(entry.getCron());
             var jobId = entry.getJobId();
             var jobName = entry.getName();
             var cron = entry.getCron();
             var context = entry.getExecContext();
            var execType = entry.getExecType();

            if (execType.equals("rest")) {
                buildRestJob(jobId, jobName, cron, context);
            }
            else if (execType.equals("event")) {
                buildProducerJob(jobId, jobName, cron, context);
            }
            else {
                throw new IllegalArgumentException("unkown type" + execType);
            }



        }


    }

    public void delete(String jobId) {
        jobScheduler.delete(jobId);
    }

    public void buildRestJob(String jobId, String jobName,
                             String cron, String context) {
        RestJob clazz =  new RestJob();
        jobScheduler.createRecurrently(
                RecurringJobBuilder.aRecurringJob()
                        .withId(jobId)
                        .withName(jobName)
                        //.withAmountOfRetries(3)
                        .withCron(cron)
                        .withDetails(() -> clazz.execute(context))
        );
    }

    public void buildProducerJob(String jobId, String jobName,
                             String cron, String context) {
        ProducerJob clazz =  new ProducerJob();
        jobScheduler.createRecurrently(
                RecurringJobBuilder.aRecurringJob()
                        .withId(jobId)
                        .withName(jobName)
                        //.withAmountOfRetries(3)
                        .withCron(cron)
                        .withDetails(() -> clazz.execute(context))
        );
    }
}
