package com.savvi.rangepickersample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.savvi.rangedatepicker.*;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import static com.savvi.rangedatepicker.RangeState.FIRST;

public class SampleActivity extends AppCompatActivity {


    CalendarPickerView calendar;
    Button button;

    Calendar lastYear;
    Calendar nextYear;
    final List<Integer> deactivedates = new ArrayList<>();
    Date selectFirst;
    Date selectLast;

    Date activeMin;
    Date activeMax;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sample);

        nextYear = Calendar.getInstance();
        nextYear.add(Calendar.MONTH, 6);

        lastYear = Calendar.getInstance();
//        lastYear.add(Calendar.YEAR, -10);

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        button = (Button) findViewById(R.id.get_selected_dates);

        ArrayList<Date> selected = new ArrayList<Date>();
//        try {
//            selected.add(new SimpleDateFormat("yyyy-MM-dd").parse("2018-09-01"));
//        } catch (ParseException e) {
//            throw new RuntimeException(e);
//        }
        init(lastYear, nextYear, selected, new ArrayList<Integer>(), true);
        /*
        ArrayList<Integer> list = new ArrayList<>();
//        list.add(1);// 日曜
//        list.add(7);// 土曜

        calendar.deactivateDates(list);
        ArrayList<Date> arrayList = new ArrayList<>();
        try {
            SimpleDateFormat dateformat = new SimpleDateFormat("dd-MM-yyyy");
            String strdate = "20-9-2018";
            String strdate2 = "26-9-2018";
            Date newdate = dateformat.parse(strdate);
            Date newdate2 = dateformat.parse(strdate2);
            arrayList.add(newdate);
            arrayList.add(newdate2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
//        try {
            calendar.init(lastYear.getTime(), nextYear.getTime(), new SimpleDateFormat("yyyy年MM月", Locale.getDefault())) //
                    .inMode(CalendarPickerView.SelectionMode.RANGE) //
//                    .withSelectedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-01"))// デフォルト選択の日付
                    .withSelectedDates(arrayList)
                    .withDeactivateDates(list);// 選択できない曜日
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//                .withHighlightedDates(arrayList);
*/

        Button all = (Button) findViewById(R.id.all);
        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deactivedates.clear();
                init(lastYear, nextYear, selectedDates(selectFirst, selectLast), deactivedates, false);
            }
        });
        Button heijitu = (Button) findViewById(R.id.heijitu);
        heijitu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deactivedates.clear();
                deactivedates.add(1);// 日曜
                deactivedates.add(7);// 土曜
                init(lastYear, nextYear, selectedDates(selectFirst, selectLast), deactivedates, false);
            }
        });
        Button shuku = (Button) findViewById(R.id.shuku);
        shuku.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deactivedates.clear();
                deactivedates.add(2);// 月
                deactivedates.add(3);// 火
                deactivedates.add(4);// 水
                deactivedates.add(5);// 木
                deactivedates.add(6);// 金
                init(lastYear, nextYear, selectedDates(selectFirst, selectLast), deactivedates, false);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                calendar.
                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                List<Date> dates = calendar.getSelectedDates();
                StringBuilder builder = new StringBuilder();
                for (Date date: dates) {
                        builder.append(format.format(date)+",");
                }
                Log.d("list", builder.substring(0,builder.length()));

            }
        });


    }

    private List<Date> selectedDates(Date... dates) {
        ArrayList<Date> arrayList = new ArrayList<>();
        if (dates == null || dates.length <= 0) {
            return arrayList;
        }
//        try {
//            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy-MM-dd");
        for (Date date : dates) {
            if (date != null) {
//                Date newdate = dateformat.parse(date);
                arrayList.add(date);
            }
        }
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        return arrayList;
    }

    /**
     * 範囲選択のクリア
     */
    void clear() {
        selectFirst = null;
        selectLast = null;
        activeMin = null;
        activeMax = null;
        calendar.setActiveMin(null);
        calendar.setActiveMax(null);
        selected = false;
    }
    boolean selected;
    private void init(Calendar last, Calendar next, final List<Date> selectedDates, final List<Integer> deactivedates, boolean scroll) {
        // 祝日
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        List<Date> holidayList = new ArrayList<>();
        try {
            holidayList.add(format.parse("2018/07/18"));
            holidayList.add(format.parse("2018/07/21"));
            holidayList.add(format.parse("2018/07/29"));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }


//        if (activeMin != null) {
            calendar.init(last.getTime(), next.getTime(), TimeZone.getTimeZone("Asia/Tokyo"), Locale.getDefault(), new SimpleDateFormat("yyyy年MM月"), activeMin, activeMax) //
                    .inMode(CalendarPickerView.SelectionMode.RANGE) //
//                    .withSelectedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-01"))// デフォルト選択の日付
                    .setHoliday(holidayList)
                    .withDeactivateDates(deactivedates)
                    .withSelectedDates(selectedDates, scroll);
//        } else {
//            calendar.init(last.getTime(), next.getTime(), new SimpleDateFormat("yyyy年MM月", Locale.getDefault())) //
//                    .inMode(CalendarPickerView.SelectionMode.RANGE) //
////                    .withSelectedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-01"))// デフォルト選択の日付
//                    .setHoliday(holidayList)
//                    .withDeactivateDates(deactivedates)
//                    .withSelectedDates(selectedDates, scroll);
////                .setShortWeekdays(weeks)
//            // 選択できない曜日
//        }

        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(MonthCellDescriptor cell) {
//                calendar.set

                switch (cell.getRangeState()) {
                    case FIRST:
                    case LAST:
                        Log.d("hoge", cell.getRangeState() + " " + cell.getDate());
                        selected = true;
                        selectLast = cell.getDate();
                        calendar.setActiveMin(null);
                        calendar.setActiveMax(null);
                        activeMin = null;
                        activeMax = null;
//                        init(lastYear, nextYear, selectedDates, SampleActivity.this.deactivedates, false);

                        break;
                    case MIDDLE:
                        Log.d("hoge", "MIDDLE " + cell.getDate());
                        break;
                    case NONE:
                        Log.d("hoge", "NONE " + cell.getDate());

                        if (selected) {
                            clear();
                            init(lastYear, nextYear, new ArrayList<Date>(), SampleActivity.this.deactivedates, false);
                        } else {
                            selectFirst = cell.getDate();
                            // 未選択の状態で選択された場合、31日より先を選択不可とする
                            Date date = cell.getDate();
                            Calendar cal = Calendar.getInstance();
                            cal.setTime(date);

                            // 選択可能な最大値
                            cal.add(Calendar.DATE, 30);
                            activeMax = cal.getTime();

                            // 選択可能な最小値
                            cal.add(Calendar.DATE, -61);
                            activeMin = cal.getTime();


                            // 選択日
                            List<Date> selectedDates = new ArrayList<>();
                            selectedDates.add(date);

//                            List<Date> from = createList(lastYear.getTime(), activeMin);
//                            List<Date> to = createList(activeMax, nextYear.getTime());
//                            deactivedates.addAll(from);


                            // 選択不可曜日
                            init(lastYear, nextYear, selectedDates, SampleActivity.this.deactivedates, false);
                        }
                        break;
                    default:
                        throw new RuntimeException();
                }
//                if (cell.getRangeState() == RangeState.NONE) {
//                } else if (cell.getRangeState() == RangeState.LAST) {
////                    activeMax = null;
////                    activeMin = null;
////                    init(lastYear, nextYear, selectedDates, SampleActivity.this.deactivedates);
//                }
                Log.d("sample", "******* rangeState=" + cell.getRangeState() + "onDateSelected:" + cell.getDate());
            }

            @Override
            public void onDateUnselected(MonthCellDescriptor cell) {
                selectFirst = null;
                selectLast = null;
                Log.d("sample", "******* rangeState=" + cell.getRangeState() + "onDateUnselected:" + cell.getDate());
            }
        });
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//                .withHighlightedDates(arrayList);
    }

//    private List<Date> createList(Date from, Date to) {
//        List<Date> list = new ArrayList<>();
//        Date date = from;
//        while(from.compareTo(to) ==0) {
//            list.add(date);
//            Calendar cal = Calendar.getInstance();
//            cal.setTime(date);
//            cal.add(Calendar.DATE, 1);
//            date = cal.getTime();
//        }
//        return list;
//    }
}
