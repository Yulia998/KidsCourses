package project.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import project.dao.childDao.ChildDao;
import project.model.entities.Child;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

@Service
public class ChildService {
    @Autowired
    private ChildDao child;

    public List<Child> getAllChildren() {
        return child.getAllChildren();
    }


    public List<HashMap<String, String>> getChildrenByGroup (String groupId){
        return child.getChildrenByGroup(Integer.parseInt(groupId));
    }

    public void addChild (String name, String birthday, String tel, String city) {
        Child childAdd = new Child(name, Date.valueOf(birthday), tel, city);
        child.addChild(childAdd);
    }

    public void updateChild (String id, String name, String birthday, String tel, String city) {
        Child childUpdate = new Child(name, Date.valueOf(birthday), tel, city);
        childUpdate.setId(Integer.parseInt(id));
        child.updateChild(childUpdate);
    }

    public Child getChildById(String id) {
        return child.getChildById(Integer.parseInt(id));
    }

    public void deleteChild(String id) {
        child.deleteChild(Integer.parseInt(id));
    }
}
