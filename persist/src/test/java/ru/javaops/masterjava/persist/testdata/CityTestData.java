package ru.javaops.masterjava.persist.testdata;

import com.google.common.collect.ImmutableList;
import ru.javaops.masterjava.persist.DBIProvider;
import ru.javaops.masterjava.persist.dao.CityDao;
import ru.javaops.masterjava.persist.model.City;

import java.util.List;

public class CityTestData {
    public static City SPB;
    public static City MOW;
    public static City KIV;
    public static City MNSK;
    public static List<City> CITIES;
    public static City NEWCITY;

    public static void init() {
        SPB = new City("Санкт-Петербург", "spb");
        MOW = new City("Москва", "mow");
        KIV = new City("Киев", "kiv");
        MNSK = new City("Минск", "mnsk");
        NEWCITY = new City("Ньюситинск", "nwct");
        CITIES = ImmutableList.of(KIV, MNSK, MOW, SPB);
    }

    public static void setUp() {
        CityDao dao = DBIProvider.getDao(CityDao.class);
        dao.clean();
        DBIProvider.getDBI().useTransaction((conn, status) -> {
            CITIES.forEach(dao::insert);
        });
    }
}
