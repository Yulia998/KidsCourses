package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.dao.OracleDaoConnection;
import project.model.WorkWithCalendar;
import project.services.AdminService;

import java.security.Principal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.util.*;

@Controller
public class AdminController {
    @Autowired
    private AdminService adminService;

    @RequestMapping("/employees")
    @ResponseBody
    public ModelAndView getEmployees(Model model) {
        List<LinkedHashMap<String, String>> employees = adminService.getAllEmployees();
        model.addAttribute("hashList", employees);
        model.addAttribute("urlTable", "getEmployeeForm");
        model.addAttribute("delete", "deleteEmployee");
        return new ModelAndView("table");
    }

    @RequestMapping("/getEmployeeForm/{action}")
    public String getEmployeeForm(@RequestParam(value = "error", required = false) String error,
                                  @PathVariable(value = "action") String action,
                                  @RequestParam(value = "id", required = false) String id,
                                  Model model, Principal principal) throws ParseException {
        model.addAttribute("action", action);
        model.addAttribute("actBtn", "Добавить");
        if (action.equals("edit")) {
            Map<String, String> emplEdit = adminService.getEmployeeById(id);
            model.addAttribute("peopleEdit", emplEdit);
            model.addAttribute("actBtn", "Редактировать");
        }
        model.addAttribute("name", principal.getName());
        model.addAttribute("errorAdd", error != null);
        List<LinkedHashMap<String, String>> employees = adminService.getAllEmployees();
        model.addAttribute("employees", employees);
        model.addAttribute("isEmployee", true);
        model.addAttribute("urlForm", "addEditEmployee");
        return "addEditEmployeeChild";
    }

    @RequestMapping(value = "/addEditEmployee/{action}", method = RequestMethod.POST)
    public String addEditEmployee(@RequestParam(value = "id", required = false) String id,
                                  @PathVariable(value = "action") String action,
                                  @RequestParam(value = "name") String name,
                                  @RequestParam(value = "birthday") String birthday,
                                  @RequestParam(value = "tel") String tel,
                                  @RequestParam(value = "city") String city,
                                  @RequestParam(value = "manager") String manager,
                                  @RequestParam(value = "login") String login,
                                  @RequestParam(value = "password") String password, Model model){
        String idLoad = adminService.loadUserByUsername(login);
        if((idLoad != null && action.equals("add")) ||
                (idLoad != null && !id.equals(idLoad) && action.equals("edit"))) {
            model.addAttribute("error", true);
            model.addAttribute("id", id);
            return "redirect:/getEmployeeForm/" + action;
        }
        if(action.equals("add")) {
            adminService.addEmployee(name, birthday, tel, city, manager, login, password);
        } else {
            adminService.updateEmployee(id, name, birthday, tel, city, manager, login, password);
        }
        model.addAttribute("mainAction", "employees");
        return "redirect:/main";
    }

