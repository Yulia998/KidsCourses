package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dao.OracleConnection;
import project.dao.childDao.ChildDao;
import project.dao.courseDao.CourseDao;
import project.dao.employeeDao.EmployeeDao;
import project.dao.groupDao.GroupDao;
import project.model.WorkWithCalendar;
import project.model.entities.Course;
import project.model.entities.Group;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class GroupService {
    @Autowired
    private GroupDao group;
    @Autowired
    private CourseDao course;
    @Autowired
    private EmployeeDao employee;
    @Autowired
    private ChildDao child;

    public List<HashMap<String, String>> getScheduleById(String id, String startDate, String endDate) throws ParseException {
        List<HashMap<String, String>> schedule = group.schedule(id, startDate, endDate);
        Calendar calendar = Calendar.getInstance();
        java.util.Date date;
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

    public String getClassNameById(String id) {
        return group.getClassNameById(Integer.parseInt(id));
    }

    public List<HashMap<String, String>> getScheduleByUsername(String username, String startDate, String endDate) throws ParseException {
        String id = employee.getEmplId(username);
        return getScheduleById(id, startDate, endDate);
    }

    public List<LinkedHashMap<String, String>> getAllClasses(boolean edit) throws ParseException {
        List<LinkedHashMap<String, String>> classes = new ArrayList<>();
        List<Group> groups = group.getAllClasses();
        String key, keyId;
        List<HashMap<String, String>> teachers;
        for(Group groupAdd : groups) {
            LinkedHashMap<String, String> groupMap = new LinkedHashMap<>();
            groupMap.put("id", String.valueOf(groupAdd.getId()));
            groupMap.put("Название группы", groupAdd.getName());
            groupMap.put("Дата начала", String.valueOf(groupAdd.getStartDate()));
            groupMap.put("courseId", String.valueOf(groupAdd.getCourse().getId()));
            groupMap.put("Название курса", groupAdd.getCourse().getName());
            int groupId = groupAdd.getId();
            teachers = employee.getTeachersByGroup(groupId);
            for(HashMap<String, String> teacher : teachers) {
                if(teacher.get("status").equals("PRO")) {
                    key = "Преподователь";
                    keyId="proId";
                } else {
                    key = "Помощник";
                    keyId="assisId";
                }
                groupMap.put(key, teacher.get("emplName"));
                if (edit) {
                    groupMap.put(keyId, teacher.get("emplId"));
                }
            }
            if (!groupMap.containsKey("Помощник")) {
                groupMap.put("Помощник", null);
            }
            Set<String> uniqData = getUniqueData(groupId);
            Iterator<String> iterator = uniqData.iterator();
            String date = "";
            while (iterator.hasNext()){
                date += iterator.next() + "<br>";
            }
            groupMap.put("День недели/время", date);
            classes.add(groupMap);
        }
        return classes;
    }

    private Set<String> getUniqueData (int groupId) throws ParseException {
        List<String> dateTimes;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd");
        dateTimes = group.getDateTimeByGroup(groupId);
        String date;
        Set<String> uniqData = new LinkedHashSet<>();
        for(String dateTime : dateTimes){
            calendar.setTime(format.parse(dateTime.substring(0, 10)));
            date = WorkWithCalendar.getDay(calendar.get(Calendar.DAY_OF_WEEK)) + " / " + dateTime.substring(11, 16);
            uniqData.add(date);
        }
        return uniqData;
    }

    public void addClass(String name, String startDate, String course, String pro,
                         String assistant, List<String> days, List<String> time, List<String> children) throws ParseException {
        Course courseGroup = new Course();
        courseGroup.setId(Integer.parseInt(course));
        Group groupAdd = new Group(name, java.sql.Date.valueOf(startDate), courseGroup);
        group.addClass(groupAdd);
        int classId = group.getClassByName(name).getId();
        for (String childAdd : children) {
            child.addChildSchedule(Integer.parseInt(childAdd), classId);
        }
        int statusId = employee.getStatusId("PRO");
        employee.addEmpSchedule(Integer.parseInt(pro), statusId, classId);
        if (!assistant.equals("")) {
            statusId = employee.getStatusId("ASSISTANT");
            employee.addEmpSchedule(Integer.parseInt(assistant), statusId, classId);
        }
        List<String> empty = new ArrayList<>();
        empty.add("");
        time.removeAll(empty);
        addLessonTime(classId, startDate, days, time);
    }

    private void addLessonTime (int classId, String startDate, List<String> days, List<String> time) throws ParseException {
        int duration = course.getCourseByGroup(classId).getDuration();
        List<String> dates = WorkWithCalendar.getDatesByDay(days, time, startDate, duration);
        dates.stream().forEach(date -> group.addLessonTime(classId, Timestamp.valueOf(date)));
    }

    public void updateClass(String id, String name, String startDate, String course, String pro,
                            String assistant, List<String> days, List<String> time, List<String> children) throws ParseException {
        int classId = Integer.parseInt(id);
        Course courseGroup = new Course();
        courseGroup.setId(Integer.parseInt(course));
        Group groupUpdate = new Group(name, Date.valueOf(startDate), courseGroup);
        groupUpdate.setId(classId);
        group.updateClass(groupUpdate);
        employee.updateEmpSchedule(Integer.parseInt(pro), "PRO", classId);
        List<HashMap<String, String>> teachers = employee.getTeachersByGroup(classId);
        long asistants = teachers.stream().filter(teacher ->
                teacher.get("status").equals("ASSISTANT")).count();
        if (assistant.equals("")){
            if (asistants != 0) {
                int empSchedId = Integer.parseInt(teachers.stream().filter(teacher ->
                        teacher.get("status").equals("ASSISTANT")).findAny().get().get("empSchedId"));
                ((OracleConnection)group).delete("EMPSCHEDULE", empSchedId);
            }
        } else {
            if (asistants != 0) {
                employee.updateEmpSchedule(Integer.parseInt(assistant), "ASSISTANT", classId);
            } else {
                int statusId = employee.getStatusId("ASSISTANT");
                employee.addEmpSchedule(Integer.parseInt(assistant), statusId, classId);
            }
        }
        List<HashMap<String, String>> childrenBefore = child.getChildrenByGroup(classId);
        for (HashMap<String, String> childBefore : childrenBefore) {
            String childBeforeId = childBefore.get("id");
            if (!children.contains(childBeforeId)) {
                child.deleteChSchedule(Integer.parseInt(childBeforeId), classId);
            }
            children.remove(childBeforeId);
        }
        for (String childAdd : children) {
            child.addChildSchedule(Integer.parseInt(childAdd), classId);
        }
        Set<String> dateTimeBefore = getUniqueData(classId);
        List<String> empty = new ArrayList<>();
        empty.add("");
        time.removeAll(empty);
        Set<String> dateTimeAfter = new LinkedHashSet<>();
        for (int i = 0; i < days.size(); i++) {
            dateTimeAfter.add(days.get(i) + " / " + time.get(i));
        }
        int deleted = 0;
        for (String before : dateTimeBefore) {
            if (dateTimeAfter.contains(before)) {
                dateTimeAfter.remove(before);
                deleted++;
            }
        }
        if(dateTimeAfter.size() != 0 || dateTimeBefore.size() != deleted) {
            group.deleteLessonTime(classId);
            addLessonTime(classId, startDate, days, time);
        }
    }

    public void deleteClass(String id) {
        group.deleteClass(Integer.parseInt(id));
    }
}
