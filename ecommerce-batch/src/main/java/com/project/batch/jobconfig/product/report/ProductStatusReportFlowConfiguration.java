package com.project.batch.jobconfig.product.report;

import com.project.batch.domain.product.report.ProductStatusReport;
import javax.sql.DataSource;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepExecutionListener;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.batch.item.database.builder.JdbcBatchItemWriterBuilder;
import org.springframework.batch.item.database.builder.JdbcCursorItemReaderBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class ProductStatusReportFlowConfiguration {

    @Bean
    public Flow productStatusReportFlow(Step productStatusReportStep) {
        return new FlowBuilder<SimpleFlow>("productStatusFlow")
                .start(productStatusReportStep)
                .build();
    }

    @Bean
    public Step productStatusReportStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            @Qualifier("productStatusReportReader") ItemReader<ProductStatusReport> productStatusReportReader,
            @Qualifier("productStatusReportWriter") ItemWriter<ProductStatusReport> productStatusReportWriter,
            StepExecutionListener listener
    ) {
        return new StepBuilder("productStatusReportStep", jobRepository)
                .<ProductStatusReport, ProductStatusReport>chunk(10, transactionManager)
                .allowStartIfComplete(true)
                .reader(productStatusReportReader)
                .writer(productStatusReportWriter)
                .listener(listener)
                .build();
    }

    @Bean
    public JdbcCursorItemReader<ProductStatusReport> productStatusReportReader(
            DataSource dataSource) {
        return new JdbcCursorItemReaderBuilder<ProductStatusReport>()
                .dataSource(dataSource)
                .name("brandReportReader")
                .sql("select product_status,"
                        + "    count(*)                               product_count,"
                        + "    avg(stock_quantity)                    avg_stock_quantity "
                        + "from products "
                        + "group by product_status")
                .beanRowMapper(ProductStatusReport.class)
                .build();
    }

    @Bean
    public JdbcBatchItemWriter<ProductStatusReport> productStatusReportWriter(
            DataSource dataSource) {
        return new JdbcBatchItemWriterBuilder<ProductStatusReport>()
                .dataSource(dataSource)
                .sql("insert into product_status_reports("
                        + "stat_date, product_status, product_count, avg_stock_quantity)"
                        + "values (:statDate, :productStatus, :productCount, :avgStockQuantity)"
                )
                .beanMapped()
                .build();
    }

}
