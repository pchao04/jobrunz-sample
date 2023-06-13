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

    public JobScheduleService(JobScheduleRepostory repo) {
        this.repo = repo;
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
}
