package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {
    public static final int NOT_FOUND = 10;
    public static final LocalDate TWENTY_CENTURY = LocalDate.ofYearDay(2000, 1);
    public static final LocalDate THIRTY_CENTURY = LocalDate.ofYearDay(3000, 1);
    public static final LocalDate EXISTING_DATE_FIRST = LocalDate.of(2020, 1, 30);
    public static final LocalDate EXISTING_DATE_SECOND = LocalDate.of(2020, 1, 31);

    public static final List<Meal> userMeals = Arrays.asList(
            new Meal(START_SEQ + 3, LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "01 Завтрак", 500),
            new Meal(START_SEQ + 4, LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "02 Обед", 1000),
            new Meal(START_SEQ + 5, LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "03 Ужин", 500),
            new Meal(START_SEQ + 6, LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "04 Еда на граничное значение", 100),
            new Meal(START_SEQ + 7, LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "05 Завтрак", 1000),
            new Meal(START_SEQ + 8, LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "06 Обед", 500),
            new Meal(START_SEQ + 9, LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "07 Ужин", 410)
    );

    public static final List<Meal> adminMeals = Arrays.asList(
            new Meal(START_SEQ + 10, LocalDateTime.of(2020, Month.JANUARY, 31, 14, 0), "08 Admin", 510),
            new Meal(START_SEQ + 11, LocalDateTime.of(2020, Month.JANUARY, 31, 21, 0), "09 Admin", 1500)
    );

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, Month.JANUARY, 29, 20, 0), "10 Ужин", 420);
    }

    public static Meal getUpdated(Meal meal) {
        Meal updated = new Meal(meal);
        updated.setDateTime(LocalDateTime.of(2021, 3, 27, 20, 0));
        updated.setDescription("UpdatedDescription");
        updated.setCalories(330);
        return updated;
    }

    public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).isEqualTo(expected);
    }
}
