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
import project.model.entities.Child;
import project.services.ChildService;

import java.security.Principal;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class ChildController {
    @Autowired
    private ChildService childService;

    @RequestMapping("/children")
    public ModelAndView children(Model model){
        List<Child> children = childService.getAllChildren();
        model.addAttribute("list", children);
        model.addAttribute("urlTable", "getChildForm");
        model.addAttribute("delete", "deleteChild");
        return new ModelAndView("tableEmpChild");
    }

    @RequestMapping("/getChildForm/{action}")
    public String getChildForm(@PathVariable(value = "action") String action,
                               @RequestParam(value = "id", required = false) String id,
                               Model model, Principal principal) {
        model.addAttribute("action", action);
        model.addAttribute("actBtn", "Добавить");
        if (action.equals("edit")) {
            Child childEdit = childService.getChildById(id);
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
            childService.addChild(name, birthday, tel, city);
        } else {
            childService.updateChild(id, name,  birthday, tel, city);
        }
        model.addAttribute("mainAction", "children");
        return "redirect:/main";
    }

    @RequestMapping("/deleteChild/{id}")
    public String deleteChild(@PathVariable(value = "id") String id, Model model){
        childService.deleteChild(id);
        model.addAttribute("mainAction", "children");
        return "redirect:/main";
    }
}
