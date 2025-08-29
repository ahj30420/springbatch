package com.project.batch.domain.product.report;

import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ManufacturerReport {

    private LocalDate statDate = LocalDate.now();

    private String manufacturer;
    private Long productCount;
    private Double avgSalesPrice;
    private Long totalStockQuantity;

}
