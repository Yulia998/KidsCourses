package project.model.entities;

import java.sql.Date;
import java.util.Objects;

public class Employee {
    private int id;
    private String fullName;
    private Date birthday;
    private String telephone;
    private String city;
    private Employee manager;
    private String login;
    private String password;

    public Employee() {
    }

    public Employee(String fullName, Date birthday, String telephone, String city, Employee manager, String login, String password) {
        this.fullName = fullName;
        this.birthday = birthday;
        this.telephone = telephone;
        this.city = city;
        this.manager = manager;
        this.login = login;
        this.password = password;
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

    public Employee getManager() {
        return manager;
    }

    public void setManager(Employee employee) {
        this.manager = employee;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return id == employee.id &&
                Objects.equals(fullName, employee.fullName) &&
                Objects.equals(birthday, employee.birthday) &&
                Objects.equals(telephone, employee.telephone) &&
                Objects.equals(city, employee.city) &&
                Objects.equals(manager, employee.manager) &&
                Objects.equals(login, employee.login) &&
                Objects.equals(password, employee.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fullName, birthday, telephone, city, manager, login, password);
    }

    @Override
    public String toString() {
        return "Employee{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", birthday=" + birthday +
                ", telephone='" + telephone + '\'' +
                ", city='" + city + '\'' +
                ", employee='" + manager + '\'' +
                ", login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
