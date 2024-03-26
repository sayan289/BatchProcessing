package com.springbatch.config;

import com.springbatch.entity.Customer;
import com.springbatch.repository.CustomerRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class SpringBatchConfig {

//    private JobBuilderFactory jobBuilderFactory;
//    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    private PlatformTransactionManager platformTransactionManager;
    @Autowired
    private JobRepository jobRepository;
    @Autowired
    private CustomerRepository repository;
    @Bean
    public FlatFileItemReader<Customer> itemreader()
    {
        FlatFileItemReader<Customer> itemReader=new  FlatFileItemReader<>();
        itemReader.setResource(new FileSystemResource("src/main/resources/customers.csv"));
        itemReader.setName("csvReader");
        itemReader.setLinesToSkip(1);
        itemReader.setLineMapper(linemaper());
        return itemReader;
    }
    @Bean
    public CustomerProcessor processor()
    {
        return new CustomerProcessor();
    }
    @Bean
    public  LineMapper<Customer> linemaper() {
        DefaultLineMapper<Customer> lineMapper=new DefaultLineMapper<>();
        DelimitedLineTokenizer lineTokenizer=new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setStrict(false);
        lineTokenizer.setNames("id", "firstName", "lastName", "email", "gender", "contactNo", "country", "dob");
        BeanWrapperFieldSetMapper<Customer> fieldSetMapper=new BeanWrapperFieldSetMapper<>();
        fieldSetMapper.setTargetType(Customer.class);
        lineMapper.setLineTokenizer(lineTokenizer);
        lineMapper.setFieldSetMapper(fieldSetMapper);
        return lineMapper;

    }
    @Bean
    public RepositoryItemWriter<Customer>writer()
    {
        RepositoryItemWriter<Customer> writer=new RepositoryItemWriter<>();
        writer.setRepository(repository);
        writer.setMethodName("save");
        return writer;
    }
    @Bean
    public Step importStep(){
        return new StepBuilder("csvImport",jobRepository)
                .<Customer,Customer>chunk(10,platformTransactionManager)
                .reader(itemreader())
                .processor(processor())
                .writer(writer())
                .build();
    }
    @Bean
    public Job runJob()
    {
        return new JobBuilder("importCustomer",jobRepository)
                .start(importStep())
                .build();
    }
}
