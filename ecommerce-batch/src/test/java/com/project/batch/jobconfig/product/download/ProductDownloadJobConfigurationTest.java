package com.project.batch.jobconfig.product.download;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.project.batch.domain.product.Product;
import com.project.batch.jobconfig.BaseBatchIntegrationTest;
import com.project.batch.service.product.ProductService;
import com.project.batch.util.DateTimeUtils;
import com.project.batch.util.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.test.context.TestPropertySource;

@TestPropertySource(properties = {"spring.batch.job.name=productDownloadJob"})
class ProductDownloadJobConfigurationTest extends BaseBatchIntegrationTest {

    @Value("classpath:/data/products_download_expected.csv")
    private Resource expectedResource;

    private File outputFile;

    @Autowired
    private ProductService productService;

    @Autowired
    @Qualifier("productDownloadJob")
    private Job productDownloadJob;

    @Test
    void testJob() throws Exception {
        saveProduct();
        outputFile = FileUtils.createTempFile("products_download", ".csv");

        JobParameters jobParameters = jobParameters();
        jobLauncherTestUtils.setJob(productDownloadJob);

        JobExecution jobExecution = jobLauncherTestUtils.launchJob(jobParameters);

        assertAll(
                () -> assertThat(Files.readString(Path.of(outputFile.getPath())).trim())
                        .isEqualTo(Files.readString(Path.of(expectedResource.getFile().getPath()))
                                .trim()),
                () -> assertJobCompleted(jobExecution)
        );

    }

    private void saveProduct() {
//1,65,화장품,소설,2023-08-12,2026-07-13,아모레퍼시픽,삼성전자,OUT_OF_STOCK,273924,882,2025-08-27 14:24:40.452,2025-08-27 14:24:40.452
//2,86,화장품,스마트폰,2020-06-16,2026-10-27,LG,LG전자,DISCOUNTINUED,106587,257,2025-08-27 14:25:40.452,2025-08-27 14:25:40.452
        productService.save(Product.of("1", 65L, "화장품", "소설",
                LocalDate.of(2023, 8, 12), LocalDate.of(2026, 7, 13), "아모레퍼시픽",
                "삼성전자", "OUT_OF_STOCK", 273924, 882,
                DateTimeUtils.toLocalDateTime("2025-08-27 14:24:40.452"),
                DateTimeUtils.toLocalDateTime("2025-08-27 14:24:40.452")
        ));
        productService.save(Product.of("2", 86L, "화장품", "스마트폰",
                LocalDate.of(2020, 6, 16), LocalDate.of(2026, 10, 27), "LG",
                "LG전자", "DISCOUNTINUED", 106587, 257,
                DateTimeUtils.toLocalDateTime("2025-08-27 14:25:40.452"),
                DateTimeUtils.toLocalDateTime("2025-08-27 14:25:40.452")
        ));
    }

    private JobParameters jobParameters() throws IOException {
        return new JobParametersBuilder()
                .addJobParameter("outputFilePath",
                        new JobParameter<>(outputFile.getPath(), String.class, false))
                .toJobParameters();
    }
}