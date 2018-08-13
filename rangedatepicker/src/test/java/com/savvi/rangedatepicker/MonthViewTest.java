package com.savvi.rangedatepicker;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class MonthViewTest {
    @Test
    public void test1() throws Exception {
        SimpleDateFormat f  = new SimpleDateFormat("yyyy-MM-dd");
//        MonthView monthView = new MonthView(null, null);
        int dayOfWeek = 0;
        Date activeMin = f.parse("2018-09-01");
        Date activeMax = null;
        boolean isHolidaySelectable = false;
        Date cellDate = f.parse("2018-08-31");;
        boolean isHoliday = true;
        List<Integer> deactivatedDates = null;

        boolean result =
                MonthView.isDeactive(dayOfWeek, activeMin, activeMax, isHolidaySelectable, cellDate, isHoliday, deactivatedDates);
        assertEquals(true, result);
    }

    @Test
    public void test2() throws Exception {
        SimpleDateFormat f  = new SimpleDateFormat("yyyy-MM-dd");
//        MonthView monthView = new MonthView(null, null);
        int dayOfWeek = 0;
        Date activeMin = f.parse("2018-09-01");
        Date activeMax = f.parse("2018-09-01");
        boolean isHolidaySelectable = false;
        Date cellDate = f.parse("2018-09-02");;
        boolean isHoliday = true;
        List<Integer> deactivatedDates = null;

        boolean result =
                MonthView.isDeactive(dayOfWeek, activeMin, activeMax, isHolidaySelectable, cellDate, isHoliday, deactivatedDates);
        assertEquals(true, result);
    }

    @Test
    public void test3() throws Exception {
        SimpleDateFormat f  = new SimpleDateFormat("yyyy-MM-dd");
//        MonthView monthView = new MonthView(null, null);
        int dayOfWeek = 0;
        Date activeMin = null;
        Date activeMax = null;
        boolean isHolidaySelectable = false;
        Date cellDate = f.parse("2018-09-02");;
        boolean isHoliday = true;
        List<Integer> deactivatedDates = new ArrayList<>();
        deactivatedDates.add(0);

        boolean result =
                MonthView.isDeactive(dayOfWeek, activeMin, activeMax, isHolidaySelectable, cellDate, isHoliday, deactivatedDates);
        assertEquals(true, result);
    }


    @Test
    public void test4() throws Exception {
        SimpleDateFormat f  = new SimpleDateFormat("yyyy-MM-dd");
//        MonthView monthView = new MonthView(null, null);
        int dayOfWeek = 0;
        Date activeMin = f.parse("2018-09-01");
        Date activeMax = f.parse("2018-09-03");
        boolean isHolidaySelectable = false;
        Date cellDate = f.parse("2018-09-02");;
        boolean isHoliday = true;
        List<Integer> deactivatedDates = null;

        boolean result =
                MonthView.isDeactive(dayOfWeek, activeMin, activeMax, isHolidaySelectable, cellDate, isHoliday, deactivatedDates);
        assertEquals(true, result);
    }

    @Test
    public void test5() throws Exception {
        SimpleDateFormat f  = new SimpleDateFormat("yyyy-MM-dd");
//        MonthView monthView = new MonthView(null, null);
        int dayOfWeek = 0;
        Date activeMin = f.parse("2018-09-01");
        Date activeMax = f.parse("2018-09-03");
        boolean isHolidaySelectable = false;
        Date cellDate = f.parse("2018-09-02");;
        boolean isHoliday = false;
        List<Integer> deactivatedDates = null;

        boolean result =
                MonthView.isDeactive(dayOfWeek, activeMin, activeMax, isHolidaySelectable, cellDate, isHoliday, deactivatedDates);
        assertEquals(false, result);
    }
    @Test
    public void test6() throws Exception {
        SimpleDateFormat f  = new SimpleDateFormat("yyyy-MM-dd");
//        MonthView monthView = new MonthView(null, null);
        int dayOfWeek = 0;
        Date activeMin = f.parse("2018-09-01");
        Date activeMax = f.parse("2018-09-03");
        boolean isHolidaySelectable = true;
        Date cellDate = f.parse("2018-09-02");;
        boolean isHoliday = true;
        List<Integer> deactivatedDates = null;

        boolean result =
                MonthView.isDeactive(dayOfWeek, activeMin, activeMax, isHolidaySelectable, cellDate, isHoliday, deactivatedDates);
        assertEquals(false, result);
    }
    @Test
    public void test7() throws Exception {
        SimpleDateFormat f  = new SimpleDateFormat("yyyy-MM-dd");
//        MonthView monthView = new MonthView(null, null);
        int dayOfWeek = 0;
        Date activeMin = f.parse("2018-09-01");
        Date activeMax = f.parse("2018-09-03");
        boolean isHolidaySelectable = true;
        Date cellDate = f.parse("2018-09-02");;
        boolean isHoliday = true;
        List<Integer> deactivatedDates = new ArrayList<>();
        deactivatedDates.add(0);

        boolean result =
                MonthView.isDeactive(dayOfWeek, activeMin, activeMax, isHolidaySelectable, cellDate, isHoliday, deactivatedDates);
        assertEquals(false, result);
    }
    @Test
    public void test8() throws Exception {
        SimpleDateFormat f  = new SimpleDateFormat("yyyy-MM-dd");
//        MonthView monthView = new MonthView(null, null);
        int dayOfWeek = 0;
        Date activeMin = f.parse("2018-09-01");
        Date activeMax = f.parse("2018-09-03");
        boolean isHolidaySelectable = true;
        Date cellDate = f.parse("2018-09-02");;
        boolean isHoliday = true;
        List<Integer> deactivatedDates = new ArrayList<>();
        deactivatedDates.add(1);

        boolean result =
                MonthView.isDeactive(dayOfWeek, activeMin, activeMax, isHolidaySelectable, cellDate, isHoliday, deactivatedDates);
        assertEquals(false, result);
    }
    @Test
    public void test9() throws Exception {
        SimpleDateFormat f  = new SimpleDateFormat("yyyy-MM-dd");
//        MonthView monthView = new MonthView(null, null);
        int dayOfWeek = 0;
        Date activeMin = f.parse("2018-09-01");
        Date activeMax = f.parse("2018-09-03");
        boolean isHolidaySelectable = false;
        Date cellDate = f.parse("2018-09-02");;
        boolean isHoliday = false;
        List<Integer> deactivatedDates = new ArrayList<>();
        deactivatedDates.add(1);

        boolean result =
                MonthView.isDeactive(dayOfWeek, activeMin, activeMax, isHolidaySelectable, cellDate, isHoliday, deactivatedDates);
        assertEquals(false, result);
    }
    @Test
    public void test10() throws Exception {
        SimpleDateFormat f  = new SimpleDateFormat("yyyy-MM-dd");
//        MonthView monthView = new MonthView(null, null);
        int dayOfWeek = 1;
        Date activeMin = f.parse("2018-09-01");
        Date activeMax = f.parse("2018-09-03");
        boolean isHolidaySelectable = true;
        Date cellDate = f.parse("2018-09-02");;
        boolean isHoliday = true;
        List<Integer> deactivatedDates = new ArrayList<>();
        deactivatedDates.add(1);
        deactivatedDates.add(2);
        deactivatedDates.add(3);
        deactivatedDates.add(4);
        deactivatedDates.add(5);


        boolean result =
                MonthView.isDeactive(dayOfWeek, activeMin, activeMax, isHolidaySelectable, cellDate, isHoliday, deactivatedDates);
        assertEquals(false, result);
    }
}
