package com.github.matkovd.carbookingservice.dao;

import com.github.matkovd.carbookingservice.model.Car;

import java.util.List;

public interface CarDao {
   List<Car> getAll();
   Long create(Car car);
   Car getById(Long id);
}
