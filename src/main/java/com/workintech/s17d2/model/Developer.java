package com.workintech.s17d2.model;

import java.util.Objects;

public class Developer {
    private Integer id;
    private String name;
    private Double salary;
    private Experience experience;

    public Developer(Integer id, String name, Double salary, Experience experience){
        this.id=id;
        this.name=name;
        this.salary=salary;
        this.experience=experience;
    }

    public Integer getId(){
        return id;
    }
    public void setId(Integer id){
        this.id=id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public Double getSalary(){
        return salary;
    }
    public void setSalary(Double  salary){
        this.salary = salary;
    }

    public Experience getExperience() {
        return experience;
    }
    public void setExperience(Experience experience){
        this.experience=experience;
    }

    @Override
    public String toString(){
        return "Developer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", salary=" + salary +
                ", experience="+ experience +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Developer developer = (Developer) o;
        return Objects.equals(id,developer.id);
    }
    @Override
    public int hashCode(){
        return Objects.hash(id);
    }



}

