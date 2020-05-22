package project.dao.childDao;

import project.model.entities.Child;

import java.util.HashMap;
import java.util.List;

public interface ChildDao {
    List<Child> getAllChildren();
    void addChild (Child child);
    void updateChild (Child child);
    Child getChildById(int id);
    void deleteChild(int id);
    void deleteChSchedule(int childId, int classId);
    void addChildSchedule (int childId, int classId);
    List<HashMap<String, String>> getChildrenByGroup (int groupId);
}
