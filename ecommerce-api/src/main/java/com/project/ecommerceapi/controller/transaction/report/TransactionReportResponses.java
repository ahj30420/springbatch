package com.project.ecommerceapi.controller.transaction.report;

import com.project.ecommerceapi.service.transaction.report.TransactionReportResults;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TransactionReportResponses {

    private List<TransactionReportResponse> reports;

    public static TransactionReportResponses from(TransactionReportResults results) {
        return new TransactionReportResponses(results.getResults().stream()
                .map(TransactionReportResponse::from)
                .collect(Collectors.toList()));
    }
}
