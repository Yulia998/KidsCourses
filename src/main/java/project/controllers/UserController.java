package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import project.services.UserService;

import java.security.Principal;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("/infoGroup")
    public String infoGroup(@RequestParam(value = "groupId") String groupId, Model model, Principal principal) {
        model.addAttribute("group", userService.getClassNameById(groupId));
        model.addAttribute("name", principal.getName());
        Map<String, String> course = userService.getCourseByGroup(groupId);
        model.addAttribute("course", course);
        List<HashMap<String, String>> children = userService.getChildrenByGroup(groupId);
        model.addAttribute("children", children);
        return "infoGroup";
    }
}
