package project.dao;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.stereotype.Component;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.swing.*;
import java.sql.*;
import java.sql.Date;
import java.util.*;

@Component
@PropertySources ({
    @PropertySource("classpath:application.properties"),
    @PropertySource("classpath:log4j.properties"),
})
public class OracleDaoConnection {
    private Context ctx;
    private Connection connection;
    private ResultSet resultSet;
    private PreparedStatement statement;
    private Hashtable<String, String> ht = new Hashtable<>();
    @Value("${contextFactory}")
    String contextFactor;
    @Value("${url}")
    String url;
    @Value("${dataSource}")
    String dataSource;
    private static final Logger LOGGER = Logger.getLogger(OracleDaoConnection.class);

    public void connect() {
        ht.put(Context.INITIAL_CONTEXT_FACTORY, contextFactor);
        ht.put(Context.PROVIDER_URL, url);
        try {
            ctx = new InitialContext(ht);
            DataSource ds = (DataSource) ctx.lookup(dataSource);
            connection = ds.getConnection();
        } catch (NamingException | SQLException e) {
            LOGGER.error("Ошибка при подключении к базе данных", e);
        }
    }

    public void disconnect() {
        try {
            connection.close();
            if (resultSet != null) {
                resultSet.close();
            }
            statement.close();
            ctx.close();
        } catch (Exception e) {
            LOGGER.error("Ошибка при отсоединении в базе данных", e);
        }
    }

