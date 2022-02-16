package ru.javawebinar.topjava.service;

import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealService {

    private final MealRepository repository;

    public MealService(MealRepository mealRepository) {
        this.repository = mealRepository;
    }

    public List<MealTo> getAll(int authUserId, int authUserCaloriesPerDay) {
        return MealsUtil.getTos(repository.getAll(authUserId), authUserCaloriesPerDay);
    }

    public List<MealTo> getAllFiltered(int authUserId, int authUserCaloriesPerDay, LocalDate startDate, LocalDate endDate,
                               LocalTime startTime, LocalTime endTime) {
        return MealsUtil.getFilteredTos(repository.getAll(authUserId, startDate, endDate), authUserCaloriesPerDay,
                startTime, endTime);
    }

    public Meal get(int id, int userId) {
        return checkNotFoundWithId(repository.get(id, userId), id);
    }

    public Meal create(Meal meal, int userId) {
        return repository.save(meal, userId);
    }

    public void delete(int id, int userId) {
        checkNotFoundWithId(repository.delete(id, userId), id);
    }

    public void update(Meal meal, int userId) {
        checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }
}