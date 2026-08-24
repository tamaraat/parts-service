package com.autocare.parts.repository;

import com.autocare.parts.entity.SparePart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface SparePartRepository
        extends JpaRepository<SparePart, UUID> {

    List<SparePart> findAllByOrderByNameAsc();

    boolean existsByPartNumberIgnoreCase(
            String partNumber
    );

    boolean existsByPartNumberIgnoreCaseAndIdNot(
            String partNumber,
            UUID id
    );
}