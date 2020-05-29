package project.dao.employeeDao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import project.dao.OracleConnection;
import project.dao.SqlConstants;
import project.model.entities.Employee;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class EmployeeDaoImpl extends OracleConnection implements EmployeeDao {
    private static final Logger LOGGER = Logger.getLogger(OracleConnection.class);

    public String getEmplId (String username) {
        String id = null;
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.ID_OF_EMPLOYEE);
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getString("EMPLOYEEID");
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting id of employee", e);
        }
        disconnect();
        return id;
    }

    public List<Employee> getAllEmployees() {
        List<Employee> empl = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.GET_ALL_EMPLOYEES);
            resultSet = statement.executeQuery();
            Employee employee;
            while (resultSet.next()) {
                employee = getEmployee(resultSet);
                empl.add(employee);
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting all employees", e);
        }
        disconnect();
        return empl;
    }

    public void addEmployee (Employee employee) {
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.ADD_EMPLOYEE);
            setParamQuery(employee);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in adding employee", e);
        }
        disconnect();
    }

    private void setParamQuery(Employee employee) throws SQLException {
        statement.setString(1, employee.getFullName());
        statement.setDate(2, employee.getBirthday());
        statement.setString(3, employee.getTelephone());
        statement.setString(4, employee.getCity());
        statement.setInt(5, employee.getManager().getId());
        statement.setString(6, employee.getLogin());
        statement.setString(7, employee.getPassword());
    }

    public Employee getEmployeeById(int id) {
        Employee employee = null;
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.EMPLOYEE_BY_ID);
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                employee = getEmployee(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting employee by id", e);
        }
        disconnect();
        return employee;
    }

    private Employee getEmployee(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getInt("EMPLOYEEID"));
        employee.setFullName(resultSet.getString("ENAME"));
        employee.setBirthday(resultSet.getDate("BIRTHDAY"));
        employee.setTelephone(resultSet.getString("TELEPHONE"));
        employee.setCity(resultSet.getString("CITY"));
        Employee manager = new Employee();
        manager.setId(resultSet.getInt("MID"));
        manager.setFullName(resultSet.getString("MNAME"));
        employee.setManager(manager);
        employee.setLogin(resultSet.getString("USERNAME"));
        employee.setPassword(resultSet.getString("PASSWORD"));
        return employee;
    }

    public void updateEmployee (Employee employee) {
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.UPDATE_EMPLOYEE);
            setParamQuery(employee);
            statement.setInt(8, employee.getId());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in updating employee", e);
        }
        disconnect();
    }

    public Map<String, String> getUser(String username) {
        Map<String, String> user = new HashMap<>();
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.USER);
            statement.setString(1, username);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user.put("password", resultSet.getString("PASSWORD"));
                user.put("status", resultSet.getString("NAME"));
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting user", e);
        }
        disconnect();
        return user;
    }

    public List<Employee> getAllTeachers() {
        List<Employee> empl = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.GET_ALL_TEACHERS);
            resultSet = statement.executeQuery();
            Employee employee;
            while (resultSet.next()) {
                employee = new Employee();
                employee.setId(resultSet.getInt("EMPLOYEEID"));
                employee.setFullName(resultSet.getString("FULLNAME"));
                empl.add(employee);
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting all teachers", e);
        }
        disconnect();
        return empl;
    }

    public void deleteEmployee(int id) {
        super.delete("EMPLOYEE", id);
    }

    public int getStatusId (String name) {
        int statusId = 0;
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.GET_STATUS_ID);
            statement.setString(1, name);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                statusId = resultSet.getInt("STATUSID");
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting status id", e);
        }
        disconnect();
        return statusId;
    }

    public void addEmpSchedule (int employeeId, int statusId, int classId) {
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.ADD_EMPSCHEDULE);
            statement.setInt(1, employeeId);
            statement.setInt(2, statusId);
            statement.setInt(3, classId);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in adding employee schedule", e);
        }
        disconnect();
    }

    public void updateEmpSchedule(int employeeId, String status, int classId) {
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.UPDATE_EMPSCHEDULE);
            statement.setInt(1, employeeId);
            statement.setInt(2, classId);
            statement.setString(3, status);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Error in updating employee schedule", e);
        }
        disconnect();
    }

    public List<HashMap<String, String>> getTeachersByGroup(int classId) {
        List<HashMap<String, String>> teachers = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement(SqlConstants.TEACHERS_BY_GROUP);
            statement.setInt(1, classId);
            resultSet = statement.executeQuery();
            HashMap<String, String> result;
            while (resultSet.next()) {
                result = new HashMap<>();
                result.put("empSchedId", resultSet.getString("EMPSCHEDULEID"));
                result.put("emplId", resultSet.getString("EMPLOYEEID"));
                result.put("emplName", resultSet.getString("FULLNAME"));
                result.put("status", resultSet.getString("NAME"));
                teachers.add(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Error in getting teachers by group", e);
        }
        disconnect();
        return teachers;
    }
}
