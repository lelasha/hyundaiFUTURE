package com.web.hyundai.model.car.complectdetail;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ComplectSpecificationRepo extends JpaRepository<ComplectSpecification,Long> {
    @Query(value = "select * from COMPLECT_SPECIFICATION where car_complect_id = :id",nativeQuery = true)
    Optional<ComplectSpecification> findByComplectId(@Param("id")Long id);
}
