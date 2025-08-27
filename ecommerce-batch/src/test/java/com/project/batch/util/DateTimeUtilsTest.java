package com.project.batch.util;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;

class DateTimeUtilsTest {

    @Test
    void testToLocalDate() {
        String date = "2024-09-21";

        LocalDate result = DateTimeUtils.toLocalDate(date);

        assertThat(result).isEqualTo(LocalDate.of(2024, 9, 21));
    }


    @Test
    void testToLocalDateTime() {
        String dateTime = "2024-09-21 14:20:22.154";

        LocalDateTime result = DateTimeUtils.toLocalDateTime(dateTime);

        assertThat(result).isEqualTo(LocalDateTime.of(2024, 9, 21,
                14, 20, 22, 154000000));
    }
}