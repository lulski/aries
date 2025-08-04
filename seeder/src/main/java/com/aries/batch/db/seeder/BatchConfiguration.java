package com.aries.batch.db.seeder;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.MongoItemWriter;
import org.springframework.batch.item.data.builder.MongoItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BatchConfiguration {

  @Bean
  public FlatFileItemReader<User> reader() {
    String usersCsvPath = System.getenv().getOrDefault("USERS_CSV_PATH", "users.csv");
    return new FlatFileItemReaderBuilder<User>()
        .name("userRoleReader")
        .resource(new ClassPathResource(usersCsvPath))
        .delimited()
        .names("username", "password", "firstName", "lastName", "email", "authorities")
        .targetType(User.class)
        .build();
  }

  @Bean
  public UserRoleProcessor processor() {
    return new UserRoleProcessor(passwordEncoder());
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();

  }

  @Bean
  public MongoItemWriter<User> writer(MongoTemplate mongoTemplate) {
    return new MongoItemWriterBuilder<User>()
        .mode(MongoItemWriter.Mode.INSERT)
        .template(mongoTemplate)
        .build();
  }

  @Bean
  public Job importUserRoleJob(
      JobRepository jobRepository,
      Step step1,
      JobCompletionNotificationListener jobCompletionNotificationListener) {
    return new JobBuilder("importUserRoleJob", jobRepository)
        .listener(jobCompletionNotificationListener)
        .start(step1)
        .build();
  }

  @Bean
  public Step step1(
      JobRepository jobRepository,
      DataSourceTransactionManager transactionManager,
      MongoItemWriter<User> writer,
      UserRoleProcessor processor,
      FlatFileItemReader<User> reader) {
    return new StepBuilder("step1", jobRepository)
        .<User, User>chunk(3, transactionManager)
        .reader(reader)
        .processor(processor)
        .writer(writer)
        .build();
  }
}
