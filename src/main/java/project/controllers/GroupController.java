package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import project.services.ChildService;
import project.services.CourseService;
import project.services.EmployeeService;
import project.services.GroupService;

import java.security.Principal;
import java.text.ParseException;
import java.util.*;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class GroupController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ChildService childService;

    @RequestMapping("/classes")
    public ModelAndView classes(Model model) throws ParseException {
        List<LinkedHashMap<String, String>> classes = groupService.getAllClasses(false);
        model.addAttribute("hashList", classes);
        model.addAttribute("urlTable", "getClassForm");
        model.addAttribute("delete", "deleteClass");
        return new ModelAndView("tableClasses");
    }

    @RequestMapping("/getClassForm/{action}")
    public String getClassForm(@PathVariable(value = "action") String action,
                               @RequestParam(value = "id", required = false) String id,
                               Model model, Principal principal) throws ParseException {
        model.addAttribute("action", action);
        model.addAttribute("actBtn", "Добавить");
        if (action.equals("edit")) {
            List<LinkedHashMap<String, String>> classes = groupService.getAllClasses(true);
            Map<String, String> classEdit = classes.stream()
                    .filter(group -> group.get("id").equals(id)).findAny().get();
            String dateTime = classEdit.get("День недели/время");
            String[] dateTimeStrings = dateTime.split("<br>");
            Map<String, String> dateTimeMap = new HashMap<>();
            Arrays.stream(dateTimeStrings).forEach(dateTimeString ->
                    dateTimeMap.put(dateTimeString.split("/")[0].trim(),
                            dateTimeString.split("/")[1].trim()));
            model.addAttribute("childrenGroup", childService.getChildrenByGroup(classEdit.get("id")));
            model.addAttribute("dateTimeMap", dateTimeMap);
            model.addAttribute("classEdit", classEdit);
            model.addAttribute("actBtn", "Редактировать");
        }
        model.addAttribute("courses", courseService.getAllCourses());
        model.addAttribute("employees", employeeService.getAllEmployees());
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
        model.addAttribute("children", childService.getAllChildren());
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
        if(action.equals("add")) {
            groupService.addClass(name, startDate, course, pro, assistant, day, time, children);
        } else {
            groupService.updateClass(id, name, startDate, course, pro, assistant, day, time, children);
        }
        model.addAttribute("mainAction", "classes");
        return "redirect:/main";
    }

    @RequestMapping("/deleteClass/{id}")
    public String deleteClass(@PathVariable(value = "id") String id, Model model){
        groupService.deleteClass(id);
        model.addAttribute("mainAction", "classes");
        return "redirect:/main";
    }
}
