// Copyright 2012 Square, Inc.
package com.savvi.rangedatepicker;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 見た目の管理
 */
public class MonthView extends LinearLayout {
    TextView title;
    CalendarGridView grid;
    private Listener listener;
    private List<CalendarCellDecorator> decorators;
    private boolean isRtl;
    private Locale locale;
    private boolean isHolidaySelectable = true;
    private List<Calendar> holidayList = new ArrayList<>();

    ArrayList<Integer> deactivatedDates;

    public static MonthView create(ViewGroup parent, LayoutInflater inflater,
                                   DateFormat weekdayNameFormat, Listener listener, Calendar today, int dividerColor,
                                   int dayBackgroundResId, int dayTextColorResId, int titleTextColor, boolean displayHeader,
                                   int headerTextColor, Locale locale, DayViewAdapter adapter, boolean isHolidaySelectable) {
        return create(parent, inflater, weekdayNameFormat, listener, today, dividerColor,
                dayBackgroundResId, dayTextColorResId, titleTextColor, displayHeader, headerTextColor, null,
                locale, adapter, isHolidaySelectable);
    }

    public void setHolidayList(List<Calendar> holidayList) {
        this.holidayList.addAll(holidayList);
    }

    public static MonthView create(ViewGroup parent, LayoutInflater inflater,
                                   DateFormat weekdayNameFormat, Listener listener, Calendar today, int dividerColor,
                                   int dayBackgroundResId, int dayTextColorResId, int titleTextColor, boolean displayHeader,
                                   int headerTextColor, List<CalendarCellDecorator> decorators, Locale locale,
                                   DayViewAdapter adapter, boolean isHolidaySelectable) {
        final MonthView view = (MonthView) inflater.inflate(R.layout.month, parent, false);
        view.setDayViewAdapter(adapter);
        view.setDividerColor(dividerColor);
        view.setDayTextColor(dayTextColorResId);
        view.setTitleTextColor(titleTextColor);
        view.setDisplayHeader(displayHeader);
        view.setHeaderTextColor(headerTextColor);
        view.isHolidaySelectable = isHolidaySelectable;

        if (dayBackgroundResId != 0) {
            view.setDayBackground(dayBackgroundResId);
        }

        final int originalDayOfWeek = today.get(Calendar.DAY_OF_WEEK);

        view.isRtl = isRtl(locale);
        view.locale = locale;
        int firstDayOfWeek = today.getFirstDayOfWeek();
        final CalendarRowView headerRow = (CalendarRowView) view.grid.getChildAt(0);
        headerRow.setBackground(headerRow.getContext().getDrawable(R.drawable.header_row_background));
        for (int offset = 0; offset < 7; offset++) {
            today.set(Calendar.DAY_OF_WEEK, getDayOfWeek(firstDayOfWeek, offset, view.isRtl));
            final TextView textView = (TextView) headerRow.getChildAt(offset);
            textView.setText(weekdayNameFormat.format(today.getTime()));
            if (offset == 0) {
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.dateTimeRangePickerRangeTextColorSunday));
            } else if (offset == 6) {
                textView.setTextColor(textView.getContext().getResources().getColor(R.color.dateTimeRangePickerRangeTextColorSaturday));
            }
        }
        today.set(Calendar.DAY_OF_WEEK, originalDayOfWeek);
        view.listener = listener;
        view.decorators = decorators;
        return view;
    }

    private static int getDayOfWeek(int firstDayOfWeek, int offset, boolean isRtl) {
        int dayOfWeek = firstDayOfWeek + offset;
        if (isRtl) {
            return 8 - dayOfWeek;
        }
        return dayOfWeek;
    }

    private static boolean isRtl(Locale locale) {
        // TODO convert the build to gradle and use getLayoutDirection instead of this (on 17+)?
        final int directionality = Character.getDirectionality(locale.getDisplayName(locale).charAt(0));
        return directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT
                || directionality == Character.DIRECTIONALITY_RIGHT_TO_LEFT_ARABIC;
    }

    public MonthView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setDecorators(List<CalendarCellDecorator> decorators) {
        this.decorators = decorators;
    }

    public List<CalendarCellDecorator> getDecorators() {
        return decorators;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        title = (TextView) findViewById(R.id.title);
        grid = (CalendarGridView) findViewById(R.id.calendar_grid);
    }

    public void init(MonthDescriptor month, List<List<MonthCellDescriptor>> cells,
                     boolean displayOnly, Typeface titleTypeface, Typeface dateTypeface, List<Integer> deactivatedDates,
                     Date activeMin, Date activeMax, boolean isHolidaySelectable) {
        Logr.d("Initializing MonthView (%d) for %s", System.identityHashCode(this), month);
        this.isHolidaySelectable = isHolidaySelectable;
        long start = System.currentTimeMillis();
        title.setText(month.getLabel());
        NumberFormat numberFormatter = NumberFormat.getInstance(locale);

        final int numRows = cells.size();
        grid.setNumRows(numRows);
        for (int i = 0; i < 6; i++) {
            CalendarRowView weekRow = (CalendarRowView) grid.getChildAt(i + 1);
            weekRow.setListener(listener);
            if (i < numRows) {
                weekRow.setVisibility(VISIBLE);
                List<MonthCellDescriptor> week = cells.get(i);
                for (int c = 0; c < week.size(); c++) {
                    MonthCellDescriptor cell = week.get(isRtl ? 6 - c : c);// TODO ここに日付の情報を持ってるぽいので、土日祝の設定をfor文以下で読み込む
                    CalendarCellView cellView = (CalendarCellView) weekRow.getChildAt(c);

                    // 日付設定
                    String cellDate = numberFormatter.format(cell.getValue());
                    if (!cellView.getDayOfMonthTextView().getText().equals(cellDate)) {
                        cellView.getDayOfMonthTextView().setText(cellDate);
                    }
                    cellView.setEnabled(cell.isCurrentMonth());
                    int dayOfWeek = c + 1;
                    if (deactivatedDates.contains(dayOfWeek))
                        cellView.setClickable(false);
                    else
                        cellView.setClickable(!displayOnly);


                    // 祝日かどうか
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(cell.getDate());
                    boolean isHoliday = false;
                    for (Calendar h : this.holidayList) {
                        if (h.get(Calendar.YEAR) == cal.get(Calendar.YEAR)
                                && h.get(Calendar.MONTH) == cal.get(Calendar.MONTH)
                                && h.get(Calendar.DAY_OF_MONTH) == cal.get(Calendar.DAY_OF_MONTH)) {
//                        if (h.compareTo(cal) == 0) {
                            isHoliday = true;
                            break;
                        }

                    }

                    Log.d("***** holidaytest", new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime()) + " isHoliday=" + isHoliday + " isSunday=" +cellView.isSunday() + " isSaturday=" + cellView.isSaturday() );


                    if (isDeactive(dayOfWeek, activeMin, activeMax, isHolidaySelectable, cell.getDate(), isHoliday, deactivatedDates)) {
                        // 選択不可の曜日
                        cellView.setSelectable(cell.isSelectable());
                        cellView.setSelected(false);
                        cellView.setCurrentMonth(cell.isCurrentMonth());
//                        cellView.setToday(cell.isToday());
                        cellView.setRangeState(RangeState.NONE);
                        cellView.setHighlighted(cell.isHighlighted());
                        cellView.setRangeUnavailable(cell.isUnavailable());
                        cellView.setDeactivated(true);

                    } else {

                        // 選択可の曜日
                        cellView.setSelectable(cell.isSelectable());
                        cellView.setSelected(cell.isSelected());
                        cellView.setCurrentMonth(cell.isCurrentMonth());
//                            cellView.setToday(cell.isToday());
                        cellView.setRangeState(cell.getRangeState());
                        cellView.setHighlighted(cell.isHighlighted());
                        cellView.setRangeUnavailable(cell.isUnavailable());
                        cellView.setDeactivated(false);

                        // 祝日設定
                        cellView.setHoliday(isHoliday);
                        if (!isHoliday) {
                            // 土日設定
                            switch (dayOfWeek) {
                                case 1:// Sun
                                    cellView.setSunday(true);
                                    break;
                                case 7:// Sat
                                    cellView.setSaturday(true);
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            cellView.setSunday(false);
                            cellView.setSaturday(false);
                        }

                    }


                    cellView.setTag(cell);

                    if (null != decorators) {
                        for (CalendarCellDecorator decorator : decorators) {
                            decorator.decorate(cellView, cell.getDate());
                        }
                    }
                }
            } else {
                weekRow.setVisibility(GONE);
            }
        }

        if (titleTypeface != null) {
            title.setTypeface(titleTypeface);
        }
        if (dateTypeface != null) {
            grid.setTypeface(dateTypeface);
        }

        Logr.d("MonthView.init took %d ms", System.currentTimeMillis() - start);
    }

    /**
     * 非アクティブならtrue、アクティブならfalseを返します
     * 非アクティブの条件は下記の通り
     * <p>
     * <ul>
     * <li>1. activemin!=nullかつactiveminより過去</li>
     * <li>2. activemax!=nullかつactivemaxより未来</li>
     * <li>3. 1.2.以外でdeactivatedDatesが設定されていない</li>
     * </ul>
     *
     * @param dayOfWeek
     * @param activeMin
     * @param activeMax
     * @param isHolidaySelectable
     * @param cellDate
     * @param isHoliday
     * @param deactivatedDates
     * @return
     */
    static boolean isDeactive(int dayOfWeek, Date activeMin, Date activeMax
            , boolean isHolidaySelectable, Date cellDate, boolean isHoliday,
                              List<Integer> deactivatedDates) {
        // アクティブな範囲外の場合は非アクティブ
        if ((activeMin != null && cellDate.before(activeMin))
                || (activeMax != null && cellDate.after(activeMax))) {
            return true;
        }

        // 祝日の場合は曜日に関係なくisHolidaySelectableで判定
        if (isHoliday) {
            return !isHolidaySelectable;
        }
        // deactivatedDatesが設定されているなら曜日と照らし合わせる
        if (deactivatedDates != null) {
            return deactivatedDates.contains(dayOfWeek);
        }
        // それ以外はアクティブ
        return false;
    }

    public void init(MonthDescriptor month, List<List<MonthCellDescriptor>> cells,
                     boolean displayOnly, Typeface titleTypeface, Typeface dateTypeface, List<Integer> deactivatedDates, boolean isHolidaySelectable) {
        init(month, cells, displayOnly, titleTypeface, dateTypeface, deactivatedDates, null, null, isHolidaySelectable);
    }

    public void setDividerColor(int color) {
        grid.setDividerColor(color);
    }

    public void setDayBackground(int resId) {
        grid.setDayBackground(resId);
    }

    public void setDayTextColor(int resId) {
        grid.setDayTextColor(resId);
    }

    public void setDayViewAdapter(DayViewAdapter adapter) {
        grid.setDayViewAdapter(adapter);
    }

    public void setTitleTextColor(int color) {
        title.setTextColor(color);
    }

    public void setDisplayHeader(boolean displayHeader) {
        grid.setDisplayHeader(displayHeader);
    }

    public void setHeaderTextColor(int color) {
        grid.setHeaderTextColor(color);
    }

    public interface Listener {
        void handleClick(MonthCellDescriptor cell, View v);
    }
}
