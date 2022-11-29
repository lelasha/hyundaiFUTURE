package com.web.hyundai.controller.car.complect.compare;

import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.Engine;
import com.web.hyundai.model.car.complectcompare.ComplectCompare;
import com.web.hyundai.model.car.complectcompare.ComplectCompareFeature;
import com.web.hyundai.model.car.complectcompare.ComplectCompareTitle;
import com.web.hyundai.model.car.complectcompare.dto.ComplectCompareDTO;
import com.web.hyundai.model.car.complectcompare.dto.EngineDTO;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.EngineRepo;
import com.web.hyundai.repo.car.modif.ComplectCompareFeatureRepo;
import com.web.hyundai.repo.car.modif.ComplectCompareRepo;
import com.web.hyundai.repo.car.modif.ComplectCompareTitleRepo;
import com.web.hyundai.service.ImageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("*")
@RequiredArgsConstructor
@Slf4j
public class ComplectCompareController {

    private static final String LANG_GEO = "ka";
    private static final String LANG_ENG = "en";

    private final CarRepo carRepo;
    private final ComplectCompareRepo complectCompareRepo;
    private final ComplectCompareFeatureRepo featureRepo;
    private final ComplectCompareTitleRepo complectCompareTitleRepo;
    private final EngineRepo engineRepo;
    private final ImageService imageService;


    @GetMapping("/api/complect/compare/{carId}")
    public ResponseEntity<ComplectCompareDTO> getComplectCompare(@PathVariable Long carId,
                                                                 @RequestHeader(
                                                                         value = "accept-language",
                                                                         defaultValue = "en") String lang) {
        Optional<ComplectCompare> single = complectCompareRepo.findByCarId(carId);
        Optional<Car> car = carRepo.findById(carId);
        if (single.isEmpty() || car.isEmpty()) return ResponseEntity.notFound().build();
        if (single.get().getComplectCompareTitles() == null
                || single.get().getComplectCompareTitles().isEmpty()) {
            return ResponseEntity.ok(ComplectCompareDTO.from(single.get(), car.get().getFile()));
        }
        List<ComplectCompareTitle> title = complectCompareTitleRepo.findByComplectAndLocale(single.get().getId(), lang);
        log.info("fetching titles {}", title);
        single.get().setComplectCompareTitles(title);

        List<Engine> engines = engineRepo.findAllByCarId(carId);
        ComplectCompareDTO from = ComplectCompareDTO.from(single.get(), car.get().getFile());
        List<EngineDTO> engineDTOS = new ArrayList<>();
        engines.forEach(e -> engineDTOS.add(EngineDTO.builder()
                    .id(e.getId())
                    .image(e.getImage())
                    .price(String.valueOf(e.getPrice()))
                    .name(e.getTitle())
                    .build()));

        from.setEngineDTO(engineDTOS);
        log.info("converted titles {}", from.getCompareTitles());


        return ResponseEntity.ok(from);
    }

