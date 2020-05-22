package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.model.entities.Course;
import project.services.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

@Controller
public class MainController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private GroupService groupService;
    @Autowired
    private ChildService childService;

    @RequestMapping("/main")
    public String main (@RequestParam(value = "mainAction", defaultValue = "calendar") String mainAction,
                        Model model, Principal principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean role = authentication.getAuthorities().contains(new SimpleGrantedAuthority("USER"));
        model.addAttribute("user", role);
        model.addAttribute("name", principal.getName());
        model.addAttribute("url", mainAction);
        return "main";
    }

    @RequestMapping("/calendar")
    @ResponseBody
    public ModelAndView calendar (Model model, Principal principal)  throws ParseException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        boolean role = authentication.getAuthorities().contains(new SimpleGrantedAuthority("USER"));
        model.addAttribute("user", role);
        if(role) {
            LocalDate localDate = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String startDate = localDate.format(formatter);
            model.addAttribute("startdate", startDate);
            localDate = localDate.plusWeeks(1);
            String endDate = localDate.format(formatter);
            model.addAttribute("enddate", endDate);
            schedule(startDate, endDate,null, model, principal);
        } else {
            model.addAttribute("employees", employeeService.getAllTeachers());
        }
        return new ModelAndView("calendar");
    }

    @RequestMapping("/schedule")
    @ResponseBody
    public ModelAndView schedule (@RequestParam(value = "start") String start,
                                  @RequestParam(value = "end") String end,
                                  @RequestParam(value = "employee", required = false) String employee,
                                  Model model, Principal principal) throws ParseException {
        List<HashMap<String, String>> schedule;
        if(employee==null) {
            schedule = groupService.getScheduleByUsername(principal.getName(), start, end);
        } else {
            schedule = groupService.getScheduleById(employee, start, end);
        }
        model.addAttribute("schedule", schedule);
        return new ModelAndView("schedule");
    }

    @RequestMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        model.addAttribute("error", error != null);
        model.addAttribute("logout", logout != null);
        return "login";
    }

    @RequestMapping(value =  "/logout", method = RequestMethod.GET)
    public void logoutPage (HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }
    }

    @RequestMapping(value =  "/error", method = RequestMethod.GET)
    public String error () {
        return "error";
    }

    @RequestMapping("/infoGroup")
    public String infoGroup(@RequestParam(value = "groupId") String groupId, Model model, Principal principal) {
        model.addAttribute("group", groupService.getClassNameById(groupId));
        model.addAttribute("name", principal.getName());
        Course course = courseService.getCourseByGroup(groupId);
        model.addAttribute("course", course);
        List<HashMap<String, String>> children = childService.getChildrenByGroup(groupId);
        model.addAttribute("children", children);
        return "infoGroup";
    }
}
