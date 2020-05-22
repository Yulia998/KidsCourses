package project.dao.groupDao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import project.dao.OracleConnection;
import project.model.entities.Course;
import project.model.entities.Group;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;

@Repository
public class GroupDaoImpl extends OracleConnection implements GroupDao {
    private static final Logger LOGGER = Logger.getLogger(GroupDaoImpl.class);

    public List<Group> getAllClasses() {
        List<Group> classes = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT CLASS.CLASSID, CLASS.NAME, CLASS.STARTDATE, COURSE.NAME COURSENAME, COURSE.COURSEID " +
                    "FROM CLASS LEFT JOIN COURSE ON CLASS.COURSEID=COURSE.COURSEID");
            resultSet = statement.executeQuery();
            Group group;
            while (resultSet.next()) {
                group = getGroup(resultSet);
                group.getCourse().setName(resultSet.getString("COURSENAME"));
                classes.add(group);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении всех групп", e);
        }
        disconnect();
        return classes;
    }

    private Group getGroup(ResultSet resultSet) throws SQLException {
        Group group = new Group();
        group.setId(resultSet.getInt("CLASSID"));
        group.setName(resultSet.getString("NAME"));
        group.setStartDate(resultSet.getDate("STARTDATE"));
        Course course = new Course();
        course.setId(resultSet.getInt("COURSEID"));
        group.setCourse(course);
        return group;
    }

    public void addClass (Group group) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO CLASS VALUES (CLASS_SEQ.NEXTVAL, ?, ?, ?)");
            setParamQuery(group);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при добавлении группы", e);
        }
        disconnect();
    }

    private void setParamQuery(Group group) throws SQLException {
        statement.setInt(1, group.getCourse().getId());
        statement.setString(2, group.getName());
        statement.setDate(3, group.getStartDate());
    }

    public Group getClassByName(String name) {
        Group group = null;
        connect();
        try {
            statement = connection.prepareStatement("SELECT * FROM CLASS WHERE NAME=?");
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                group = getGroup(resultSet);
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

    public void updateClass(Group group) {
        connect();
        try {
            statement = connection.prepareStatement("UPDATE CLASS SET COURSEID=?, " +
                    "NAME=?, STARTDATE=? " +
                    "WHERE CLASSID=?");
            setParamQuery(group);
            statement.setInt(4, group.getId());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при обновлении группы", e);
        }
        disconnect();
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

    public void addLessonTime (int classId, Timestamp lessondate) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO LESSONTIME VALUES (LESSONTIME_SEQ.NEXTVAL, ?, ?)");
            statement.setInt(1, classId);
            statement.setTimestamp(2, lessondate);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при добавлении время занятия", e);
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

    public void deleteClass(int id) {
        super.delete("CLASS", id);
    }
}
