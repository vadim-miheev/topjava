package ru.javawebinar.topjava.web.converter;

import com.fasterxml.jackson.databind.util.StdConverter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static ru.javawebinar.topjava.util.MealsUtil.MEAL_DATE_TIME_UI_PATTERN;

public class LocalDateTimeToStringConverter extends StdConverter<LocalDateTime, String> {
    static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(MEAL_DATE_TIME_UI_PATTERN);

    @Override
    public String convert(LocalDateTime value) {
        return value.format(DATE_FORMATTER);
    }
}
