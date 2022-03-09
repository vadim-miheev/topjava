package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {
    Meal findMealByIdAndUser_Id(int id, int userId);

    List<Meal> findAllByUser_IdOrderByDateTimeDesc(int userId);

    @Modifying
    @Query("SELECT m FROM Meal m WHERE m.dateTime>=:sdt AND m.dateTime<:edt AND m.user.id=:user_id ORDER BY m.dateTime DESC")
    List<Meal> getBetweenHalfOpen(@Param("sdt") LocalDateTime startDateTime, @Param("edt") LocalDateTime endDateTime,
                                  @Param("user_id") int userId);

    @Transactional
    @Modifying
    @Query("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:user_id")
    int delete(@Param("id") int id, @Param("user_id") int userId);
}
