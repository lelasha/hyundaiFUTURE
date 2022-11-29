package com.web.hyundai.controller.car;

import com.web.hyundai.model.car.Calculator;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;
import java.util.HashMap;
// test commit
@RestController
@CrossOrigin("*")
public class CalculatorController {



    @GetMapping("/api/car/calculator/first")
    public HashMap<String, String> calculateCost(
            @RequestParam("fuelConsumption")
            @Digits(integer = 2,fraction = 0,message = "მაქსიმალური ციფრების რაოდენობა 2 ფორმატი განაყოფით 0") Integer fuelConsumption,
            @RequestParam("fuelCost")
            @Digits(integer = 2, fraction = 2, message = "მაქსიმალური ციფრების რაოდენობა 2 ფორმატი განაყოფით 2") double fuelCost
                                ){

        double fuelCostAt150KM = (double)(150000 / 100) * fuelCost * fuelConsumption;


        HashMap<String,String> map = new HashMap<>();
        map.put("fuelCost",String.format("%.0f",fuelCostAt150KM));


        return map;


    }


    @GetMapping("/api/car/calculator/second")
    public Calculator calculateCost(
            @RequestParam("watCost")
            @Digits(integer = 2, fraction = 2, message = "მაქსიმალური ციფრების რაოდენობა 2 ფორმატი განაყოფით 2") double watCost
    ){
        BigDecimal costPerCharge = BigDecimal.valueOf(62).multiply(BigDecimal.valueOf(watCost));
        BigDecimal doubleChargeCost150 =
                BigDecimal.valueOf(150000)
                .divide(BigDecimal.valueOf(480))
                .multiply(costPerCharge);



        Calculator calculator = new Calculator();
        calculator.setCostPerCharge(String.format("%.2f",costPerCharge));
        calculator.setChargeCost150(String.format("%.2f",doubleChargeCost150));
        return calculator;


    }
}
