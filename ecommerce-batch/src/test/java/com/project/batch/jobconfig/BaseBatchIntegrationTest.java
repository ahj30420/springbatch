package com.project.batch.jobconfig;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.project.batch.BatchApplication;
import javax.sql.DataSource;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@Sql("/sql/schema.sql")
@SpringBatchTest
@SpringJUnitConfig(classes = {BatchApplication.class})
public abstract class BaseBatchIntegrationTest {

    @Autowired
    protected JobLauncherTestUtils jobLauncherTestUtils;

    protected JdbcTemplate jdbcTemplate;

    @Autowired
    private void setDataSource(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    protected static void assertJobCompleted(JobExecution jobExecution) {
        assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());
    }
}
