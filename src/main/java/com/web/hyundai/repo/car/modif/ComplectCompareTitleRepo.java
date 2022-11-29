package com.web.hyundai.repo.car.modif;



import com.web.hyundai.model.car.complectcompare.ComplectCompareTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ComplectCompareTitleRepo extends JpaRepository<ComplectCompareTitle, Long> {
    @Query(value = "select * from complect_compare_title where compare_id = :id and locale = :lang order by order_id", nativeQuery = true)
    List<ComplectCompareTitle> findByComplectAndLocale(@Param("id") Long id, @Param("lang") String lang);
}
