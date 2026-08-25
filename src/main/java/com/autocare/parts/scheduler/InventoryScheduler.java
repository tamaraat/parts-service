package com.autocare.parts.scheduler;

import com.autocare.parts.entity.SparePart;
import com.autocare.parts.repository.SparePartRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class InventoryScheduler {

    private static final Logger log =
            LoggerFactory.getLogger(InventoryScheduler.class);

    private static final int LOW_STOCK_THRESHOLD = 3;
    private static final int RESTOCK_QUANTITY = 10;

    private final SparePartRepository sparePartRepository;

    public InventoryScheduler(
            SparePartRepository sparePartRepository
    ) {
        this.sparePartRepository = sparePartRepository;
    }

    @Scheduled(cron = "0 0 3 * * *")
    @Transactional
    @CacheEvict(
            value = "spareParts",
            allEntries = true
    )
    public void restockLowStockParts() {

        List<SparePart> lowStockParts =
                sparePartRepository
                        .findAll()
                        .stream()
                        .filter(part ->
                                part.getQuantity() != null
                                        && part.getQuantity()
                                        < LOW_STOCK_THRESHOLD
                        )
                        .toList();

        if (lowStockParts.isEmpty()) {
            log.info(
                    "Scheduled stock check completed. No parts require restocking"
            );
            return;
        }

        lowStockParts.forEach(
                part -> part.setQuantity(
                        RESTOCK_QUANTITY
                )
        );

        sparePartRepository.saveAll(
                lowStockParts
        );

        log.info(
                "Scheduled restock completed. Restocked {} spare parts",
                lowStockParts.size()
        );
    }

    @Scheduled(
            fixedDelay = 600000,
            initialDelay = 600000
    )
    @CacheEvict(
            value = "spareParts",
            allEntries = true
    )
    public void refreshSparePartsCache() {

        log.info(
                "Scheduled spare parts cache refresh completed"
        );
    }
}