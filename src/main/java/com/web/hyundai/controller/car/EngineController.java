package com.web.hyundai.controller.car;


import com.web.hyundai.model.FileUpload;
import com.web.hyundai.model.Path;
import com.web.hyundai.model.car.*;
import com.web.hyundai.model.car.modif.CarComplect;
import com.web.hyundai.model.car.web.EngineDescWeb;
import com.web.hyundai.model.car.web.EngineWrep;
import com.web.hyundai.repo.car.CarRepo;
import com.web.hyundai.repo.car.EngineDescIconRepo;
import com.web.hyundai.repo.car.EngineDescRepo;
import com.web.hyundai.repo.car.EngineRepo;
import com.web.hyundai.repo.car.modif.CarComplectRepo;
import com.web.hyundai.service.ImageService;
import com.web.hyundai.service.car.CarBuildService;
import com.web.hyundai.service.car.EngineService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@Api(tags = "Car-Engine")
@CrossOrigin("*")

public class EngineController {


    @Autowired
    CarRepo carRepo;

    @Autowired
    EngineService engineService;

    @Autowired
    EngineRepo engineRepo;

    @Autowired
    EngineDescRepo engineDescRepo;

    @Autowired
    CarBuildService carBuildService;

    @Autowired
    CarComplectRepo carComplectRepo;

    @Autowired
    ImageService imageService;

    @Autowired
    EngineDescIconRepo engineDescIconRepo;


    @GetMapping(path = "/admin/car-engine/findall/{carid}", produces = {"application/json;**charset=UTF-8**"})
    public ResponseEntity<?> findallEngine(@RequestHeader("accept-language") String lang, @PathVariable Long carid) {

        Optional<Car> car = carRepo.findById(carid);
        if (car.isPresent()) {
            List<EngineWrep> eng = carBuildService.engineBuild(lang, car.get(), "engine");
            if (eng.size() > 0) return ResponseEntity.ok(eng);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი მანქანა ვერ მოიძებნა ან ამ მანქანაზე მიმაგრებული ძრავი");
    }


    @PostMapping(path = "/admin/car-engine/create/{carid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<Engine> createEngine
            (
                    @PathVariable Long carid,
                    @RequestParam("title") String title,
                    @RequestParam(value = "hp", required = false, defaultValue = "0") @ApiParam(value = "ცხენის ძალა ინტიჯერში", required = true) int hp,
                    @RequestParam("price") int price,
                    @RequestParam("city") @ApiParam(value = "წვა ქალაქში", required = true) String city,
                    @RequestParam("outCity") @ApiParam(value = "წვა ქალაქ გარეთ", required = true) String outCity,
                    @RequestParam("hundred") @ApiParam(value = "წვა 100კმ ზე", required = true) String hundred,
                    @RequestParam("combined") @ApiParam(value = "შერეული წვა", required = true) String combined,
                    @RequestParam("image") MultipartFile image
            ) throws IOException {


        Engine engine = engineService.createEngine(carid, title, price, hp, city, outCity, hundred, combined, image);

        if (engine.getId() != null) return ResponseEntity.ok(engine);


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new Engine());
    }


    @PostMapping(path = "/admin/car-engine/update/{engineid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<EngineWrep> updateEngine(
            @PathVariable Long engineid,
            @RequestParam("title") String title,
            @RequestParam("hp") @ApiParam(value = "ცხენის ძალა ინტიჯერში", required = true) int hp,
            @RequestParam("price") int price,
            @RequestParam("city") @ApiParam(value = "წვა ქალაქში", required = true) String city,
            @RequestParam("outCity") @ApiParam(value = "წვა ქალაქ გარეთ", required = true) String outCity,
            @RequestParam("hundred") @ApiParam(value = "წვა 100კმ ზე", required = true) String hundred,
            @RequestParam("combined") @ApiParam(value = "შერეული წვა", required = true) String combined,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestHeader("accept-language") String lang
    ) throws IOException {


        Engine engine = engineService.updateEngine(engineid, title, hp, price, city, outCity, hundred, combined, image);

        EngineWrep engineWrep = new EngineWrep();
        engineWrep.setCarLogo(engine.getCar().getLogo());
        engineWrep.setEngineId(engine.getId());
        engineWrep.setEngineTitle(engine.getTitle());
        engineWrep.setCity(engine.getFuelUsage().getCity());
        engineWrep.setOutCity(engine.getFuelUsage().getOutCity());
        engineWrep.setHundred(engine.getFuelUsage().getHundred());
        engineWrep.setCombined((engine.getFuelUsage().getCombined()));
        engineWrep.setHp(engine.getHp());
        engineWrep.setPrice(engine.getPrice());
        engineWrep.setImage(engine.getImage());
        Optional<CarComplect> cmp = carComplectRepo.findByEngineId(engine.getId());
        if (cmp.isPresent()) {
            engineWrep.setComplectId(cmp.get().getId());
            engineWrep.setComplectName(cmp.get().getName());
            if (lang.equals("ka")) engineWrep.setComplectName(cmp.get().getNameGEO());
        }

        List<EngineDescWeb> engineDescWebs = new ArrayList<>();

        List<EngineDesc> descs = engineDescRepo.findAllByEngineId(engine.getId());
        descs.forEach(engineDesc -> {
            EngineDescWeb engineDescWeb = new EngineDescWeb();

            engineDescWeb.setId(engineDesc.getId());
            engineDescWeb.setName(engineDesc.getName());
            if (null!= engineDesc.getEngineDescIcon()) {
                engineDescWeb.setEngineIcon(engineDesc.getEngineDescIcon().getName());
            }
            engineDescWeb.setEngineDescLogo(engineDesc.getEngineDescLogo().toString());
            if (lang.equals("ka")) engineDescWeb.setName(engineDesc.getNameGEO());
            engineDescWebs.add(engineDescWeb);

        });
        engineWrep.setEngineDesc(engineDescWebs);


        if (engine.getId() != null) return ResponseEntity.ok(engineWrep);


        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new EngineWrep());
    }


