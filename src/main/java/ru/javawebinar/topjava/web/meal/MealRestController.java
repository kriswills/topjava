package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import static ru.javawebinar.topjava.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.topjava.util.ValidationUtil.checkNew;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final MealService service;

    public MealRestController(MealService service) {
        this.service = service;
    }

    public Meal get(int id) {
        log.info("get {}", id);
        int userId = SecurityUtil.authUserId();
        return service.get(id, userId);
    }

    public List<MealTo> getAll() {
        log.info("getAll");
        int userId = SecurityUtil.authUserId();
        return MealsUtil.getTos(service.getAll(userId), SecurityUtil.authUserCaloriesPerDay());
//        return service.getAll(userId);
    }

    public void delete(int id) {
        log.info("delete {}", id);
        int userId = SecurityUtil.authUserId();
        service.delete(id, userId);
    }

//    public Meal save (Meal meal, int id) {
//        log.info("create {}", id);
//        return service.save(meal, id);
//    }

    public Meal create(Meal meal) {

        int userId = SecurityUtil.authUserId();
        log.info("create {}", userId);
        checkNew(meal);
        return service.create(meal, userId);
    }

    public void update(Meal meal, int id) {
        log.info("update {}", id);
        int userId = SecurityUtil.authUserId();
        assureIdConsistent(meal, userId);
        service.update(meal, userId);
    }

    public List<MealTo> getBetweenHalfOpen(@Nullable LocalDate startDate, @Nullable LocalTime startTime, @Nullable LocalDate endDate, @Nullable LocalTime endTime) {
        int userId = SecurityUtil.authUserId();
        log.info("getBetween dates({} - {}) time({} - {}) for user {}", startDate, startTime, endDate, endTime, userId);

        List<Meal> mealsDateFiltered = service.getBetweenHalfOpen(startDate, endDate, userId);
        return MealsUtil.getFilteredTos(mealsDateFiltered, SecurityUtil.authUserCaloriesPerDay(), startTime, endTime);
    }
}
