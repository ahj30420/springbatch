package com.project.ecommerceapi.service.product.report;

import com.project.ecommerceapi.domain.product.report.BrandReport;
import com.project.ecommerceapi.domain.product.report.CategoryReport;
import com.project.ecommerceapi.domain.product.report.ManufacturerReport;
import com.project.ecommerceapi.domain.product.report.ProductStatusReport;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ProductReportResults {

    private List<BrandReportResult> brandReports;
    private List<CategoryReportResult> categoryReports;
    private List<ManufacturerReportResult> manufacturerReports;
    private List<ProductStatusReportResult> productStatusReports;

    public static ProductReportResults of(List<BrandReport> brandReports,
            List<CategoryReport> categoryReports, List<ManufacturerReport> manufacturerReports,
            List<ProductStatusReport> productStatusReportResults) {
        return new ProductReportResults(
                brandReports.stream()
                        .map(BrandReportResult::from)
                        .collect(Collectors.toList()),
                categoryReports.stream()
                        .map(CategoryReportResult::from)
                        .collect(Collectors.toList()),
                manufacturerReports.stream()
                        .map(ManufacturerReportResult::from)
                        .collect(Collectors.toList()),
                productStatusReportResults.stream()
                        .map(ProductStatusReportResult::from)
                        .collect(Collectors.toList())
        );
    }
}
