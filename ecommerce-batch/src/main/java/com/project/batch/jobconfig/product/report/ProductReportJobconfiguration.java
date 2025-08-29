package com.project.batch.jobconfig.product.report;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class ProductReportJobconfiguration {

    @Bean
    public Job productReportJob(JobRepository jobRepository, JobExecutionListener listener,
            TaskExecutor taskExecutor,
            Flow categoryReportFlow, Flow brandReportFlow, Flow manufacturerReportFlow,
            Flow productStatusReportFlow) {
        return new JobBuilder("productReportJob", jobRepository)
                .listener(listener)
                .start(categoryReportFlow)
                .split(taskExecutor)
                .add(brandReportFlow, manufacturerReportFlow, productStatusReportFlow)
                .end()
                .build();
    }

}
