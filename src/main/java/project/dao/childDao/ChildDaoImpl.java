package project.dao.childDao;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;
import project.dao.OracleConnection;
import project.model.entities.Child;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ChildDaoImpl extends OracleConnection implements ChildDao {
    private static final Logger LOGGER = Logger.getLogger(ChildDaoImpl.class);

    public List<Child> getAllChildren() {
        List<Child> children = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT * FROM CHILD ORDER BY FULLNAME");
            resultSet = statement.executeQuery();
            Child child;
            while (resultSet.next()) {
                child = getChild(resultSet);
                children.add(child);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении всех детей", e);
        }
        disconnect();
        return children;
    }

    public void addChild (Child child) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO CHILD VALUES (CHILD_SEQ.NEXTVAL, ?, ?, ?, ?)");
            setParamQuery(child);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при добавлении ребенка", e);
        }
        disconnect();
    }

    private void setParamQuery(Child child) throws SQLException {
        statement.setString(1, child.getFullName());
        statement.setDate(2, child.getBirthday());
        statement.setString(3, child.getCity());
        statement.setString(4, child.getTelephone());
    }

    public void updateChild (Child child) {
        connect();
        try {
            statement = connection.prepareStatement("UPDATE CHILD SET FULLNAME=?, " +
                    "BIRTHDAY=?, CITY=?, TELPARENT=? " +
                    "WHERE CHILDID=?");
            setParamQuery(child);
            statement.setInt(5, child.getId());
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при обновлении ребенка", e);
        }
        disconnect();
    }

    public Child getChildById(int id) {
        Child child = null;
        connect();
        try {
            statement = connection.prepareStatement("SELECT * FROM CHILD WHERE CHILDID=?");
            statement.setInt(1, id);
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                child = getChild(resultSet);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении ребенка по id", e);
        }
        disconnect();
        return child;
    }

    private Child getChild(ResultSet resultSet) throws SQLException {
        Child child = new Child();
        child.setId(resultSet.getInt("CHILDID"));
        child.setFullName(resultSet.getString("FULLNAME"));
        child.setBirthday(resultSet.getDate("BIRTHDAY"));
        child.setCity(resultSet.getString("CITY"));
        child.setTelephone(resultSet.getString("TELPARENT"));
        return child;
    }

    public void deleteChild(int id) {
      super.delete("CHILD", id);
    }

    public void deleteChSchedule(int childId, int classId) {
        connect();
        try {
            statement = connection.prepareStatement("DELETE FROM CHSCHEDULE " +
                    "WHERE CHILDID = ? AND CLASSID = ?");
            statement.setInt(1, childId);
            statement.setInt(2, classId);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при удалении расписания ребенка", e);
        }
        disconnect();
    }


    public void addChildSchedule (int childId, int classId) {
        connect();
        try {
            statement = connection.prepareStatement("INSERT INTO CHSCHEDULE VALUES (CHSCHEDULE_SEQ.NEXTVAL, ?, ?)");
            statement.setInt(1, childId);
            statement.setInt(2, classId);
            statement.execute();
        } catch (SQLException e) {
            LOGGER.error("Ошибка при добавлении расписания ребенка", e);
        }
        disconnect();
    }

    public List<HashMap<String, String>> getChildrenByGroup (int groupId){
        List<HashMap<String, String>> children = new ArrayList<>();
        connect();
        try {
            statement = connection.prepareStatement("SELECT CHILD.CHILDID, CHILD.FULLNAME, TRUNC(MONTHS_BETWEEN(SYSDATE, CHILD.BIRTHDAY)/12) AS AGE " +
                    "FROM CHILD, CHSCHEDULE SC, CLASS " +
                    "WHERE SC.CLASSID=CLASS.CLASSID AND SC.CHILDID=CHILD.CHILDID " +
                    "AND CLASS.CLASSID=?");
            statement.setInt(1, groupId);
            resultSet = statement.executeQuery();
            HashMap<String, String> result;
            while (resultSet.next()) {
                result = new HashMap<>();
                result.put("id", resultSet.getString("CHILDID"));
                result.put("name", resultSet.getString("FULLNAME"));
                result.put("age", resultSet.getString("AGE"));
                children.add(result);
            }
        } catch (SQLException e) {
            LOGGER.error("Ошибка при получении детей по группе", e);
        }
        disconnect();
        return children;
    }
}
