package com.lis.jobrunzsample.service;

import com.lis.jobrunzsample.model.JobSchedule;
import com.lis.jobrunzsample.repository.JobScheduleRepostory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class JobScheduleService {
    private JobScheduleRepostory repo;
    private JobrunrService jobRunrService;
    public JobScheduleService(JobScheduleRepostory repo, JobrunrService jobRunrService) {
        this.repo = repo;
        this.jobRunrService = jobRunrService;
    }

    public List<JobSchedule> findAll() {

        return repo.findAll();
    }

    public JobSchedule getJob(String jobId) {
        return  repo.findJobScheduleByJobId(jobId);
    }

    public void updateCron (String jobId, String cron) {
        repo.updateCron(jobId,cron);
    }

    public void updateSchedule(String jobId, String cron) {
        var job = getJob(jobId);
        var execType = job.getExecType();
        if (job.getExecType().equals("rest")) {
            // update repo
            updateCron(jobId,cron);
            jobRunrService.delete(jobId);
            jobRunrService.buildRestJob(jobId, job.getName(),
                   cron, job.getExecContext());
        }
        else if (execType.equals("event")) {
            // update repo
            updateCron(jobId,cron);
            jobRunrService.delete(jobId);
            jobRunrService.buildProducerJob(jobId, job.getName(),
                    cron, job.getExecContext());
        }

    }
}
