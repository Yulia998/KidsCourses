package project.dao.groupDao;

import project.model.entities.Group;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;

public interface GroupDao {
    List<Group> getAllClasses();
    void addClass (Group group);
    Group getClassByName(String name);
    String getClassNameById(int id);
    void updateClass(Group group);
    List<String> getDateTimeByGroup(int classId);
    void addLessonTime (int classId, Timestamp lessondate);
    void deleteLessonTime(int classId);
    List<HashMap<String, String>> schedule(String id, String startDate, String endDate);
    void deleteClass(int id);
}
