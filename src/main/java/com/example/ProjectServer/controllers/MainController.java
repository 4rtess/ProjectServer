package com.example.ProjectServer.controllers;

import com.example.ProjectServer.parser.VolgeduParser;
import com.example.ProjectServer.entity.Day;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Parser controller.",
     description = "Get timetable and maybe something else in perspective.")
@RestController
public class MainController {

    private VolgeduParser parser;

    public MainController() {
        parser = VolgeduParser.getInstance();
    }


    // BEGIN OF THE DOCUMENTATION
    @Operation(summary = "Get timetable",
               description = "Get timetable and return as json")
    //END OF THE DOCUMENTATION
    @GetMapping("/getDays")
    public List<Day> getDays(@RequestParam String username, @RequestParam String password) {
        List<Day> days = parser.getDays(username,password);
        return days;
    }

}
