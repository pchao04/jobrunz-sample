package com.lis.jobrunzsample.controller;

import com.lis.jobrunzsample.job.ProducerJob;
import com.lis.jobrunzsample.job.RestJob;
import com.lis.jobrunzsample.model.JobSchedule;
import com.lis.jobrunzsample.service.JobScheduleService;
import org.jobrunr.scheduling.JobScheduler;
import org.jobrunr.scheduling.RecurringJobBuilder;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/jobrunr")
public class JobRunrController {

    private JobScheduler jobScheduler;

    private JobScheduleService service;



    private JobRunrController(JobScheduler jobScheduler,  JobScheduleService jobScheduleService) {
        this.jobScheduler = jobScheduler;
        this.service = jobScheduleService;


    }




    @GetMapping(value = "/update/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateSchedule(
            @PathVariable("jobId") String jobId,
            @RequestBody String cron) {

        var job = service.getJob(jobId);
        var execType = job.getExecType();

        if (job.getExecType().equals("rest")) {
            RestJob clazz =  new RestJob();
            service.updateCron(jobId,cron);
            jobScheduler.delete(jobId);
            jobScheduler.createRecurrently(
                    RecurringJobBuilder.aRecurringJob()
                            .withId(jobId)
                            .withName(job.getName())
                            //.withAmountOfRetries(3)
                            .withCron(cron)
                            .withDetails(() -> clazz.execute(job.getExecContext()))
            );
        }
        else if (execType.equals("event")) {
            ProducerJob clazz =  new ProducerJob();
            service.updateCron(jobId,cron);
            jobScheduler.delete(jobId);
            jobScheduler.createRecurrently(
                    RecurringJobBuilder.aRecurringJob()
                            .withId(jobId)
                            .withName(job.getName())
                            //.withAmountOfRetries(3)
                            .withCron(cron)
                            .withDetails(() -> clazz.execute(job.getExecContext()))
            );
        }


        return okResponse("job enqueued successfully");
    }

    //JobScheduleService
    @GetMapping(value = "/findAll", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<JobSchedule>> findAll() {

        List<JobSchedule> all = service.findAll();

        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @GetMapping(value = "/find/{jobId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<JobSchedule> findByJobId(
            @PathVariable("jobId") String jobId) {

        var all = service.getJob(jobId);

        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    private ResponseEntity<String> okResponse(String feedback) {
        return new ResponseEntity<>(feedback, HttpStatus.OK);
    }

}
