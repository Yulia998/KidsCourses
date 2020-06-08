package project.model.entities;

import java.sql.Date;
import java.util.Objects;

public class Group {
    private int id;
    private String name;
    private Date startDate;
    private Course course;

    public Group() {
    }

    public Group(String name, Date startDate, Course course) {
        this.name = name;
        this.startDate = startDate;
        this.course = course;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return id == group.id &&
                Objects.equals(name, group.name) &&
                Objects.equals(startDate, group.startDate) &&
                course.equals(group.course);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startDate, course);
    }

    @Override
    public String toString() {
        return "Group{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", startDate=" + startDate +
                ", course=" + course +
                '}';
    }
}
