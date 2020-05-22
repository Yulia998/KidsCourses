package project.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class WorkWithCalendar {
    public static String getDay(int number) {
        switch (number) {
            case 1:
                return "Воскресенье";
            case 2:
                return "Понедельник";
            case 3:
                return "Вторник";
            case 4:
                return "Среда";
            case 5:
                return "Четверг";
            case 6:
                return "Пятница";
            case 7:
                return "Суббота";
        }
        return "";
    }

    public static List<String> getDatesByDay (List<String> days, List<String> times, String startDate, int numberLes) throws ParseException {
        List<String> dates = new ArrayList<>();
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd");
        Date date;
        Calendar calendar = Calendar.getInstance();
        int nLesson = 0;
        date = format.parse(startDate);
        calendar.setTime(date);
        String dayOfWeek, dayTime;
        int index;
        do {
            long foundDays = days.stream().filter(day ->
                    day.equals(getDay(calendar.get(Calendar.DAY_OF_WEEK)))).count();
            if (foundDays != 0) {
                dayOfWeek = days.stream().filter(day ->
                        day.equals(getDay(calendar.get(Calendar.DAY_OF_WEEK)))).findAny().get();
                dayTime = format.format(calendar.getTime());
                index = days.indexOf(dayOfWeek);
                dayTime += " " + times.get(index) + ":00";
                dates.add(dayTime);
                nLesson++;
            }
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        } while (nLesson < numberLes);
        return dates;
    }
}