    public Map<String, String> getUser(String username) {
        Map<String, String> user = new HashMap<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT EMP.PASSWORD, STATUS.NAME " +
                    "FROM EMPLOYEE EMP, EMPSCHEDULE SC, STATUS " +
                    "WHERE SC.EMPLOYEEID=EMP.EMPLOYEEID AND SC.STATUSID=STATUS.STATUSID " +
                    "AND EMP.USERNAME=?");
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user.put("password", resultSet.getString("PASSWORD"));
                user.put("status", resultSet.getString("NAME"));
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении пользователя", e);
        }
        disconnect();
        return user;
    }

    public String getEmplId (String username) {
        String id = null;
        connect();
        try {
            statement = connection.prepareStatement("SELECT EMPLOYEEID " +
                    "FROM EMPLOYEE " +
                    "WHERE EMPLOYEE.USERNAME = ?");
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            HashMap<String, String> result;
            while (resultSet.next()) {
                id = resultSet.getString("EMPLOYEEID");
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении id пользователя", e);
        }
        disconnect();
        return id;
    }

    public List<HashMap<String, String>> schedule(String id, String startDate, String endDate) {
        List<HashMap<String, String>> schedule = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT class.classid, class.name, lessontime.lessondate, status.name statusname " +
                    "FROM lessontime, class, empschedule sc, employee em, status " +
                    "WHERE em.employeeid=sc.employeeid AND sc.classid=class.classid AND lessontime.classid=class.classid AND sc.statusid=status.statusid " +
                    "AND lessontime.lessondate BETWEEN TO_DATE (?, 'yyyy-mm-dd') AND TO_DATE (?, 'yyyy-mm-dd') " +
                    "AND em.employeeid=? " +
                    "ORDER BY lessontime.lessondate");
            statement.setString(1, startDate);
            statement.setString(2, endDate);
            statement.setString(3, id);
            resultSet = statement.executeQuery();
            HashMap<String, String> result;
            while (resultSet.next()) {
                result = new HashMap<>();
                result.put("groupId", resultSet.getString("CLASSID"));
                result.put("groupName", resultSet.getString("NAME"));
                result.put("lessonDate", resultSet.getString("LESSONDATE"));
                result.put("statusName", resultSet.getString("STATUSNAME"));
                schedule.add(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении расписания", e);
        }
        disconnect();
        return schedule;
    }

    public Map<String, String> getCourseByGroup (int groupId) {
        Map<String, String> course = new HashMap<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT COURSE.NAME, COURSE.DESCRIPTION, COURSE.DURATION " +
                    "FROM COURSE, CLASS " +
                    "WHERE CLASS.COURSEID=COURSE.COURSEID AND CLASS.CLASSID=?");
            statement.setInt(1, groupId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                course.put("name", resultSet.getString("NAME"));
                course.put("description", resultSet.getString("DESCRIPTION"));
                course.put("duration", resultSet.getString("DURATION"));
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении курса по группе", e);
        }
        disconnect();
        return course;
    }

    public List<HashMap<String, String>> getChildrenByGroup (int groupId){
        List<HashMap<String, String>> children = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT CHILD.CHILDID, CHILD.FULLNAME, TRUNC(MONTHS_BETWEEN(SYSDATE, CHILD.BIRTHDAY)/12) AS AGE " +
                    "FROM CHILD, CHSCHEDULE SC, CLASS " +
                    "WHERE SC.CLASSID=CLASS.CLASSID AND SC.CHILDID=CHILD.CHILDID " +
                    "AND CLASS.CLASSID=?");
            statement.setInt(1, groupId);
            resultSet = statement.executeQuery();
            HashMap<String, String> result;
            while (resultSet.next()) {
                result = new HashMap<>();
                result.put("id", resultSet.getString("CHILDID"));
                result.put("name", resultSet.getString("FULLNAME"));
                result.put("age", resultSet.getString("AGE"));
                children.add(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении детей по группе", e);
        }
        disconnect();
        return children;
    }

    public List<HashMap<String, String>> getAllTeachers() {
        List<HashMap<String, String>> empl = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT DISTINCT EMP.EMPLOYEEID, EMP.FULLNAME " +
                    "FROM EMPLOYEE EMP, EMPSCHEDULE SC, STATUS " +
                    "WHERE EMP.EMPLOYEEID=SC.EMPLOYEEID AND SC.STATUSID=STATUS.STATUSID " +
                    "AND STATUS.NAME IN ('ASSISTANT','PRO')");
            resultSet = statement.executeQuery();
            HashMap<String, String> result;
            while (resultSet.next()) {
                result = new HashMap<>();
                result.put("id", resultSet.getString("EMPLOYEEID"));
                result.put("name", resultSet.getString("FULLNAME"));
                empl.add(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении всех учителей", e);
        }
        disconnect();
        return empl;
    }

    public List<LinkedHashMap<String, String>> getAllEmployees() {
        List<LinkedHashMap<String, String>> empl = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT EMP.EMPLOYEEID, EMP.FULLNAME ENAME, EMP.BIRTHDAY, " +
                    "EMP.TELEPHONE, EMP.CITY, MNG.FULLNAME MNAME, EMP.USERNAME, EMP.PASSWORD " +
                    "FROM EMPLOYEE EMP LEFT JOIN EMPLOYEE MNG " +
                    "ON EMP.MANAGER=MNG.EMPLOYEEID");
            resultSet = statement.executeQuery();
            LinkedHashMap<String, String> result;
            while (resultSet.next()) {
                result = new LinkedHashMap<>();
                result.put("id", resultSet.getString("EMPLOYEEID"));
                result.put("Имя", resultSet.getString("ENAME"));
                result.put("День рождение", resultSet.getString("BIRTHDAY"));
                result.put("Телефон", resultSet.getString("TELEPHONE"));
                result.put("Город", resultSet.getString("CITY"));
                result.put("Менеджер", resultSet.getString("MNAME"));
                result.put("Логин", resultSet.getString("USERNAME"));
                result.put("Пароль", resultSet.getString("PASSWORD"));
                empl.add(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении всех работников", e);
        }
        disconnect();
        return empl;
    }

    public void addEmployee (String name, Date birthday, String tel, String city,
                             int manager, String login, String password) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO EMPLOYEE VALUES (NULL, ?, ?, ?, ?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setDate(2, birthday);
            statement.setString(3, tel);
            statement.setString(4, city);
            statement.setInt(5, manager);
            statement.setString(6, login);
            statement.setString(7, password);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при добавлении работника", e);
        }
        disconnect();
    }

    public Map<String, String> getEmployeeById(int id) {
        Map<String, String> employee = new HashMap<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT EMP.EMPLOYEEID, EMP.FULLNAME ENAME, EMP.BIRTHDAY, " +
                    "EMP.TELEPHONE, EMP.CITY, MNG.EMPLOYEEID MID, MNG.FULLNAME MNAME, EMP.USERNAME, EMP.PASSWORD " +
                    "FROM EMPLOYEE EMP LEFT JOIN EMPLOYEE MNG " +
                    "ON EMP.MANAGER=MNG.EMPLOYEEID " +
                    "WHERE EMP.EMPLOYEEID=?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                employee.put("id", resultSet.getString("EMPLOYEEID"));
                employee.put("eName", resultSet.getString("ENAME"));
                employee.put("birthday", resultSet.getString("BIRTHDAY"));
                employee.put("tel", resultSet.getString("TELEPHONE"));
                employee.put("city", resultSet.getString("CITY"));
                employee.put("mId", resultSet.getString("MID"));
                employee.put("mName", resultSet.getString("MNAME"));
                employee.put("username", resultSet.getString("USERNAME"));
                employee.put("password", resultSet.getString("PASSWORD"));
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении работника по id", e);
        }
        disconnect();
        return employee;
    }

    public void updateEmployee (int id, String name, Date birthday, String tel, String city,
                                int manager, String login, String password) {
        connect();
        try {
            statement = connection.prepareStatement("UPDATE EMPLOYEE SET FULLNAME=?," +
                    "BIRTHDAY=?, TELEPHONE=?, CITY=?, MANAGER=?, USERNAME=?, PASSWORD=?" +
                    "WHERE EMPLOYEEID=?");
            statement.setString(1, name);
            statement.setDate(2, birthday);
            statement.setString(3, tel);
            statement.setString(4, city);
            statement.setInt(5, manager);
            statement.setString(6, login);
            statement.setString(7, password);
            statement.setInt(8, id);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при обновлении работника", e);
        }
        disconnect();
    }

    public List<LinkedHashMap<String, String>> getAllCourses() {
        List<LinkedHashMap<String, String>> courses = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT * FROM COURSE");
            resultSet = statement.executeQuery();
            LinkedHashMap<String, String> result;
            while (resultSet.next()) {
                result = new LinkedHashMap<>();
                result.put("id", resultSet.getString("COURSEID"));
                result.put("Название", resultSet.getString("NAME"));
                result.put("Описание", resultSet.getString("DESCRIPTION"));
                result.put("Кол-во занятий", resultSet.getString("DURATION"));
                result.put("Цена 1 занятия", resultSet.getString("PRICE"));
                courses.add(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении всех курсов", e);
        }
        disconnect();
        return courses;
    }

    public void addCourse (String name, String description, int lessons, float price) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO COURSE VALUES (NULL, ?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setInt(3, lessons);
            statement.setFloat(4, price);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при добавлении курса", e);
        }
        disconnect();
    }

    public void updateCourse (int id, String name, String description, int lessons, float price) {
        connect();
        try {
            statement = connection.prepareStatement("UPDATE COURSE SET NAME=?, " +
                    "DESCRIPTION=?, DURATION=?, PRICE=? " +
                    "WHERE COURSEID=?");
            statement.setString(1, name);
            statement.setString(2, description);
            statement.setInt(3, lessons);
            statement.setFloat(4, price);
            statement.setInt(5, id);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при обновлении курса", e);
        }
        disconnect();
    }

    public Map<String, String> getCourseById (int id) {
        Map<String, String> course = new HashMap<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT * FROM COURSE " +
                    "WHERE COURSEID=?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                course.put("id", resultSet.getString("COURSEID"));
                course.put("name", resultSet.getString("NAME"));
                course.put("description", resultSet.getString("DESCRIPTION"));
                course.put("duration", resultSet.getString("DURATION"));
                course.put("price", resultSet.getString("PRICE"));
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении курса по id", e);
        }
        disconnect();
        return course;
    }

    public List<LinkedHashMap<String, String>> getAllChildren() {
        List<LinkedHashMap<String, String>> children = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT * FROM CHILD ORDER BY FULLNAME");
            resultSet = statement.executeQuery();
            LinkedHashMap<String, String> result;
            while (resultSet.next()) {
                result = new LinkedHashMap<>();
                result.put("id", resultSet.getString("CHILDID"));
                result.put("Имя", resultSet.getString("FULLNAME"));
                result.put("День рождение", resultSet.getString("BIRTHDAY"));
                result.put("Город", resultSet.getString("CITY"));
                result.put("Телефон родителей", resultSet.getString("TELPARENT"));
                children.add(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении всех детей", e);
        }
        disconnect();
        return children;
    }

    public void addChild (String name, Date birthday, String tel, String city) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO CHILD VALUES (NULL, ?, ?, ?, ?)");
            statement.setString(1, name);
            statement.setDate(2, birthday);
            statement.setString(3, city);
            statement.setString(4, tel);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при добавлении ребенка", e);
        }
        disconnect();
    }

    public void updateChild (int id, String name, Date birthday, String tel, String city) {
        connect();
        try {
            statement = connection.prepareStatement("UPDATE CHILD SET FULLNAME=?, " +
                    "BIRTHDAY=?, CITY=?, TELPARENT=? " +
                    "WHERE CHILDID=?");
            statement.setString(1, name);
            statement.setDate(2, birthday);
            statement.setString(3, city);
            statement.setString(4, tel);
            statement.setInt(5, id);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при обновлении ребенка", e);
        }
        disconnect();
    }

    public Map<String, String> getChildById(int id) {
        Map<String, String> child = new HashMap<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT * FROM CHILD WHERE CHILDID=?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                child.put("id", resultSet.getString("CHILDID"));
                child.put("eName", resultSet.getString("FULLNAME"));
                child.put("birthday", resultSet.getString("BIRTHDAY"));
                child.put("city", resultSet.getString("CITY"));
                child.put("tel", resultSet.getString("TELPARENT"));
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении ребенка по id", e);
        }
        disconnect();
        return child;
    }

    public List<LinkedHashMap<String, String>> getAllClasses() {
        List<LinkedHashMap<String, String>> classes = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT CLASS.CLASSID, CLASS.NAME, CLASS.STARTDATE, COURSE.NAME COURSENAME, COURSE.COURSEID " +
                    "FROM CLASS, COURSE WHERE CLASS.COURSEID=COURSE.COURSEID");
            resultSet = statement.executeQuery();
            LinkedHashMap<String, String> result;
            while (resultSet.next()) {
                result = new LinkedHashMap<>();
                result.put("id", resultSet.getString("CLASSID"));
                result.put("Название группы", resultSet.getString("NAME"));
                result.put("Дата начала", resultSet.getString("STARTDATE"));
                result.put("Название курса", resultSet.getString("COURSENAME"));
                result.put("courseId", resultSet.getString("COURSEID"));
                classes.add(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении всех групп", e);
        }
        disconnect();
        return classes;
    }

    public List<HashMap<String, String>> getTeachersByGroup(int classId) {
        List<HashMap<String, String>> teachers = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT EMPSC.EMPSCHEDULEID, EMP.EMPLOYEEID, EMP.FULLNAME, STATUS.NAME " +
                    "FROM CLASS, EMPSCHEDULE EMPSC, STATUS, EMPLOYEE EMP " +
                    "WHERE EMP.EMPLOYEEID=EMPSC.EMPLOYEEID AND EMPSC.STATUSID=STATUS.STATUSID AND EMPSC.CLASSID=CLASS.CLASSID " +
                    "AND CLASS.CLASSID = ?");
            statement.setInt(1, classId);
            resultSet = statement.executeQuery();
            HashMap<String, String> result;
            while (resultSet.next()) {
                result = new HashMap<>();
                result.put("empSchedId", resultSet.getString("EMPSCHEDULEID"));
                result.put("emplId", resultSet.getString("EMPLOYEEID"));
                result.put("emplName", resultSet.getString("FULLNAME"));
                result.put("status", resultSet.getString("NAME"));
                teachers.add(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении учителей по группе", e);
        }
        disconnect();
        return teachers;
    }

    public List<String> getDateTimeByGroup(int classId) {
        List<String> dateTime = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT DATA.LESSONDATE " +
                    "FROM CLASS, LESSONTIME DATA " +
                    "WHERE DATA.CLASSID=CLASS.CLASSID " +
                    "AND CLASS.CLASSID = ? ORDER BY DATA.LESSONDATE");
            statement.setInt(1, classId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                dateTime.add(resultSet.getString("LESSONDATE"));
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении времени занятий по группе", e);
        }
        disconnect();
        return dateTime;
    }

    public void addClass (int courseId, String name, Date startDate) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO CLASS VALUES (NULL, ?, ?, ?)");
            statement.setInt(1, courseId);
            statement.setString(2, name);
            statement.setDate(3, startDate);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при добавлении группы", e);
        }
        disconnect();
    }

    public void addChildSchedule (int childId, int classId) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO CHSCHEDULE VALUES (NULL, ?, ?)");
            statement.setInt(1, childId);
            statement.setInt(2, classId);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при добавлении расписания ребенка", e);
        }
        disconnect();
    }

    public void addEmpSchedule (int employeeId, int statusId, int classId) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO EMPSCHEDULE VALUES (NULL, ?, ?, ?)");
            statement.setInt(1, employeeId);
            statement.setInt(2, statusId);
            statement.setInt(3, classId);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при добавлении расписания работника", e);
        }
        disconnect();
    }

    public void addLessonTime (int classId, Timestamp lessondate) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO LESSONTIME VALUES (NULL, ?, ?)");
            statement.setInt(1, classId);
            statement.setTimestamp(2, lessondate);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при добавлении время занятия", e);
        }
        disconnect();
    }

    public Map<String, String> getClassByName(String name) {
        Map<String, String> group = new HashMap<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT * FROM CLASS WHERE NAME=?");
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                group.put("id", resultSet.getString("CLASSID"));
                group.put("courseId", resultSet.getString("COURSEID"));
                group.put("name", resultSet.getString("NAME"));
                group.put("startDate", resultSet.getString("STARTDATE"));
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении группы по названию", e);
        }
        disconnect();
        return group;
    }

    public String getClassNameById(int id) {
        String name = null;
        connect();
        try {
            statement = connection.prepareStatement("SELECT NAME FROM CLASS WHERE CLASSID=?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                name = resultSet.getString("NAME");
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении группы по id", e);
        }
        disconnect();
        return name;
    }

    public int getStatusId (String name) {
        int statusId = 0;
        connect();
        try {
            statement = connection.prepareStatement("SELECT STATUSID FROM STATUS WHERE NAME=?");
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                statusId = resultSet.getInt("STATUSID");
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении id статуса", e);
        }
        disconnect();
        return statusId;
    }

    public void updateClass(int id, int courseId, String name, Date startDate) {
        connect();
        try {
            statement = connection.prepareStatement("UPDATE CLASS SET COURSEID=?, " +
                    "NAME=?, STARTDATE=? " +
                    "WHERE CLASSID=?");
            statement.setInt(1, courseId);
            statement.setString(2, name);
            statement.setDate(3, startDate);
            statement.setInt(4, id);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при обновлении группы", e);
        }
        disconnect();
    }

    public void updateEmpSchedule(int employeeId, String status, int classId) {
        connect();
        try {
            statement = connection.prepareStatement("UPDATE EMPSCHEDULE SET EMPLOYEEID=? " +
                    "WHERE CLASSID=? AND STATUSID=" +
                    "(SELECT STATUSID FROM STATUS WHERE NAME=?)");
            statement.setInt(1, employeeId);
            statement.setInt(2, classId);
            statement.setString(3, status);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при обновлении расписания сотрудника", e);
        }
        disconnect();
    }

    public void deleteChSchedule(int childId, int classId) {
        connect();
        try {
            statement = connection.prepareStatement("DELETE FROM CHSCHEDULE " +
                    "WHERE CHILDID = ? AND CLASSID = ?");
            statement.setInt(1, childId);
            statement.setInt(2, classId);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при удалении расписания ребенка", e);
        }
        disconnect();
    }

    public void delete(String table, int id) {
        connect();
        try {
            statement = connection.prepareStatement("DELETE FROM " + table + " WHERE " +
                    table + "ID = ?");
            statement.setInt(1, id);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при удалении таблицы "  + table, e);
        }
        disconnect();
    }

    public void deleteLessonTime(int classId) {
        connect();
        try {
            statement = connection.prepareStatement("DELETE FROM LESSONTIME WHERE " +
                    "CLASSID = ?");
            statement.setInt(1, classId);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при удалении времени занятия", e);
        }
        disconnect();
    }
}
