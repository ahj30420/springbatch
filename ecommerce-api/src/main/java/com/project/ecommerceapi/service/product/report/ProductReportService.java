package com.project.ecommerceapi.service.product.report;

import com.project.ecommerceapi.domain.product.report.BrandReportRepository;
import com.project.ecommerceapi.domain.product.report.CategoryReportRepository;
import com.project.ecommerceapi.domain.product.report.ManufacturerReportRepository;
import com.project.ecommerceapi.domain.product.report.ProductStatusReportRepository;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductReportService {

    private final BrandReportRepository brandReportRepository;
    private final CategoryReportRepository categoryReportRepository;
    private final ManufacturerReportRepository manufacturerReportRepository;
    private final ProductStatusReportRepository productStatusReportRepository;

    public ProductReportResults findReports(LocalDate date) {
        return ProductReportResults.of(brandReportRepository.findAllByStatDate(date),
                categoryReportRepository.findAllByStatDate(date),
                manufacturerReportRepository.findAllByStatDate(date),
                productStatusReportRepository.findAllByStatDate(date)
        );
    }
}
