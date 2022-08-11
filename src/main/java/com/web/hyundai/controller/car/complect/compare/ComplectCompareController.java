package com.web.hyundai.controller.car.complect.compare;

import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.complectcompare.ComplectCompare;
import com.web.hyundai.model.car.complectcompare.ComplectCompareFeature;
import com.web.hyundai.model.car.complectcompare.dto.ComplectCompareDTO;
import com.web.hyundai.model.car.complectcompare.dto.ComplectCompareFeatureDTO;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.modif.ComplectCompareFeatureRepo;
import com.web.hyundai.repo.car.modif.ComplectCompareRepo;
import com.web.hyundai.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
public class ComplectCompareController {

    private static final String LANG_GEO = "ka";
    private static final String LANG_ENG = "en";

    private final CarRepo carRepo;
    private final ComplectCompareRepo complectCompareRepo;
    private final ComplectCompareFeatureRepo featureRepo;
    private final ImageService imageService;


    @GetMapping("/api/complect/compare/{carId}")
    public ResponseEntity<ComplectCompareDTO> getComplectCompare(@PathVariable Long carId,
                                                                 @RequestHeader(
                                                                         value = "accept-language",
                                                                         defaultValue = "en") String lang) {
        Optional<ComplectCompare> single = complectCompareRepo.findById(carId);
        if (single.isEmpty()) return ResponseEntity.notFound().build();
        if (single.get().getComplectCompareFeatureList() == null
                || single.get().getComplectCompareFeatureList().isEmpty()){
            return ResponseEntity.ok(ComplectCompareDTO.from(single.get()));
        }
        single.get().setComplectCompareFeatureList(featureRepo.findFeaturesByComplectIdAndLocale(single.get().getId(),lang));
        return ResponseEntity.ok(ComplectCompareDTO.from(single.get()));
    }

    @GetMapping("/admin/complect/compare/list")
    public ResponseEntity<List<ComplectCompareDTO>> getComplectCompareList() {
        return ResponseEntity.ok(ComplectCompareDTO.from(complectCompareRepo.findAll()));
    }

    @PostMapping("/admin/complect/compare/{carId}")
    @Transactional
    public ResponseEntity<ComplectCompareDTO> createComplectCompare(@PathVariable Long carId,
                                                                 @RequestParam MultipartFile image1,
                                                                 @RequestParam MultipartFile image2) throws IOException {
        Optional<Car> car = carRepo.findById(carId);
        if (car.isEmpty()) return ResponseEntity.notFound().build();

        ComplectCompare saved = complectCompareRepo.save(ComplectCompare.builder()
                .car(car.get())
                .image1(imageService.uploadNewDir(image1, Path.getComplectIntPath()))
                .image2(imageService.uploadNewDir(image2, Path.getComplectIntPath()))
                .build());
        return ResponseEntity.ok(ComplectCompareDTO.from(saved));
    }

    @PutMapping("/admin/complect/compare/{compareId}")
    public ResponseEntity<ComplectCompareDTO> updateComplectCompare(@PathVariable Long compareId,
                                                                 @RequestParam(required = false) MultipartFile image1,
                                                                 @RequestParam(required = false) MultipartFile image2)
            throws IOException {
        Optional<ComplectCompare> compare = complectCompareRepo.findById(compareId);
        if (compare.isEmpty()) return ResponseEntity.notFound().build();

        if (image1 != null && image1.getBytes().length > 0) {
            imageService.deleteFile(compare.get().getImage1());
            compare.get().setImage1(imageService.uploadNewDir(image1, Path.getComplectIntPath()));
        }
        if (image2 != null && image2.getBytes().length > 0) {
            imageService.deleteFile(compare.get().getImage2());
            compare.get().setImage2(imageService.uploadNewDir(image1, Path.getComplectIntPath()));
        }

        ComplectCompare saved = complectCompareRepo.save(compare.get());
        return ResponseEntity.ok(ComplectCompareDTO.from(saved));
    }

    @DeleteMapping("/admin/complect/compare/{compareId}")
    public ResponseEntity<ComplectCompare> deleteComplectCompare(@PathVariable Long compareId) {
        Optional<ComplectCompare> compare = complectCompareRepo.findById(compareId);
        if (compare.isEmpty()) return ResponseEntity.notFound().build();

        String image1 = compare.get().getImage1();
        String image2 = compare.get().getImage2();
        if (image1 != null && image1.getBytes().length > 0) imageService.deleteFile(compare.get().getImage1());
        if (image2 != null && image2.getBytes().length > 0) imageService.deleteFile(compare.get().getImage2());

        complectCompareRepo.deleteById(compareId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/complect/compare/feature/list/{compareId}")
    public ResponseEntity<List<ComplectCompareFeatureDTO>> getComplectCompareFeatureList(@PathVariable Long compareId) {
        Optional<ComplectCompare> compare = complectCompareRepo.findById(compareId);
        if (compare.isEmpty()) return ResponseEntity.notFound().build();
        List<ComplectCompareFeature> list = compare.get().getComplectCompareFeatureList();
        list.sort(Comparator.comparing(ComplectCompareFeature::getOrderId));
        return ResponseEntity.ok(ComplectCompareDTO.fromFeature(list));
    }

    @GetMapping("/admin/complect/compare/feature/get/{featureId}")
    public ResponseEntity<ComplectCompareFeatureDTO> getComplectCompareFeature(@PathVariable Long featureId) {
        Optional<ComplectCompareFeature> feature = featureRepo.findById(featureId);
        if (feature.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ComplectCompareDTO.from(feature.get()));
    }

    @PostMapping("/admin/complect/compare/feature/{compareId}")
    public ResponseEntity<ComplectCompareDTO> createComplectCompareFeature(@PathVariable Long compareId,
                                                                        @RequestParam String title,
                                                                        @RequestParam String titleGEO,
                                                                        @RequestParam String value,
                                                                        @RequestParam String valueGEO,
                                                                        @RequestParam int order) {
        Optional<ComplectCompare> compare = complectCompareRepo.findById(compareId);
        if (compare.isEmpty()) return ResponseEntity.notFound().build();
        ComplectCompareFeature featureEng = ComplectCompareFeature.builder()
                .title(title)
                .value(value)
                .locale(LANG_ENG)
                .orderId(order)
                .build();
        ComplectCompareFeature featureGeo = ComplectCompareFeature.builder()
                .title(titleGEO)
                .value(valueGEO)
                .locale(LANG_GEO)
                .orderId(order)
                .build();
        compare.get().getComplectCompareFeatureList().add(featureEng);
        compare.get().getComplectCompareFeatureList().add(featureGeo);
        System.out.println(compare.get());
        ComplectCompare saved = complectCompareRepo.save(compare.get());
        return ResponseEntity.ok(ComplectCompareDTO.from(saved));
    }

    @DeleteMapping("/admin/complect/compare/feature/{featureId}")
    public ResponseEntity<ComplectCompare> deleteComplectCompareFeature(@PathVariable Long featureId) {
        Optional<ComplectCompareFeature> feature = featureRepo.findById(featureId);
        if (feature.isEmpty()) return ResponseEntity.notFound().build();
        featureRepo.deleteById(featureId);
        return ResponseEntity.ok().build();
    }
}
