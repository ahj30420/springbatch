package com.project.ecommerceapi.domain.product.report;

import com.project.ecommerceapi.domain.product.ProductStatus;
import java.io.Serializable;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductStatusReportId implements Serializable {

    private LocalDate statDate;
    private ProductStatus productStatus;

}