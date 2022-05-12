package ru.javawebinar.topjava.web.meal;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealToUI;
import ru.javawebinar.topjava.util.ValidationUtil;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = "/profile/meals", produces = MediaType.APPLICATION_JSON_VALUE)
public class MealUIController extends AbstractMealController {

    @GetMapping
    public List<MealToUI> getAllUI() {
        return super.getAll().stream().map(MealToUI::getFromMealTo).toList();
    }

    @GetMapping("/{id}")
    public MealToUI getUI(@PathVariable int id) {
        return MealToUI.getFromMeal(super.get(id));
    }

    @Override
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        super.delete(id);
    }

    @PostMapping
    public ResponseEntity<String> createOrUpdate(@Valid Meal meal, BindingResult result) {
        if (result.hasErrors()) {
            return ValidationUtil.prepareBindingErrors(result);
        }
        if (meal.isNew()) {
            super.create(meal);
        } else {
            super.update(meal, meal.id());
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/filter")
    public List<MealToUI> getBetweenUI(
            @RequestParam @Nullable LocalDate startDate,
            @RequestParam @Nullable LocalTime startTime,
            @RequestParam @Nullable LocalDate endDate,
            @RequestParam @Nullable LocalTime endTime) {
        return super.getBetween(startDate, startTime, endDate, endTime).stream().map(MealToUI::getFromMealTo).toList();
    }
}