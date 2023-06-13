package com.lis.jobrunzsample.model;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name="job_schedule")
public class JobSchedule {
    @Id
    //@GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "job_id")
    private String jobId;

    @Column(name = "name")
    private String name;

    @Column(name = "cron")
    private String cron;

    @Column(name = "exec_type")
    private String execType;

    @Column(name = "exec_context")
    private String execContext;

    @Column(name = "description")
    private String description;
}
