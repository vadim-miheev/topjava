package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

public interface MealsRepository {
    Meal get(int id);

    List<Meal> getAll();

    Meal create(Meal meal);

    Meal update(Meal meal);

    void delete(int id);
}
