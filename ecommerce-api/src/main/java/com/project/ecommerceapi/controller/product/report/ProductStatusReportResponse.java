package com.project.ecommerceapi.controller.product.report;

import com.project.ecommerceapi.domain.product.ProductStatus;
import com.project.ecommerceapi.service.product.report.ProductStatusReportResult;
import java.time.LocalDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductStatusReportResponse {

    private LocalDate statDate;
    private ProductStatus productStatus;
    private Long productCount;
    private Double avgStockQuantity;

    public static ProductStatusReportResponse from(ProductStatusReportResult result) {
        return new ProductStatusReportResponse(
                result.getStatDate(),
                result.getProductStatus(),
                result.getProductCount(),
                result.getAvgStockQuantity()
        );
    }
}
