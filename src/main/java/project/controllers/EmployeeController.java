package project.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import project.model.entities.Employee;
import project.services.EmployeeService;

import java.security.Principal;
import java.text.ParseException;
import java.util.List;

@Controller
@PreAuthorize("hasAuthority('ADMIN')")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @RequestMapping("/employees")
    @ResponseBody
    public ModelAndView getEmployees(Model model) {
        List<Employee> employees = employeeService.getAllEmployees();
        model.addAttribute("list", employees);
        model.addAttribute("urlTable", "getEmployeeForm");
        model.addAttribute("delete", "deleteEmployee");
        return new ModelAndView("tableEmpChild");
    }

    @RequestMapping("/getEmployeeForm/{action}")
    public String getEmployeeForm(@RequestParam(value = "error", required = false) String error,
                                  @PathVariable(value = "action") String action,
                                  @RequestParam(value = "id", required = false) String id,
                                  Model model, Principal principal) throws ParseException {
        model.addAttribute("action", action);
        model.addAttribute("actBtn", "Добавить");
        if (action.equals("edit")) {
            Employee emplEdit = employeeService.getEmployeeById(id);
            model.addAttribute("peopleEdit", emplEdit);
            model.addAttribute("actBtn", "Редактировать");
        }
        model.addAttribute("name", principal.getName());
        model.addAttribute("errorAdd", error != null);
        List<Employee> employees = employeeService.getAllEmployees();
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
        String idLoad = employeeService.getEmplId(login);
        if((idLoad != null && action.equals("add")) ||
                (idLoad != null && !id.equals(idLoad) && action.equals("edit"))) {
            model.addAttribute("error", true);
            model.addAttribute("id", id);
            return "redirect:/getEmployeeForm/" + action;
        }
        if(action.equals("add")) {
            employeeService.addEmployee(name, birthday, tel, city, manager, login, password);
        } else {
            employeeService.updateEmployee(id, name, birthday, tel, city, manager, login, password);
        }
        model.addAttribute("mainAction", "employees");
        return "redirect:/main";
    }

    @RequestMapping("/deleteEmployee/{id}")
    public String deleteEmployee(@PathVariable(value = "id") String id, Model model){
        employeeService.deleteEmployee(id);
        model.addAttribute("mainAction", "employees");
        return "redirect:/main";
    }
}
