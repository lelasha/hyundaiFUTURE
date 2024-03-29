package com.web.hyundai.service.car;


import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.Car;
import com.web.hyundai.model.car.Engine;
import com.web.hyundai.model.car.EngineDesc;
import com.web.hyundai.model.car.FuelUsage;
import com.web.hyundai.model.car.modif.CarComplect;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.EngineDescRepo;
import com.web.hyundai.repo.car.EngineRepo;
import com.web.hyundai.repo.car.modif.CarComplectRepo;
import com.web.hyundai.repo.car.modif.ComplectCompareFeatureRepo;
import com.web.hyundai.repo.car.modif.ComplectCompareRepo;
import com.web.hyundai.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class EngineService {

    @Autowired
    EngineRepo engineRepo;

    @Autowired
    EngineDescRepo engineDescRepo;

    @Autowired
    CarRepo carRepo;

    @Autowired
    ImageService imageService;

    @Autowired
    CarComplectRepo carComplectRepo;

    @Autowired
    ComplectCompareFeatureRepo complectCompareFeatureRepo;

    public Engine createEngine(Long carid, String title, int price, int hp,
                               String city, String outCity, String hundred, String combined, MultipartFile image) throws IOException {


        Optional<Car> car = carRepo.findById(carid);
        if (car.isPresent()) {

            Engine engine = new Engine();
            engine.setTitle(title);
            engine.setCar(car.get());
            engine.setFuelUsage(new FuelUsage(city, outCity, hundred, combined));
            engine.setHp(hp);
            engine.setPrice(price);
            //engine.setTitleGEO(titleGEO);

            if (image.getBytes().length > 0) {
                FileUpload fileUpload = imageService.uploadImage(image, Path.CAR_PATH_FILES);
                engine.setImage(imageService.removeDoubleSlash(
                        Path.CAR_PATH_FILES + fileUpload.getFile().getName()));
            }

            return engineRepo.save(engine);
        }
        return new Engine();


    }

    public Engine updateEngine(Long engineId, String title, int hp, int price,
                               String city, String outCity, String hundred,
                               String combined,MultipartFile image) throws IOException {


        Optional<Engine> engine = engineRepo.findById(engineId);

        if (engine.isPresent()) {
            engine.get().getFuelUsage().setCity(city);
            engine.get().getFuelUsage().setHundred(hundred);
            engine.get().getFuelUsage().setOutCity(outCity);
            engine.get().getFuelUsage().setCombined(combined);
            engine.get().setTitle(title);
            engine.get().setHp(hp);
            engine.get().setPrice(price);

            if (null != image && image.getBytes().length > 0) {
                FileUpload fileUpload = imageService.uploadImage(image, Path.CAR_PATH_FILES);
                engine.get().setImage(imageService.removeDoubleSlash(
                        Path.CAR_PATH_FILES + fileUpload.getFile().getName()));
            }

            return engineRepo.save(engine.get());

        }
        return new Engine();

    }

    @Transactional
    public String deleteEngine(Long engineId) {
        Optional<Engine> engine = engineRepo.findById(engineId);
        if (engine.isPresent()) {
            List<EngineDesc> desc = engineDescRepo.findAllByEngineId(engineId);
            engineDescRepo.deleteAll(desc);

            carComplectRepo.findByEngineId(engineId).ifPresent(carComplect -> carComplectRepo.delete(carComplect));


            complectCompareFeatureRepo.deleteByEngineId(engineId);

            engineRepo.deleteById(engineId);

            return "1";
        }
        return "0";

    }
}
