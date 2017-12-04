package ru.javaops.masterjava.persist.dao;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.javaops.masterjava.persist.model.City;
import ru.javaops.masterjava.persist.testdata.CityTestData;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static ru.javaops.masterjava.persist.testdata.CityTestData.CITIES;
import static ru.javaops.masterjava.persist.testdata.CityTestData.NEWCITY;

public class CityDaoTest extends AbstractDaoTest<CityDao> {

    public CityDaoTest() {
        super(CityDao.class);
    }

    @BeforeClass
    public static void init() throws Exception {
        CityTestData.init();
    }

    @Before
    public void setUp() throws Exception {
        CityTestData.setUp();
    }

    @Test
    public void insert() {
        City city = dao.insert(NEWCITY);
        assertTrue(dao.getAll().contains(city));
    }

    @Test
    public void insertBatch() {
        dao.clean();
        int[] insertedCities = dao.insertBatch(CITIES);
        assertTrue(insertedCities.length == CITIES.size());
    }

    @Test
    public void getAll() {
        List<City> cities = dao.getAll();
        assertEquals(CITIES, cities);
    }
}
