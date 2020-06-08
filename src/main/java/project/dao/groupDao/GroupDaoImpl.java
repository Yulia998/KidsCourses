package project.dao.groupDao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import project.dao.OracleConnection;
import project.dao.SqlConstants;
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
            statement = connection.prepareStatement(SqlConstants.GET_ALL_GROUPS);
            resultSet = statement.executeQuery();
            Group group;
            while (resultSet.next()) {
                group = getGroup(resultSet);
                group.getCourse().setName(resultSet.getString("COURSENAME"));
                classes.add(group);
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting all groups", e);
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
            statement = connection.prepareStatement(SqlConstants.ADD_GROUP);
            setParamQuery(group);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in adding group", e);
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
            statement = connection.prepareStatement(SqlConstants.GROUP_BY_NAME);
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                group = getGroup(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting group by name", e);
        }
        disconnect();
        return group;
    }

    public String getClassNameById(int id) {
        String name = null;
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.GROUP_NAME_BY_ID);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                name = resultSet.getString("NAME");
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting group by id", e);
        }
        disconnect();
        return name;
    }

    public void updateClass(Group group) {
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.UPDATE_GROUP);
            setParamQuery(group);
            statement.setInt(4, group.getId());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in updating group", e);
        }
        disconnect();
    }

    public List<String> getDateTimeByGroup(int classId) {
        List<String> dateTime = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.LESSONS_BY_GROUP);
            statement.setInt(1, classId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                dateTime.add(resultSet.getString("LESSONDATE"));
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting lesson date and time by group", e);
        }
        disconnect();
        return dateTime;
    }

    public void addLessonTime (int classId, Timestamp lessondate) {
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.ADD_LESSON);
            statement.setInt(1, classId);
            statement.setTimestamp(2, lessondate);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in adding lesson date and time", e);
        }
        disconnect();
    }

    public void deleteLessonTime(int classId) {
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.DELETE_LESSON);
            statement.setInt(1, classId);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in deleting lesson date and time", e);
        }
        disconnect();
    }

    public List<HashMap<String, String>> schedule(String id, String startDate, String endDate) {
        List<HashMap<String, String>> schedule = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.GET_SCHEDULE);
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
            LOGGER.error("Error in getting lesson schedule", e);
        }
        disconnect();
        return schedule;
    }

    public void deleteClass(int id) {
        super.delete("CLASS", id);
    }
}
