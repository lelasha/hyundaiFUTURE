package com.web.hyundai.repo.car.modif;

import com.web.hyundai.model.car.complectcompare.ComplectCompareFeature;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplectCompareFeatureRepo extends JpaRepository<ComplectCompareFeature, Long> {
}
