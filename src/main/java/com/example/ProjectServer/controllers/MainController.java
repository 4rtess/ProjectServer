package com.example.ProjectServer.controllers;

import com.example.ProjectServer.parser.VolgeduParser;
import com.example.ProjectServer.entity.Day;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Tag(name = "Parser controller.",
     description = "Get timetable and maybe something else in perspective.")
@Controller
public class MainController {


    // BEGIN OF THE DOCUMENTATION
    @Operation(summary = "Get timetable",
               description = "Get timetable and return as json")
    //END OF THE DOCUMENTATION
    @GetMapping("/getDays")
    public String getDays(@CookieValue(name = "username", defaultValue = "", required = false) String username,
                          @CookieValue(name = "password", defaultValue = "", required = false) String password,
                          Map<String, Object> model) {
        System.out.printf("username=%s  |  password=%s\n", username,password);
        if(username.isEmpty() || password.isEmpty())
            return "day";
        VolgeduParser parser = new VolgeduParser();
        List<Day> days = parser.getDays(username, password);
        model.put("days", days);
        return "day";
    }

    @PostMapping("/getDays")
    public String getDaysPost(@RequestParam String username, @RequestParam String password, Map<String, Object> model) {
        System.out.println(String.format("-->%s\n-->%s",username,password));
        if(!username.equals("") && !password.equals("")) {
            VolgeduParser parser = new VolgeduParser();
            List<Day> days = parser.getDays(username, password);
            model.put("days", days);
            System.out.println("--> PUT MODELS");
        }
        return "day";
    }
}
