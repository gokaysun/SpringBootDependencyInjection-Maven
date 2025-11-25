package com.workintech.s17d2.rest;


import com.workintech.s17d2.model.*;
import com.workintech.s17d2.tax.DeveloperTax;
import com.workintech.s17d2.tax.Taxable;
import jakarta.annotation.PostConstruct;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/developers")
public class DeveloperController {

    private Taxable taxable;
    public Map<Integer, Developer> developers;

    public DeveloperController(Taxable taxable) {
        this.taxable= taxable;
    }

    @PostConstruct
    public void init(){
        developers = new HashMap<>();

        developers.put(1,new JuniorDeveloper(1,"Ahmet",applyTax(5000d, Experience.JUNIOR)));
        developers.put(2,new MidDeveloper(2,"Ayşe",applyTax(10000d,Experience.MID)));
        developers.put(3,new SeniorDeveloper(3,"Mehmet",applyTax(20000d, Experience.SENIOR)));

    }

    private Double applyTax(Double salary,Experience exp){
        double rate;
        switch (exp) {
            case JUNIOR : rate = taxable.getSimpleTaxRate(); break;
            case MID : rate = taxable.getMiddleTaxRate();break;
            case SENIOR: rate= taxable.getUpperTaxRate();break;
            default: rate = 0d;
        }
        return salary - (salary *rate /100d);
    }

    @GetMapping
    public List<Developer> getAll(){
        return new ArrayList<>(developers.values());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Developer>getById(@PathVariable Integer id){
        Developer d = developers.get(id);
        if(d== null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(d);
    }
    //DTO for requestt
    public static class DeveloperRequest{
        public Integer id;
        public String name;
        public Double salary;
        public String experience; //junior,mid,senior
    }
    @PostMapping
    public ResponseEntity<Developer> create(@RequestBody DeveloperRequest req) {
        if (req == null || req.id == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Experience exp;
        try {
            exp = Experience.valueOf(req.experience);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Double taxedSalary = applyTax(req.salary, exp);
        Developer created;
        switch (exp) {
            case JUNIOR:
                created = new JuniorDeveloper(req.id, req.name, taxedSalary);
                break;
            case MID:
                created = new MidDeveloper(req.id, req.name, taxedSalary);
                break;
            case SENIOR:
                created = new SeniorDeveloper(req.id, req.name, taxedSalary);
                break;
            default:
                created = new Developer(req.id, req.name, taxedSalary, exp);
        }
        developers.put(req.id, created);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Developer> update(@PathVariable Integer id,@RequestBody DeveloperRequest req) {
        Developer existing = developers.get(id);
        if (existing == null) return ResponseEntity.notFound().build();

        Experience exp;
        try {
            exp = Experience.valueOf(req.experience);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        existing.setName(req.name);
        existing.setSalary(req.salary);
        existing.setExperience(exp);

        developers.put(id,existing);
        return  ResponseEntity.ok(existing);

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        Developer removed = developers.remove(id);
        if (removed == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }

}
