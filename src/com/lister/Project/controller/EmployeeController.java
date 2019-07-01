package com.lister.Project.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.lister.Project.domain.FilterCondition;
import com.lister.Project.domain.InvalidFiltersException;
import com.lister.Project.domain.SortCondition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        List<Employee> le = es.getEmployeeList();
        model.put("Employees", le);
        return "employeedtls";
    }

    /**
     * Get the table view of the database.
     *
     * @param model
     * @return
     */
    @RequestMapping("/view")
    public String viewTable(Map<String, Object> model) {
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
    @RequestMapping(value = "/generate",
            params = {"col", "sortBy", "filterCol", "condition"})
    public String generate(Model model,
                           @RequestParam("col") String col,
                           @RequestParam("sortBy") String sortBy,
                           @RequestParam("filterCol") List<String> filterCol,
                           @RequestParam("condition") List<String> condition
    ) throws ReportSDKException, IOException {
        SortCondition sortCondition = new SortCondition(col, sortBy);
        FilterCondition filterCondition = null;
        try {
            filterCondition = new FilterCondition(filterCol, condition);
        } catch (InvalidFiltersException e) {
            model.addAttribute("message",
                    "Ensure that you have entered BOTH filter column and filter condition for each filter.");
            List<Employee> le = es.getEmployeeList();
            model.addAttribute("Employees", le);
            return "employeedtls";
        }

        if (es.generate(sortCondition, filterCondition)) {
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

    /**
     * @param model
     * @return
     */
//    @RequestMapping(value = "/filter",
//            params = {"filterCol", "condition"})
//    @ResponseBody
//    public List<Employee> filter(Map<String, Object> model,
//                                 @RequestParam("filterCol") List<String> filterCol,
//                                 @RequestParam("condition") List<String> condition) {
//        List<Employee> le = es.getEmployeeList(condition);
//        model.put("Employees", le);
//        //return "employeedtls";
//        for (String a: condition ) {
//            System.out.println(a);
//        }
//        return le;
//    }

}