    @GetMapping("/admin/complect/compare/{carId}")
    public ResponseEntity<ComplectCompareDTO> getComplectCompare(@PathVariable Long carId) {
        Optional<ComplectCompare> single = complectCompareRepo.findByCarId(carId);
        Optional<Car> car = carRepo.findById(carId);
        if (single.isEmpty() || car.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(ComplectCompareDTO.from(single.get(), car.get().getFile()));
    }

    @GetMapping("/admin/complect/compare/list")
    public ResponseEntity<List<ComplectCompareDTO>> getComplectCompareList() {
        return ResponseEntity.ok(ComplectCompareDTO.from(complectCompareRepo.findAll()));
    }

    @PostMapping("/admin/complect/compare/{carId}")
    public ResponseEntity<ComplectCompareDTO> createComplectCompare(@PathVariable Long carId,
                                                                    @RequestParam MultipartFile image1,
                                                                    @RequestParam MultipartFile image2) throws IOException {
        Optional<Car> car = carRepo.findById(carId);
        if (car.isEmpty()) return ResponseEntity.notFound().build();

        ComplectCompare saved = complectCompareRepo.save(ComplectCompare.builder()
                .car(car.get())
                .image1(imageService.uploadNewDir(image1, Path.getComplectIntPath()))
                .image2(imageService.uploadNewDir(image2, Path.getComplectIntPath()))
                .complectCompareTitles(new ArrayList<>())
                .build());
        return ResponseEntity.ok(ComplectCompareDTO.from(saved, car.get().getFile()));
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
            compare.get().setImage2(imageService.uploadNewDir(image2, Path.getComplectIntPath()));
        }

        ComplectCompare saved = complectCompareRepo.save(compare.get());
        return ResponseEntity.ok(ComplectCompareDTO.from(saved, null));
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

    @GetMapping("/admin/complect/compare/feature/list/{compareTitleId}")
    public ResponseEntity<List<ComplectCompareFeature>> getComplectCompareFeatureList(@PathVariable Long compareTitleId) {
        Optional<ComplectCompareTitle> compare = complectCompareTitleRepo.findById(compareTitleId);
        if (compare.isEmpty()) return ResponseEntity.notFound().build();
        List<ComplectCompareFeature> list = compare.get().getComplectCompareFeatures();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/admin/complect/compare/feature/get/{featureId}")
    public ResponseEntity<ComplectCompareFeature> getComplectCompareFeature(@PathVariable Long featureId) {
        Optional<ComplectCompareFeature> feature = featureRepo.findById(featureId);
        if (feature.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(feature.get());
    }

    @PostMapping("/admin/complect/compare/feature/{compareTitleId}/{complectId}")
    public ResponseEntity<ComplectCompareFeature> createComplectCompareFeature(@PathVariable Long compareTitleId,
                                                                               @PathVariable Long complectId,
                                                                               @RequestParam String value) {
        Optional<ComplectCompareTitle> compareTitle = complectCompareTitleRepo.findById(compareTitleId);
        Optional<Engine> engine = engineRepo.findById(complectId);
        if (compareTitle.isEmpty() || engine.isEmpty()) return ResponseEntity.notFound().build();
        ComplectCompareFeature feature = ComplectCompareFeature.builder()
                .value(value)
                .engine(engine.get())
                .build();
        ComplectCompareFeature savedFeature = featureRepo.save(feature);
        compareTitle.get().getComplectCompareFeatures().add(savedFeature);
        complectCompareTitleRepo.save(compareTitle.get());
        return ResponseEntity.ok(savedFeature);
    }

    @PutMapping("/admin/complect/compare/feature/{featureId}")
    public ResponseEntity<ComplectCompareFeature> updateComplectCompareFeature(@PathVariable Long featureId,
                                                                               @RequestParam String value) {
        Optional<ComplectCompareFeature> feature = featureRepo.findById(featureId);
        if (feature.isEmpty()) return ResponseEntity.notFound().build();
        feature.get().setValue(value);
        ComplectCompareFeature saved = featureRepo.save(feature.get());
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/admin/complect/compare/feature/{featureId}")
    public ResponseEntity<ComplectCompare> deleteComplectCompareFeature(@PathVariable Long featureId) {
        Optional<ComplectCompareFeature> feature = featureRepo.findById(featureId);
        if (feature.isEmpty()) return ResponseEntity.notFound().build();
        featureRepo.deleteById(featureId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/admin/complect/compare/title/{compareId}")
    public ResponseEntity<ComplectCompareDTO> createComplectCompareTitle(@PathVariable Long compareId,
                                                                         @RequestParam String title,
                                                                         @RequestParam String titleGEO,
                                                                         @RequestParam int orderId) {
        Optional<ComplectCompare> compare = complectCompareRepo.findById(compareId);

        if (compare.isEmpty()) return ResponseEntity.notFound().build();
        ComplectCompareTitle titleEng = ComplectCompareTitle.builder()
                .title(title)
                .orderId(orderId)
                .locale(LANG_ENG)
                .complectCompareFeatures(new ArrayList<>())
                .build();
        ComplectCompareTitle titleGeo = ComplectCompareTitle.builder()
                .title(titleGEO)
                .orderId(orderId)
                .locale(LANG_GEO)
                .complectCompareFeatures(new ArrayList<>())
                .build();
        compare.get().getComplectCompareTitles().add(titleEng);
        compare.get().getComplectCompareTitles().add(titleGeo);
        ComplectCompare saved = complectCompareRepo.save(compare.get());
        return ResponseEntity.ok(ComplectCompareDTO.from(saved, null));
    }

    @PutMapping("/admin/complect/compare/title/{compareTitleId}")
    public ResponseEntity<ComplectCompareTitle> updateComplectCompareTitle(@PathVariable Long compareTitleId,
                                                                           @RequestParam String title,
                                                                           @RequestParam int orderId) {
        Optional<ComplectCompareTitle> compare = complectCompareTitleRepo.findById(compareTitleId);
        if (compare.isEmpty()) return ResponseEntity.notFound().build();
        compare.get().setOrderId(orderId);
        compare.get().setTitle(title);
        ComplectCompareTitle saved = complectCompareTitleRepo.save(compare.get());
        return ResponseEntity.ok(saved);
    }

    @DeleteMapping("/admin/complect/compare/title/{compareTitleId}")
    public ResponseEntity<Object> updateComplectCompareTitle(@PathVariable Long compareTitleId) {
        Optional<ComplectCompareTitle> compare = complectCompareTitleRepo.findById(compareTitleId);
        if (compare.isEmpty()) return ResponseEntity.notFound().build();
        complectCompareTitleRepo.deleteById(compareTitleId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/admin/complect/compare/title/{compareTitleId}")
    public ResponseEntity<ComplectCompareTitle> getComplectCompareTitle(@PathVariable Long compareTitleId) {
        Optional<ComplectCompareTitle> compare = complectCompareTitleRepo.findById(compareTitleId);
        if (compare.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(compare.get());
    }

    @GetMapping("/admin/complect/compare/title/list/{compareId}")
    public ResponseEntity<List<ComplectCompareTitle>> getComplectCompareTitleList(@PathVariable Long compareId) {
        Optional<ComplectCompare> compare = complectCompareRepo.findById(compareId);
        if (compare.isEmpty()) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(compare.get().getComplectCompareTitles());
    }
}
