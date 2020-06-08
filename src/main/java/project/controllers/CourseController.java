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
import project.model.entities.Course;
import project.services.CourseService;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class CourseController {
    @Autowired
    private CourseService courseService;

    @RequestMapping("/courses")
    public ModelAndView courses(Model model){
        List<Course> courses = courseService.getAllCourses();
        model.addAttribute("courses", courses);
        return new ModelAndView("tableCourse");
    }

    @RequestMapping("/getCourseForm/{action}")
    public String getCourseForm(@PathVariable(value = "action") String action,
                                @RequestParam(value = "id", required = false) String id,
                                Model model, Principal principal) throws ParseException {
        model.addAttribute("action", action);
        model.addAttribute("actBtn", "Добавить");
        if (action.equals("edit")) {
            Course courseEdit = courseService.getCourseById(id);
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
            courseService.addCourse(name, description, lessons, price);
        } else {
            courseService.updateCourse(id, name, description, lessons, price);
        }
        model.addAttribute("mainAction", "courses");
        return "redirect:/main";
    }

    @RequestMapping("/deleteCourse/{id}")
    public String deleteCourse(@PathVariable(value = "id") String id, Model model){
        courseService.deleteCourse(id);
        model.addAttribute("mainAction", "courses");
        return "redirect:/main";
    }
}
