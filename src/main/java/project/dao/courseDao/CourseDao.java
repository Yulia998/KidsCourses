package project.dao.courseDao;

import project.model.entities.Course;

import java.util.List;

public interface CourseDao {
    Course getCourseByGroup (int groupId);
    List<Course> getAllCourses();
    void addCourse (Course course);
    void updateCourse (Course course);
    Course getCourseById (int id);
    void deleteCourse(int id);
}
