package project.dao.employeeDao;

import project.model.entities.Employee;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface EmployeeDao {
    String getEmplId (String username);
    List<Employee> getAllEmployees();
    void addEmployee (Employee employee);
    Employee getEmployeeById(int id);
    void updateEmployee (Employee employee);
    Map<String, String> getUser(String username);
    List<Employee> getAllTeachers();
    void deleteEmployee(int id);
    int getStatusId (String name);
    void addEmpSchedule (int employeeId, int statusId, int classId);
    void updateEmpSchedule(int employeeId, String status, int classId);
    List<HashMap<String, String>> getTeachersByGroup(int classId);
}
