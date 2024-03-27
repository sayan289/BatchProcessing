package com.springbatch.config;

import com.springbatch.entity.Customer;
import com.springbatch.entity.CustomerDetails;
import com.springbatch.repository.CustomerDetailsRepository;
import com.springbatch.repository.CustomerRepository;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.data.builder.RepositoryItemWriterBuilder;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SpringBatchDbtoDb {
    @Autowired
    CustomerDetailsRepository customerDetailsRepository;
    @Autowired
    private EntityManagerFactory entityManagerFactory;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private JobRepository jobRepository;
    @Bean
    public JpaPagingItemReader<Customer> reader() {
        return new JpaPagingItemReaderBuilder<Customer>()
                .name("customerItemReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT c FROM Customer c") // Specify your JPQL query here
                .pageSize(10) // Adjust the page size as needed
                .build();
    }

    @Bean
    public RepositoryItemWriter<CustomerDetails> writer() {
        return new RepositoryItemWriterBuilder<CustomerDetails>()
                .repository(customerDetailsRepository)
                .methodName("save")
                .build();
    }
    @Bean
    public CustomerProcessor processor()
    {
        return new CustomerProcessor();
    }
    @Bean
    public Step importStep() {
        return new StepBuilder("importStep",jobRepository)
                .<Customer, CustomerDetails>chunk(10,platformTransactionManager)
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .transactionManager(platformTransactionManager)
                .build();
    }
    @Bean
    public Job runJob()
    {
        return new JobBuilder("importCustomer",jobRepository)
                .start(importStep())
                .build();
    }
    @Bean
    public TaskExecutor taskExecutor()
    {
        SimpleAsyncTaskExecutor asyncTaskExecutor=new SimpleAsyncTaskExecutor();
        asyncTaskExecutor.setConcurrencyLimit(10);
        return asyncTaskExecutor;
    }
}
