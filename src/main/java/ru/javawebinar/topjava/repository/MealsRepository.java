package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.util.List;

public interface MealsRepository {
    List<Meal> getMeals();

    void createMeal(Meal meal);

    void updateMeal(Meal meal);

    void deleteMeal(int id);
}
