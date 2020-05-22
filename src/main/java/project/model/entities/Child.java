package project.model.entities;


import java.sql.Date;
import java.util.Objects;

public class Child {
    private int id;
    private String fullName;
    private Date birthday;
    private String telephone;
    private String city;

    public Child() {
    }

    public Child(String fullName, Date birthday, String telephone, String city) {
        this.fullName = fullName;
        this.birthday = birthday;
        this.telephone = telephone;
        this.city = city;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Child child = (Child) o;
        return id == child.id &&
                Objects.equals(fullName, child.fullName) &&
                Objects.equals(birthday, child.birthday) &&
                Objects.equals(telephone, child.telephone) &&
                Objects.equals(city, child.city);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, birthday, telephone, city);
    }

    @Override
    public String toString() {
        return "Child{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", birthday=" + birthday +
                ", telParent='" + telephone + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}
