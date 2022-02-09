package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealsRepository {
    List<Meal> getAll();

    Meal create(LocalDateTime date, String description, int calories);

    Meal update(Meal meal);

    void delete(int id);
}
