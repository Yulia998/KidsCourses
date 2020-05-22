package project.model.entities;


import java.util.Objects;

public class Course {
    private int id;
    private String name;
    private String description;
    private int duration;
    private float price;

    public Course() {
    }

    public Course(String name, String description, int duration, float price) {
        this.name = name;
        this.description = description;
        this.duration = duration;
        this.price = price;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return id == course.id &&
                duration == course.duration &&
                Float.compare(course.price, price) == 0 &&
                Objects.equals(name, course.name) &&
                Objects.equals(description, course.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, duration, price);
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", duration=" + duration +
                ", price=" + price +
                '}';
    }
}
