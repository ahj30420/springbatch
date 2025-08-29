package com.project.batch.domain.product.report;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ProductStatusReport {

    private LocalDate statDate = LocalDate.now();

    private String productStatus;
    private Long productCount;
    private Double avgStockQuantity;

}
