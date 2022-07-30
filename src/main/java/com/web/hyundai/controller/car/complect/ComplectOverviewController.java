package com.web.hyundai.controller.car.complect;

import com.web.hyundai.model.car.complectdetail.ComplectOverview;
import com.web.hyundai.model.car.complectdetail.ComplectOverviewRepo;
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
@RequestMapping("/admin/complect/overview")
@Api(tags = "Complect Overview")
@CrossOrigin("*")
public class ComplectOverviewController {
    private final ComplectOverviewRepo complectOverviewRepo;
    private final CarComplectRepo carComplectRepo;

    @GetMapping("/get/{complectId}")
    public ResponseEntity<ComplectOverview> getComplectOverviewByComplectId(
            @PathVariable Long complectId) {
        Optional<ComplectOverview> overview = complectOverviewRepo.findByComplectId(complectId);
        return overview.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ComplectOverview.builder().build()));
    }


    @PostMapping("/create/{complectId}")
    public ResponseEntity<ComplectOverview> createComplectOverview(@PathVariable Long complectId,
                                                                   @RequestParam String field,
                                                                   @RequestParam String fieldGeo,
                                                                   @RequestParam String value,
                                                                   @RequestParam String valueGeo) {
        Optional<CarComplect> complect = carComplectRepo.findById(complectId);
        Optional<ComplectOverview> overview = complectOverviewRepo.findByComplectId(complectId);
        if (complect.isPresent() && overview.isEmpty()) {
            return ResponseEntity.ok(
                    complectOverviewRepo.save(
                            ComplectOverview.builder()
                                    .field(field)
                                    .fieldGeo(fieldGeo)
                                    .value(value)
                                    .valueGeo(valueGeo)
                                    .carComplect(complect.get())
                                    .build()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ComplectOverview.builder().build());

    }

    @PostMapping("/update/{overviewId}")
    public ResponseEntity<ComplectOverview> updateComplectOverview(@PathVariable Long overviewId,
                                                                   @RequestParam String field,
                                                                   @RequestParam String fieldGeo,
                                                                   @RequestParam String value,
                                                                   @RequestParam String valueGeo) {
        Optional<ComplectOverview> overview = complectOverviewRepo.findById(overviewId);
        if (overview.isPresent()) {
            return ResponseEntity.ok(
                    complectOverviewRepo.save(
                            ComplectOverview.builder()
                                    .field(field)
                                    .fieldGeo(fieldGeo)
                                    .value(value)
                                    .valueGeo(valueGeo)
                                    .build()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ComplectOverview.builder().build());

    }

    @PostMapping("/delete/{overviewId}")
    public ResponseEntity<String> updateComplectOverview(@PathVariable Long overviewId) {
        Optional<ComplectOverview> overview = complectOverviewRepo.findById(overviewId);
        if (overview.isPresent()) {
            complectOverviewRepo.delete(overview.get());
            return ResponseEntity.ok("deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");

    }
}
