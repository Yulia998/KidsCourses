package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import project.dao.employeeDao.EmployeeDao;
import project.model.entities.Employee;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class EmployeeService implements UserDetailsService {
    @Autowired
    private EmployeeDao employee;

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        List<GrantedAuthority> authorities = new ArrayList<>();
        Map<String, String> hashMap = employee.getUser(s);
        String role;
        if(!hashMap.isEmpty()) {
            if(hashMap.get("status").equals("PRO") || hashMap.get("status").equals("ASSISTANT")) {
                role = "USER";
            } else {
                role = "ADMIN";
            }
            authorities.add(new SimpleGrantedAuthority(role));
            User user = new User(s, "{noop}" + hashMap.get("password"), authorities);
            return user;
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public List<Employee> getAllTeachers() {
        return employee.getAllTeachers();
    }

    public List<Employee> getAllEmployees() {
        return employee.getAllEmployees();
    }

    public void addEmployee (String name, String birthday, String tel, String city,
                             String manager,  String login, String password) {
        Employee managerAdd = new Employee();
        managerAdd.setId(Integer.parseInt(manager));
        Employee employeeAdd = new Employee(name, Date.valueOf(birthday), tel, city,
                managerAdd, login, password);
        employee.addEmployee(employeeAdd);
    }

    public void deleteEmployee(String id) {
        employee.deleteEmployee(Integer.parseInt(id));
    }

    public String getEmplId(String username) {
        return employee.getEmplId(username);
    }

    public Employee getEmployeeById(String id) {
        Employee empl = employee.getEmployeeById(Integer.parseInt(id));
        return empl;
    }

    public void updateEmployee(String id, String name, String birthday, String tel, String city,
                               String manager,  String login, String password) {
        Employee managerUpdate = new Employee();
        managerUpdate.setId(Integer.parseInt(manager));
        Employee employeeUpdate = new Employee(name, Date.valueOf(birthday), tel, city,
                managerUpdate, login, password);
        employeeUpdate.setId(Integer.parseInt(id));
        employee.updateEmployee(employeeUpdate);
    }
}
