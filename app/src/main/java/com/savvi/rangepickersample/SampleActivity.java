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
//        lastYear.add(Calendar.MONTH, 1);

        calendar = (CalendarPickerView) findViewById(R.id.calendar_view);
        button = (Button) findViewById(R.id.get_selected_dates);

        ArrayList<Date> selected = new ArrayList<Date>();
        init(lastYear, nextYear, selected, new ArrayList<Integer>(), true);

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
                for (Date date : dates) {
                    builder.append(format.format(date) + ",");
                }
                Log.d("list", builder.substring(0, builder.length()));

            }
        });


    }

    private List<Date> selectedDates(Date... dates) {
        ArrayList<Date> arrayList = new ArrayList<>();
        if (dates == null || dates.length <= 0) {
            return arrayList;
        }
        for (Date date : dates) {
            if (date != null) {
                arrayList.add(date);
            }
        }
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
            holidayList.add(format.parse("2018/08/01"));
            holidayList.add(format.parse("2018/09/01"));
            holidayList.add(format.parse("2018/09/02"));

        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // カレンダーの初期化
        calendar.init(last.getTime(), next.getTime(), TimeZone.getTimeZone("Asia/Tokyo"), Locale.getDefault(), new SimpleDateFormat("yyyy年MM月"), activeMin, activeMax) //
                .inMode(CalendarPickerView.SelectionMode.RANGE) //
//                    .withSelectedDate(new SimpleDateFormat("yyyy-MM-dd").parse("2018-10-01"))// デフォルト選択の日付
                .setHoliday(holidayList)
                .withDeactivateDates(deactivedates)
                .withSelectedDates(selectedDates, scroll);

        // 日付選択時の処理
        calendar.setOnDateSelectedListener(new CalendarPickerView.OnDateSelectedListener() {
            @Override
            public void onDateSelected(MonthCellDescriptor cell) {
                switch (cell.getRangeState()) {
                    case FIRST:
                    case LAST:
                        selected = true;
                        selectLast = cell.getDate();
                        calendar.setActiveMin(null);
                        calendar.setActiveMax(null);
                        activeMin = null;
                        activeMax = null;
                        break;
                    case MIDDLE:
                        break;
                    case NONE:
                        if (selected) {
                            // 範囲選択中の場合は、範囲選択、選択可能範囲をクリアする
                            clear();
                            init(lastYear, nextYear, new ArrayList<Date>(), SampleActivity.this.deactivedates, false);
                        } else {
                            // 範囲選択していない最初のタップ
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

                            // 選択不可曜日
                            init(lastYear, nextYear, selectedDates, SampleActivity.this.deactivedates, false);
                        }
                        break;
                    default:
                        throw new RuntimeException();
                }
            }

            @Override
            public void onDateUnselected(MonthCellDescriptor cell) {
                selectFirst = null;
                selectLast = null;
            }
        });
    }

}
