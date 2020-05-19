package project.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import project.dao.OracleDaoConnection;
import project.model.WorkWithCalendar;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Service
public class AdminService extends MainService {

    public List<HashMap<String, String>> getAllTeachers() {
        return connection.getAllTeachers();
    }

    public List<LinkedHashMap<String, String>> getAllEmployees() {
        List<LinkedHashMap<String, String>> employees = connection.getAllEmployees();
        for(LinkedHashMap<String, String> employee : employees) {
            WorkWithCalendar.formatDate(employee, "День рождение");
        }
        return employees;
    }

    public void addEmployee (String name, String birthday, String tel, String city,
                             String manager,  String login, String password) {
        connection.addEmployee(name, Date.valueOf(birthday), tel, city,
                Integer.parseInt(manager), login, password);
    }

    public void delete(String table, String id) {
        connection.delete(table, Integer.parseInt(id));
    }

    public String loadUserByUsername(String username) {
        return connection.getEmplId(username);
    }

    public Map<String, String> getEmployeeById(String id) {
        Map<String, String> empl = connection.getEmployeeById(Integer.parseInt(id));
        WorkWithCalendar.formatDate(empl, "birthday");
        return empl;
    }

    public void updateEmployee(String id, String name, String birthday, String tel, String city,
                               String manager,  String login, String password) {
        connection.updateEmployee(Integer.parseInt(id), name, Date.valueOf(birthday), tel, city,
                Integer.parseInt(manager), login, password);
    }

    public List<LinkedHashMap<String, String>> getAllCourses() {
        return connection.getAllCourses();
    }

    public void addCourse (String name, String description, String lessons, String price) {
        connection.addCourse(name, description, Integer.parseInt(lessons),
                                                            Float.parseFloat(price));
    }

    public void updateCourse (String id, String name, String description, String lessons, String price) {
        connection.updateCourse(Integer.parseInt(id), name, description, Integer.parseInt(lessons),
                Float.parseFloat(price));
    }

    public Map<String, String> getCourseById (String id) {
        return connection.getCourseById(Integer.parseInt(id));
    }

    public List<LinkedHashMap<String, String>> getAllChildren() {
        List<LinkedHashMap<String, String>> children = connection.getAllChildren();
        for(LinkedHashMap<String, String> child : children) {
            WorkWithCalendar.formatDate(child, "День рождение");
        }
        return children;
    }

    public void addChild (String name, String birthday, String tel, String city) {
        connection.addChild(name, Date.valueOf(birthday), tel, city);
    }

    public void updateChild (String id, String name, String birthday, String tel, String city) {
        connection.updateChild(Integer.parseInt(id), name, Date.valueOf(birthday),
                                                        tel, city);
    }

    public Map<String, String> getChildById(String id) {
        Map<String, String> child = connection.getChildById(Integer.parseInt(id));
        WorkWithCalendar.formatDate(child, "birthday");
        return child;
    }

    public List<LinkedHashMap<String, String>> getAllClasses(boolean edit) throws ParseException {
        List<LinkedHashMap<String, String>> classes = connection.getAllClasses();
        String key, keyId;
        List<HashMap<String, String>> teachers;
        for(LinkedHashMap<String, String> group : classes) {
            WorkWithCalendar.formatDate(group, "Дата начала");
            int groupId = Integer.parseInt(group.get("id"));
            teachers = connection.getTeachersByGroup(groupId);
            for(HashMap<String, String> teacher : teachers) {
                if(teacher.get("status").equals("PRO")) {
                    key = "Преподователь";
                    keyId="proId";
                } else {
                    key = "Помощник";
                    keyId="assisId";
                }
                group.put(key, teacher.get("emplName"));
                if (edit) {
                    group.put(keyId, teacher.get("emplId"));
                }
            }
            if (!group.containsKey("Помощник")) {
                group.put("Помощник", null);
            }
            Set<String> uniqData = getUniqueData(groupId);
            Iterator<String> iterator = uniqData.iterator();
            String date = "";
            while (iterator.hasNext()){
                date += iterator.next() + "<br>";
            }
            group.put("День недели/время", date);
        }
        return classes;
    }

    private Set<String> getUniqueData (int groupId) throws ParseException {
        List<String> dateTimes;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat format = new SimpleDateFormat();
        format.applyPattern("yyyy-MM-dd");
        dateTimes = connection.getDateTimeByGroup(groupId);
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
        connection.addClass(Integer.parseInt(course), name, Date.valueOf(startDate));
        int classId = Integer.parseInt(connection.getClassByName(name).get("id"));
        for (String child : children) {
            connection.addChildSchedule(Integer.parseInt(child), classId);
        }
        int statusId = connection.getStatusId("PRO");
        connection.addEmpSchedule(Integer.parseInt(pro), statusId, classId);
        if (!assistant.equals("")) {
            statusId = connection.getStatusId("ASSISTANT");
            connection.addEmpSchedule(Integer.parseInt(assistant), statusId, classId);
        }
        List<String> empty = new ArrayList<>();
        empty.add("");
        time.removeAll(empty);
        addLessonTime(classId, startDate, days, time);
    }

    private void addLessonTime (int classId, String startDate, List<String> days, List<String> time) throws ParseException {
        int duration = Integer.parseInt(connection.getCourseByGroup(classId).get("duration"));
        List<String> dates = WorkWithCalendar.getDatesByDay(days, time, startDate, duration);
        dates.stream().forEach(date -> connection.addLessonTime(classId, Timestamp.valueOf(date)));
    }

    public void updateClass(String id, String name, String startDate, String course, String pro,
                         String assistant, List<String> days, List<String> time, List<String> children) throws ParseException {
        int classId = Integer.parseInt(id);
        connection.updateClass(classId, Integer.parseInt(course), name, Date.valueOf(startDate));
        connection.updateEmpSchedule(Integer.parseInt(pro), "PRO", classId);
        List<HashMap<String, String>> teachers = connection.getTeachersByGroup(classId);
        long asistants = teachers.stream().filter(teacher ->
                teacher.get("status").equals("ASSISTANT")).count();
        if (assistant.equals("")){
            if (asistants != 0) {
                int empSchedId = Integer.parseInt(teachers.stream().filter(teacher ->
                        teacher.get("status").equals("ASSISTANT")).findAny().get().get("empSchedId"));
                connection.delete("EMPSCHEDULE", empSchedId);
            }
        } else {
            if (asistants != 0) {
                connection.updateEmpSchedule(Integer.parseInt(assistant), "ASSISTANT", classId);
            } else {
                int statusId = connection.getStatusId("ASSISTANT");
                connection.addEmpSchedule(Integer.parseInt(assistant), statusId, classId);
            }
        }
        List<HashMap<String, String>> childrenBefore = connection.getChildrenByGroup(classId);
        for (HashMap<String, String> childBefore : childrenBefore) {
            String childBeforeId = childBefore.get("id");
            if (!children.contains(childBeforeId)) {
                connection.deleteChSchedule(Integer.parseInt(childBeforeId), classId);
            }
            children.remove(childBeforeId);
        }
        for (String child : children) {
            connection.addChildSchedule(Integer.parseInt(child), classId);
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
            connection.deleteLessonTime(classId);
            addLessonTime(classId, startDate, days, time);
        }
    }
}
