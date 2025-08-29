package com.project.batch.jobconfig.product.report;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.project.batch.domain.product.Product;
import com.project.batch.jobconfig.BaseBatchIntegrationTest;
import com.project.batch.service.product.ProductService;
import com.project.batch.service.product.report.ProductReportService;
import com.project.batch.util.DateTimeUtils;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"spring.batch.job.name=productReportJob"})
class ProductReportJobconfigurationTest extends BaseBatchIntegrationTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductReportService productReportService;

    @Test
    public void testJob(@Autowired Job productReportJob) throws Exception {
        LocalDate now = LocalDate.now();
        saveProduct();

        jobLauncherTestUtils.setJob(productReportJob);
        JobParameters jobParameters = new JobParametersBuilder().toJobParameters();

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertAll(
                () -> assertThat(productReportService.countCategoryReport(now)).isEqualTo(1),
                () -> assertThat(productReportService.countBrandReport(now)).isEqualTo(2),
                () -> assertThat(productReportService.countManufacturerReport(now)).isEqualTo(2),
                () -> assertThat(productReportService.countProductStatusReport(now)).isEqualTo(2),
                () -> assertJobCompleted(jobExecution)
        );
    }

    private void saveProduct() {
        productService.save(Product.of("1", 65L, "화장품", "소설",
                LocalDate.of(2023, 8, 12), LocalDate.of(2026, 7, 13), "OUT_OF_STOCK",
                "삼성전자", "아모레퍼시픽", 273924, 882,
                DateTimeUtils.toLocalDateTime("2025-08-27 14:24:40.452"),
                DateTimeUtils.toLocalDateTime("2025-08-27 14:24:40.452")
        ));
        productService.save(Product.of("2", 86L, "화장품", "스마트폰",
                LocalDate.of(2020, 6, 16), LocalDate.of(2026, 10, 27), "DISCOUNTINUED",
                "LG전자", "LG", 106587, 257,
                DateTimeUtils.toLocalDateTime("2025-08-27 14:25:40.452"),
                DateTimeUtils.toLocalDateTime("2025-08-27 14:25:40.452")
        ));
    }
}