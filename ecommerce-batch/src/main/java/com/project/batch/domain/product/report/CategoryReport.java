package com.project.batch.domain.product.report;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CategoryReport {

    private LocalDate statDate = LocalDate.now();

    private String category;
    private Long productCount;
    private Double avgSalesPrice;
    private Integer maxSalesPrice;
    private Integer minSalesPrice;
    private Long totalStockQuantity;
    private Long potentialSalesAmount;


}
