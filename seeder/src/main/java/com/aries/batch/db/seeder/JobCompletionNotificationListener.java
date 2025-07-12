package com.aries.batch.db.seeder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class JobCompletionNotificationListener implements JobExecutionListener {

  private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

  @Override
  public void afterJob(@NonNull JobExecution jobExecution) {
    log.info("!!! JOB FINISHED !!!");
    JobExecutionListener.super.afterJob(jobExecution);
  }
}
