package com.savvi.rangedatepicker;

import android.view.ContextThemeWrapper;
import android.widget.TextView;

public class DefaultDayViewAdapter implements DayViewAdapter {
  @Override
  public void makeCellView(CalendarCellView parent) {
      TextView textView = new TextView(
              new ContextThemeWrapper(parent.getContext(), R.style.CalendarCell_CalendarDate));
      textView.setDuplicateParentStateEnabled(true);
      textView.setFocusable(false);
      textView.setClickable(false);
      parent.addView(textView);
      parent.setDayOfMonthTextView(textView);
  }
}
