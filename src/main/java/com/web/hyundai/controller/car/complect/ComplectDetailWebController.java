package com.web.hyundai.controller.car.complect;

import com.web.hyundai.model.car.complectdetail.*;
import com.web.hyundai.model.car.modif.CarComplect;
import com.web.hyundai.repo.car.modif.CarComplectRepo;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/complect/detail")
@Api(tags = "Complect Detail Web")
@CrossOrigin("*")
public class ComplectDetailWebController {

    private final ComplectOverviewRepo complectOverviewRepo;
    private final ComplectSpecificationRepo complectSpecificationRepo;
    private final CarComplectRepo carComplectRepo;


    @GetMapping("/get/{complectId}")
    public ResponseEntity<ComplectDetailDto> getComplectDetails(@PathVariable Long complectId) {
        Optional<ComplectOverview> overview = complectOverviewRepo.findByComplectId(complectId);
        Optional<ComplectSpecification> spec = complectSpecificationRepo.findByComplectId(complectId);
        Optional<CarComplect> complect = carComplectRepo.findById(complectId);
        if (overview.isPresent() && spec.isPresent() && complect.isPresent()) {
            return ResponseEntity.ok(ComplectDetailDto.builder()
                    .complectOverview(overview.get())
                    .complectSpecification(spec.get())
                    .build());

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ComplectDetailDto.builder().build());
    }
}
