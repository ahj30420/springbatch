package com.project.batch.service.product;

import com.project.batch.domain.file.PartitionedFileRepository;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProductDownloadPartitioner implements Partitioner {

    private final ProductService productService;
    private final PartitionedFileRepository partitionedFileRepository;

    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {
        List<String> productIds = productService.getProductIds().stream()
                .sorted()
                .toList();

        int minIdx = 0;
        int maxIdx = productIds.size() - 1;

        Map<String, ExecutionContext> result = new HashMap<>();

        int targetSize = (maxIdx - minIdx) / gridSize + 1;

        int partitionNumber = 0;
        int start = minIdx;
        int end = start + targetSize - 1;

        while (start <= maxIdx) {
            ExecutionContext context = new ExecutionContext();
            String partitionKey = "partition" + partitionNumber;
            result.put(partitionKey, context);

            if (end >= maxIdx) {
                end = maxIdx;
            }

            context.putString("minId", productIds.get(start));
            context.putString("maxId", productIds.get(end));

            try {
                File partitionFile = partitionedFileRepository.putFile(partitionKey,
                        "partition" + partitionNumber + "_", ".csv");
                context.put("file", partitionFile);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            start += targetSize;
            end += targetSize;
            partitionNumber++;
        }

        return result;
    }
}
