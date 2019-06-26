package com.lister.Project.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.lister.Project.domain.Employee;
import com.lister.Project.service.EmployeeService;

/**
 * Handles the REST calls.
 */
@Controller
public class EmployeeController {
    private static final String outputDirectory = "C://reports/";

    @Autowired
    EmployeeService es;

    /**
     * @param emp
     * @param model
     * @return
     */
    @RequestMapping("/save")
    public String show(@ModelAttribute Employee emp, Map<String, Object> model) {
        es.addemployee(emp);
        List<Employee> le = es.getEmployeeList();
        model.put("Employees", le);
        return "employeedtls";
    }

    /**
     * @param id
     * @param model
     * @return
     */
    @RequestMapping("/delete")
    public String remove(@RequestParam int id, Map<String, Object> model) {
        es.removeEmployeeByID(id);
        System.out.println("Deleted");
        List<Employee> le = es.getEmployeeList();
        model.put("Employees", le);
        return "employeedtls";
    }

    /**
     * @param model
     * @return
     * @throws ReportSDKException
     * @throws IOException
     */
    @RequestMapping("/generate")
    public String generate(Model model) throws ReportSDKException, IOException {
        if (es.generate()) {
            model.addAttribute("message", "Report published succesfully");
        } else {
            model.addAttribute("message", "The output report file must be open in some other application.");
            List<Employee> le = es.getEmployeeList();
            model.addAttribute("Employees", le);
            return "employeedtls";
        }

        File directory = new File(outputDirectory);
        File lf[] = directory.listFiles();
        Arrays.sort(lf, (a, b) -> Long.compare(a.lastModified(), b.lastModified()));
        List<String> fname = new ArrayList<>();
        for (File oListItem : lf) {
            if (!oListItem.isDirectory()) {
                fname.add(oListItem.getName());
                System.out.println(oListItem.getName());
            }
        }
        model.addAttribute("reports", fname);
        return "reportlist";
    }
}