    @RequestMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable(value = "id") String id, Model model){
        adminService.delete("EMPLOYEE", id);
        model.addAttribute("mainAction", "employees");
        return "redirect:/main";
    }

    @RequestMapping("/courses")
    public ModelAndView courses(Model model){
        List<LinkedHashMap<String, String>> courses = adminService.getAllCourses();
        model.addAttribute("hashList", courses);
        model.addAttribute("urlTable", "getCourseForm");
        model.addAttribute("delete", "deleteCourse");
        return new ModelAndView("table");
    }

    @RequestMapping("/getCourseForm/{action}")
    public String getCourseForm(@PathVariable(value = "action") String action,
                                  @RequestParam(value = "id", required = false) String id,
                                  Model model, Principal principal) throws ParseException {
        model.addAttribute("action", action);
        model.addAttribute("actBtn", "Добавить");
        if (action.equals("edit")) {
            Map<String, String> courseEdit = adminService.getCourseById(id);
            model.addAttribute("courseEdit", courseEdit);
            model.addAttribute("actBtn", "Редактировать");
        }
        model.addAttribute("name", principal.getName());
        return "addEditCourses";
    }

    @RequestMapping(value = "/addEditCourses/{action}", method = RequestMethod.POST)
    public String addEditCourses(@RequestParam(value = "id", required = false) String id,
                                  @PathVariable(value = "action") String action,
                                  @RequestParam(value = "name") String name,
                                  @RequestParam(value = "description") String description,
                                  @RequestParam(value = "lessons") String lessons,
                                  @RequestParam(value = "price") String price,
                                  Model model){
        if(action.equals("add")) {
            adminService.addCourse(name, description, lessons, price);
        } else {
            adminService.updateCourse(id, name, description, lessons, price);
        }
        model.addAttribute("mainAction", "courses");
        return "redirect:/main";
    }

    @RequestMapping("/deleteCourse/{id}")
    public String deleteCourse(@PathVariable(value = "id") String id, Model model){
        adminService.delete("COURSE", id);
        model.addAttribute("mainAction", "courses");
        return "redirect:/main";
    }

    @RequestMapping("/children")
    public ModelAndView children(Model model){
        List<LinkedHashMap<String, String>> children = adminService.getAllChildren();
        model.addAttribute("hashList", children);
        model.addAttribute("urlTable", "getChildForm");
        model.addAttribute("delete", "deleteChild");
        return new ModelAndView("table");
    }

    @RequestMapping("/getChildForm/{action}")
    public String getChildForm(@PathVariable(value = "action") String action,
                                @RequestParam(value = "id", required = false) String id,
                                Model model, Principal principal) {
        model.addAttribute("action", action);
        model.addAttribute("actBtn", "Добавить");
        if (action.equals("edit")) {
            Map<String, String> childEdit = adminService.getChildById(id);
            model.addAttribute("peopleEdit", childEdit);
            model.addAttribute("actBtn", "Редактировать");
        }
        model.addAttribute("name", principal.getName());
        model.addAttribute("isEmployee", false);
        model.addAttribute("urlForm", "addEditChild");
        return "addEditEmployeeChild";
    }

    @RequestMapping(value = "/addEditChild/{action}", method = RequestMethod.POST)
    public String addEditChild(@RequestParam(value = "id", required = false) String id,
                               @PathVariable(value = "action") String action,
                               @RequestParam(value = "name") String name,
                               @RequestParam(value = "birthday") String birthday,
                               @RequestParam(value = "tel") String tel,
                               @RequestParam(value = "city") String city,
                               Model model){
        if(action.equals("add")) {
            adminService.addChild(name, birthday, tel, city);
        } else {
            adminService.updateChild(id, name,  birthday, tel, city);
        }
        model.addAttribute("mainAction", "children");
        return "redirect:/main";
    }

    @RequestMapping("/deleteChild/{id}")
    public String deleteChild(@PathVariable(value = "id") String id, Model model){
        adminService.delete("CHILD", id);
        model.addAttribute("mainAction", "children");
        return "redirect:/main";
    }

    @RequestMapping("/classes")
    public ModelAndView classes(Model model) throws ParseException {
        List<LinkedHashMap<String, String>> classes = adminService.getAllClasses(false);
        model.addAttribute("hashList", classes);
        model.addAttribute("urlTable", "getClassForm");
        model.addAttribute("delete", "deleteClass");
        return new ModelAndView("table");
    }

    @RequestMapping("/getClassForm/{action}")
    public String getClassForm(@PathVariable(value = "action") String action,
                               @RequestParam(value = "id", required = false) String id,
                               Model model, Principal principal) throws ParseException {
        model.addAttribute("action", action);
        model.addAttribute("actBtn", "Добавить");
        if (action.equals("edit")) {
            List<LinkedHashMap<String, String>> classes = adminService.getAllClasses(true);
            Map<String, String> classEdit = classes.stream()
                    .filter(group -> group.get("id").equals(id)).findAny().get();
            String dateTime = classEdit.get("День недели/время");
            String[] dateTimeStrings = dateTime.split("<br>");
            Map<String, String> dateTimeMap = new HashMap<>();
            Arrays.stream(dateTimeStrings).forEach(dateTimeString ->
                    dateTimeMap.put(dateTimeString.split("/")[0].trim(),
                            dateTimeString.split("/")[1].trim()));
            model.addAttribute("childrenGroup", adminService.getChildrenByGroup(classEdit.get("id")));
            model.addAttribute("dateTimeMap", dateTimeMap);
            model.addAttribute("classEdit", classEdit);
            model.addAttribute("actBtn", "Редактировать");
        }
        model.addAttribute("courses", adminService.getAllCourses());
        model.addAttribute("employees", adminService.getAllEmployees());
        ArrayList<String> dayOfWeek = new ArrayList<>();
        dayOfWeek.add("Понедельник");
        dayOfWeek.add("Вторник");
        dayOfWeek.add("Среда");
        dayOfWeek.add("Четверг");
        dayOfWeek.add("Пятница");
        dayOfWeek.add("Суббота");
        dayOfWeek.add("Воскресенье");
        model.addAttribute("dayOfWeek", dayOfWeek);
        model.addAttribute("name", principal.getName());
        model.addAttribute("children", adminService.getAllChildren());
        return "addEditClass";
    }

    @RequestMapping(value = "/addEditClasses/{action}", method = RequestMethod.POST)
    public String addEditClasses(@RequestParam(value = "id", required = false) String id,
                                @PathVariable(value = "action") String action,
                                @RequestParam(value = "name") String name,
                                @RequestParam(value = "startDate") String startDate,
                                @RequestParam(value = "course") String course,
                                @RequestParam(value = "pro") String pro,
                                @RequestParam(value = "assistant") String assistant,
                                @RequestParam(value = "day") List<String> day,
                                @RequestParam(value = "time") List<String> time,
                                @RequestParam(value = "children") List<String> children,
                                Model model) throws ParseException {
        List<String> dates;
        if(action.equals("add")) {
            adminService.addClass(name, startDate, course, pro, assistant, day, time, children);
        } else {
            adminService.updateClass(id, name, startDate, course, pro, assistant, day, time, children);
        }
        model.addAttribute("mainAction", "classes");
        return "redirect:/main";
    }

    @RequestMapping("/deleteClass/{id}")
    public String deleteClass(@PathVariable(value = "id") String id, Model model){
        adminService.delete("CLASS", id);
        model.addAttribute("mainAction", "classes");
        return "redirect:/main";
    }
}
