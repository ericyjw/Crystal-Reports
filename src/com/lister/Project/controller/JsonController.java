package com.lister.Project.controller;

import com.crystaldecisions.sdk.occa.report.lib.ReportSDKException;
import com.lister.Project.domain.Employee;
import com.lister.Project.domain.FilterCondition;
import com.lister.Project.domain.InvalidFiltersException;
import com.lister.Project.domain.SortCondition;
import com.lister.Project.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import sun.awt.geom.AreaOp;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Handles the REST calls.
 */
@RestController
public class JsonController {
    private static final String outputDirectory = "C://reports/";

    @Autowired
    EmployeeService es;

    @RequestMapping(value = "/filter",
            params = {"filterCol", "condition"})
    public List<Employee> filter(Map<String, Object> model,
                                 @RequestParam("filterCol") List<String> filterCol,
                                 @RequestParam("condition") List<String> condition) {
        List<Employee> le = es.getEmployeeList(condition);
        model.put("Employees", le);
        //return "employeedtls";
        for (String a: condition ) {
            System.out.println(a);
        }
        return le;
    }

    @RequestMapping(value = "/get", method = RequestMethod.POST)
    public String filter(HttpServletRequest request) {
        String param = request.getParameter("condition");
        System.out.println(param);
        return param;
    }

}
