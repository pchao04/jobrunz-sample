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
             var desription = entry.getDescription();
             var context = entry.getExecContext();
              var execType = entry.getExecType();

            if (execType.equals("rest")) {

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
            else if (execType.equals("event")) {
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
            else {
                throw new IllegalArgumentException("unkown type" + execType);
            }



        }


    }
}
