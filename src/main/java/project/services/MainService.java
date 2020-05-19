package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dao.OracleDaoConnection;
import project.model.WorkWithCalendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MainService {
    @Autowired
    OracleDaoConnection connection;

    public List<HashMap<String, String>> getSchedule(String id, String startDate, String endDate) throws ParseException {
        List<HashMap<String, String>> schedule = connection.schedule(id, startDate, endDate);
        Calendar calendar = Calendar.getInstance();
        Date date;
        String dateTime;
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd");
        for(HashMap<String, String> map : schedule) {
            dateTime = map.get("lessonDate").substring(0, 10);
            date = format.parse(dateTime);
            calendar.setTime(date);
            map.put("dayOfWeek", WorkWithCalendar.getDay(calendar.get(Calendar.DAY_OF_WEEK)));
            map.put("dayDate", dateTime);
            map.replace("lessonDate", map.get("lessonDate").substring(11));
        }
        return schedule;
    }

    public List<HashMap<String, String>> getChildrenByGroup (String groupId){
        return connection.getChildrenByGroup(Integer.parseInt(groupId));
    }

    public String getClassNameById(String id) {
        return connection.getClassNameById(Integer.parseInt(id));
    }
}
