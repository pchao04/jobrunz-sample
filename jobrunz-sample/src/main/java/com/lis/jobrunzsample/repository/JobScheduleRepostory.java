package com.lis.jobrunzsample.repository;

import com.lis.jobrunzsample.model.JobSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.RequestParam;

public interface JobScheduleRepostory extends JpaRepository<JobSchedule, Long> {
    JobSchedule findJobScheduleByJobId(@RequestParam("jobId") String jobId);

    @Modifying
    @Query("update JobSchedule  set cron =:cron where jobId =:jobId")
    void updateCron(@Param("jobId") String  jobId, @Param("cron") String cron);
}
