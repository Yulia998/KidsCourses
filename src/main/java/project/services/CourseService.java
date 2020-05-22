package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dao.courseDao.CourseDao;
import project.model.entities.Course;

import java.util.List;

@Service
public class CourseService {
    @Autowired
    private CourseDao course;

    public List<Course> getAllCourses() {
        return course.getAllCourses();
    }

    public Course getCourseByGroup (String groupId) {
        return course.getCourseByGroup(Integer.parseInt(groupId));
    }

    public void addCourse (String name, String description, String lessons, String price) {
        Course courseAdd = new Course(name, description, Integer.parseInt(lessons), Float.parseFloat(price));
        course.addCourse(courseAdd);
    }

    public void updateCourse (String id, String name, String description, String lessons, String price) {
        Course courseUpdate = new Course(name, description, Integer.parseInt(lessons), Float.parseFloat(price));
        courseUpdate.setId(Integer.parseInt(id));
        course.updateCourse(courseUpdate);
    }

    public Course getCourseById (String id) {
        return course.getCourseById(Integer.parseInt(id));
    }

    public void deleteCourse(String id) {
        course.deleteCourse(Integer.parseInt(id));
    }
}
