package com.project.batch.jobconfig.transcation.report;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.batch.domain.transaction.report.TransactionReport;
import com.project.batch.domain.transaction.report.TransactionReportMapRepository;
import com.project.batch.dto.transaction.TransactionLog;
import com.project.batch.service.file.SplitFilePartitioner;
import com.project.batch.service.transaction.TransactionReportAccumulator;
import com.project.batch.util.FileUtils;
import java.io.File;
import javax.sql.DataSource;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.partition.PartitionHandler;
import org.springframework.batch.core.partition.support.TaskExecutorPartitionHandler;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.support.IteratorItemReader;
import org.springframework.batch.item.support.SynchronizedItemStreamReader;
import org.springframework.batch.item.support.builder.SynchronizedItemStreamReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class TransactionReportJobConfiguration {

    @Bean
    public Job transactionReportJob(
            JobRepository jobRepository,
            JobExecutionListener listener,
            @Qualifier("transactionAccPartitionStep") Step transactionAccPartitionStep,
            @Qualifier("transactionSaveStep") Step transactionSaveStep
    ) {
        return new JobBuilder("transactionReportJob", jobRepository)
                .start(transactionAccPartitionStep)
                .next(transactionSaveStep)
                .listener(listener)
                .build();
    }

    @Bean
    @JobScope
    public Step transactionAccPartitionStep(
            JobRepository jobRepository,
            SplitFilePartitioner splitLogFilePartitioner,
            @Qualifier("transctionAccStep") Step transctionAccStep,
            @Qualifier("logFilePartitionHandler") PartitionHandler logFilePartitionHandler
    ) {
        return new StepBuilder("transactionAccPartitionStep", jobRepository)
                .partitioner(transctionAccStep.getName(), splitLogFilePartitioner)
                .partitionHandler(logFilePartitionHandler)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    @JobScope
    public SplitFilePartitioner splitLogFilePartitioner(
            @Value("#{jobParameters['inputFilePath']}") String path,
            @Value("#{jobParameters['gridSize']}") int gridSize
    ) {
        return new SplitFilePartitioner(FileUtils.splitLog(new File(path), gridSize));
    }

    @Bean
    @JobScope
    public TaskExecutorPartitionHandler logFilePartitionHandler(
            TaskExecutor taskExecutor,
            @Qualifier("transctionAccStep") Step transctionAccStep,
            @Value("#{jobParameters['gridSize']}") int gridSize
    ) {
        TaskExecutorPartitionHandler handler = new TaskExecutorPartitionHandler();
        handler.setTaskExecutor(taskExecutor);
        handler.setStep(transctionAccStep);
        handler.setGridSize(gridSize);
        return handler;
    }

    @Bean
    public Step transctionAccStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            StepExecutionListener listener,
            ItemReader<TransactionLog> logReader,
            ItemWriter<TransactionLog> logAccumulator,
            TaskExecutor taskExecutor
    ) {
        return new StepBuilder("transctionAccStep", jobRepository)
                .<TransactionLog, TransactionLog>chunk(10, transactionManager)
                .reader(logReader)
                .writer(logAccumulator)
                .listener(listener)
                .allowStartIfComplete(true)
                .taskExecutor(taskExecutor)
                .build();
    }

    @Bean
    @StepScope
    public SynchronizedItemStreamReader<TransactionLog> logReader(
            @Value("#{stepExecutionContext['file']}") File file,
            ObjectMapper objectMapper
    ) {
        FlatFileItemReader<TransactionLog> logReader = new FlatFileItemReaderBuilder<TransactionLog>()
                .name("logReader")
                .resource(new FileSystemResource(file))
                .lineMapper((((line, lineNumber) -> objectMapper.readValue(line,
                        TransactionLog.class))))
                .build();

        return new SynchronizedItemStreamReaderBuilder<TransactionLog>()
                .delegate(logReader)
                .build();
    }

    @Bean
    public ItemWriter<TransactionLog> logAccumulator(
            TransactionReportAccumulator accumulator
    ) {
        return chunk -> {
            for (TransactionLog transactionLog : chunk.getItems()) {
                accumulator.accumulate(transactionLog);
            }
        };
    }

    @Bean
    public Step transactionSaveStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            ItemReader<TransactionReport> reportReader,
            ItemWriter<TransactionReport> reportWriter,
            StepExecutionListener listener
    ) {
        return new StepBuilder("transactionSaveStep", jobRepository)
                .<TransactionReport, TransactionReport>chunk(10, transactionManager)
                .reader(reportReader)
                .writer(reportWriter)
                .listener(listener)
                .allowStartIfComplete(true)
                .build();
    }

    @Bean
    @StepScope
    public ItemReader<TransactionReport> reportReader(
            TransactionReportMapRepository repository
    ) {
        return new IteratorItemReader<>(repository.getTransactionReports());
    }

    @Bean
    @StepScope
    public JdbcBatchItemWriter<TransactionReport> writer(DataSource dataSource) {
        String sql =
                "insert into transaction_reports(transaction_date, transaction_type, transaction_count, "
                        + "total_amount, customer_count, order_count, payment_method_count, avg_product_count, total_item_quantity)"
                        + " values (:transactionDate, :transactionType, :transactionCount, :totalAmount, :customerCount,"
                        + " :orderCount, :paymentMethodCount, :avgProductCount, :totalItemQuantity)";
        return new JdbcBatchItemWriterBuilder<TransactionReport>()
                .dataSource(dataSource)
                .sql(sql)
                .beanMapped()
                .build();
    }

}