    @PostMapping(path = "/admin/car-engine/delete/{engineid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> deleteEngine(@PathVariable Long engineid) {

        if (engineService.deleteEngine(engineid).equals("1")) {
            return ResponseEntity.ok("წარმატებით წაიშალა");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი ძრავი ვერ მოიძებნა");


    }

    @GetMapping(path = "/admin/car-engine/desc/findall/{engineid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<List<EngineDesc>> findAllEngineDesc(@PathVariable Long engineid) {
        List<EngineDesc> engineDesc = engineDescRepo.findAllByEngineId(engineid);
        return ResponseEntity.ok(engineDesc);
    }


    @PostMapping(path = "/admin/car-engine/desc/create/{engineid}/{iconId}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> createEngineDesc(@PathVariable Long engineid,
                                              @PathVariable Long iconId,
                                              @RequestParam String name,
                                              @RequestParam String nameGEO){
        Optional<Engine> engine = engineRepo.findById(engineid);
        Optional<EngineDescIcon> icon = engineDescIconRepo.findById(iconId);
        if (engine.isPresent() && icon.isPresent()) {
            EngineDesc engineDesc = new EngineDesc(name, nameGEO, engine.get());
            engineDesc.setEngineDescIcon(icon.get());
            engineDesc.setEngineDescLogo(EngineDescLogo.ENGINE); //todo just for old data not to get nullpointer
            engineDescRepo.save(engineDesc);
            return ResponseEntity.ok(engineDesc);

        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი ძრავი ვერ მოიძებნა");

    }

    @PostMapping(path = "/admin/car-engine/desc/update/{descid}/{iconId}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> updateEngineDesc(@PathVariable Long descid,
                                              @RequestParam String name,
                                              @RequestParam String nameGEO,
                                              @PathVariable Long iconId) {

        Optional<EngineDesc> desc = engineDescRepo.findById(descid);
        Optional<EngineDescIcon> icon = engineDescIconRepo.findById(iconId);
        if (desc.isPresent() && icon.isPresent()) {
            desc.get().setName(name);
            desc.get().setNameGEO(nameGEO);
            desc.get().setEngineDescIcon(icon.get());
            engineDescRepo.save(desc.get());
            return ResponseEntity.ok(desc.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი ძრავის დახასიათება ვერ მოიძებნა");

    }

    @PostMapping(path = "/admin/car-engine/desc/delete/{descid}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<String> deleteEngineDesc(@PathVariable Long descid) {

        Optional<EngineDesc> desc = engineDescRepo.findById(descid);
        if (desc.isPresent()) {
            engineDescRepo.deleteById(descid);

            return ResponseEntity.ok("1");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ასეთი ძრავის დახასიათება ვერ მოიძებნა");

    }

    @GetMapping(path = "/admin/car-engine/desc/icon/get", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<List<EngineDescIcon>> getEngineDescIcons() {
        return ResponseEntity.ok(engineDescIconRepo.findAll());
    }

    @PostMapping(path = "/admin/car-engine/desc/icon/create", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<EngineDescIcon> createEngineDescIcon(@RequestParam MultipartFile icon) throws IOException {

        if (icon.getBytes().length > 0) {
            EngineDescIcon engineDescIcon = new EngineDescIcon();
            FileUpload fileUpload = imageService.uploadImage(icon, Path.CAR_PATH_FILES);
            engineDescIcon.setName(imageService.removeDoubleSlash(
                    Path.CAR_PATH_FILES + fileUpload.getFile().getName()));

            return ResponseEntity.ok(engineDescIconRepo.save(engineDescIcon));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EngineDescIcon());

    }

    @PostMapping(path = "/admin/car-engine/desc/icon/update/{iconId}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> updateEngineDescIcon(@PathVariable Long iconId,
                                                  @RequestParam MultipartFile icon) throws IOException {
        Optional<EngineDescIcon> iconObj = engineDescIconRepo.findById(iconId);
        if (iconObj.isPresent() && icon.getBytes().length > 0) {
            FileUpload fileUpload = imageService.uploadImage(icon, Path.CAR_PATH_FILES);
            iconObj.get().setName(imageService.removeDoubleSlash(
                    Path.CAR_PATH_FILES + fileUpload.getFile().getName()));

            return ResponseEntity.ok(engineDescIconRepo.save(iconObj.get()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new EngineDescIcon());

    }

    @PostMapping(path = "/admin/car-engine/desc/icon/delete/{iconId}", produces = "application/json;**charset=UTF-8**")
    public ResponseEntity<?> deleteEngineDescIcon(@PathVariable Long iconId) {
        Optional<EngineDescIcon> iconObj = engineDescIconRepo.findById(iconId);
        if (iconObj.isPresent()) {
            iconObj.get().getEngineDesc().forEach(engineDesc -> {
                engineDesc.setEngineDescIcon(null);
            });
            engineDescIconRepo.delete(iconObj.get());
            File oldFile = new File(Path.folderPath() + iconObj.get().getName());
            if (oldFile.exists()) oldFile.delete();
            return ResponseEntity.ok("deleted");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not found");

    }


}



