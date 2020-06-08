package project.dao.courseDao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import project.dao.OracleConnection;
import project.dao.SqlConstants;
import project.model.entities.Course;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class CourseDaoImpl extends OracleConnection implements CourseDao {
    private static final Logger LOGGER = Logger.getLogger(CourseDaoImpl.class);

    public Course getCourseByGroup (int groupId) {
        Course course = null;
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.COURSE_BY_GROUP);
            statement.setInt(1, groupId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                course = getCourse(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting course by group", e);
        }
        disconnect();
        return course;
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.GET_ALL_COURSES);
            resultSet = statement.executeQuery();
            Course course;
            while (resultSet.next()) {
                course = getCourse(resultSet);
                courses.add(course);
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting all courses", e);
        }
        disconnect();
        return courses;
    }

    public void addCourse (Course course) {
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.ADD_COURSE);
            setParamQuery(course);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in adding course", e);
        }
        disconnect();
    }

    private void setParamQuery(Course course) throws SQLException {
        statement.setString(1, course.getName());
        statement.setString(2, course.getDescription());
        statement.setInt(3, course.getDuration());
        statement.setFloat(4, course.getPrice());
    }

    public void updateCourse (Course course) {
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.UPDATE_COURSE);
            setParamQuery(course);
            statement.setInt(5, course.getId());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in updating course", e);
        }
        disconnect();
    }

    public Course getCourseById (int id) {
        Course course = null;
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.COURSE_BY_ID);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                course = getCourse(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting course by id", e);
        }
        disconnect();
        return course;
    }

    private Course getCourse(ResultSet resultSet) throws SQLException {
        Course course = new Course();
        course.setId(resultSet.getInt("COURSEID"));
        course.setName(resultSet.getString("NAME"));
        course.setDescription(resultSet.getString("DESCRIPTION"));
        course.setDuration(resultSet.getInt("DURATION"));
        course.setPrice(resultSet.getFloat("PRICE"));
        return course;
    }

    public void deleteCourse(int id) {
        super.delete("COURSE", id);
    }
}
