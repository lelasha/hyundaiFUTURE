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
@RequestMapping("/admin/complect/specification/")
@Api(tags = "Complect Specification")
@CrossOrigin("*")
public class ComplectSpecificationController {
    private final ComplectSpecificationRepo complectSpecificationRepo;
    private final SpecificationTopicRepo specificationTopicRepo;
    private final SpecificationTopicDetailRepo specificationTopicDetailRepo;
    private final CarComplectRepo carComplectRepo;


    @GetMapping("/get/{complectId}")
    public ResponseEntity<ComplectSpecification> getComplectSpecificationByComplectId(
            @PathVariable Long complectId) {
        Optional<ComplectSpecification> spec = complectSpecificationRepo.findByComplectId(complectId);
        return spec.map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ComplectSpecification.builder().build()));
    }


    @PostMapping("/create/{complectId}")
    public ResponseEntity<ComplectSpecification> createComplectSpecification(@PathVariable Long complectId,
                                                                             @RequestParam String title,
                                                                             @RequestParam String titleGeo) {
        Optional<CarComplect> complect = carComplectRepo.findById(complectId);
        Optional<ComplectSpecification> spec = complectSpecificationRepo.findByComplectId(complectId);
        if (complect.isPresent() && spec.isEmpty()) {
            return ResponseEntity.ok(
                    complectSpecificationRepo.save(
                            ComplectSpecification.builder()
                                    .title(title)
                                    .titleGeo(titleGeo)
                                    .carComplect(complect.get())
                                    .build()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ComplectSpecification.builder().build());

    }

    @PostMapping("/update/{specId}")
    public ResponseEntity<ComplectSpecification> updateComplectSpecification(@PathVariable Long specId,
                                                                             @RequestParam String title,
                                                                             @RequestParam String titleGeo) {

        Optional<ComplectSpecification> spec = complectSpecificationRepo.findById(specId);
        if (spec.isPresent()) {
            spec.get().setTitle(title);
            spec.get().setTitleGeo(titleGeo);
            return ResponseEntity.ok(complectSpecificationRepo.save(spec.get()));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ComplectSpecification.builder().build());

    }

    @PostMapping("/delete/{id}")
    public ResponseEntity<String> deleteComplectSpecification(@PathVariable Long id) {
        Optional<ComplectSpecification> cmp = complectSpecificationRepo.findById(id);
        if (cmp.isPresent()) {
            complectSpecificationRepo.deleteById(id);
            return ResponseEntity.ok("deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");
    }

    @PostMapping("/topic/create/{specId}")
    public ResponseEntity<SpecificationTopic> createSpecificationTopic(@PathVariable Long specId,
                                                                       @RequestParam String title,
                                                                       @RequestParam String titleGeo) {

        Optional<ComplectSpecification> complectSpec = complectSpecificationRepo.findById(specId);
        if (complectSpec.isPresent()) {
            SpecificationTopic specificationTopic = specificationTopicRepo.save(
                    SpecificationTopic.builder()
                            .title(title)
                            .titleGeo(titleGeo)
                            .build());

            complectSpec.get().getSpecificationTopics().add(specificationTopic);
            complectSpecificationRepo.save(complectSpec.get());
            return ResponseEntity.ok(specificationTopic);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(SpecificationTopic.builder().build());
    }

    @PostMapping("/topic/delete/{id}")
    public ResponseEntity<String> deleteSpecificationTopic(@PathVariable Long id) {

        Optional<SpecificationTopic> topic = specificationTopicRepo.findById(id);
        if (topic.isPresent()) {
            specificationTopicRepo.deleteById(id);
            return ResponseEntity.ok("deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("not found");
    }


    @PostMapping("/topic/detail/create/{topicId}")
    public ResponseEntity<SpecificationTopicDetail> createSpecificationTopicDetail(@PathVariable Long topicId,
                                                                                   @RequestParam String field,
                                                                                   @RequestParam String fieldGeo,
                                                                                   @RequestParam String value,
                                                                                   @RequestParam String valueGeo) {

        Optional<SpecificationTopic> topic = specificationTopicRepo.findById(topicId);
        if (topic.isPresent()) {
            SpecificationTopicDetail detail = specificationTopicDetailRepo.save(SpecificationTopicDetail
                    .builder()
                    .field(field)
                    .fieldGeo(fieldGeo)
                    .value(value)
                    .valueGeo(valueGeo)
                    .build());

            topic.get().getSpecificationTopicDetails().add(detail);
            specificationTopicRepo.save(topic.get());
            return ResponseEntity.ok(detail);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(SpecificationTopicDetail.builder().build());
    }

    @PostMapping("/topic/detail/delete/{id}")
    public ResponseEntity<String> deleteSpecificationTopicDetail(@PathVariable Long id) {

        Optional<SpecificationTopicDetail> detail = specificationTopicDetailRepo.findById(id);

        if (detail.isPresent()) {
            specificationTopicDetailRepo.deleteById(id);
            return ResponseEntity.ok("deleted");
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("not found");
    }


}
