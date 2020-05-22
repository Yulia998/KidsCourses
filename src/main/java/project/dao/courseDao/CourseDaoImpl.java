package project.dao.courseDao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import project.dao.OracleConnection;
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
            statement = connection.prepareStatement("SELECT COURSE.* " +
                    "FROM COURSE, CLASS " +
                    "WHERE CLASS.COURSEID=COURSE.COURSEID AND CLASS.CLASSID=?");
            statement.setInt(1, groupId);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                course = getCourse(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении курса по группе", e);
        }
        disconnect();
        return course;
    }

    public List<Course> getAllCourses() {
        List<Course> courses = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT * FROM COURSE");
            resultSet = statement.executeQuery();
            Course course;
            while (resultSet.next()) {
                course = getCourse(resultSet);
                courses.add(course);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении всех курсов", e);
        }
        disconnect();
        return courses;
    }

    public void addCourse (Course course) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO COURSE VALUES (COURSE_SEQ.NEXTVAL, ?, ?, ?, ?)");
            setParamQuery(course);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при добавлении курса", e);
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
            statement = connection.prepareStatement("UPDATE COURSE SET NAME=?, " +
                    "DESCRIPTION=?, DURATION=?, PRICE=? " +
                    "WHERE COURSEID=?");
            setParamQuery(course);
            statement.setInt(5, course.getId());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при обновлении курса", e);
        }
        disconnect();
    }

    public Course getCourseById (int id) {
        Course course = null;
        connect();
        try {
            statement = connection.prepareStatement("SELECT * FROM COURSE " +
                    "WHERE COURSEID=?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                course = getCourse(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении курса по id", e);
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
