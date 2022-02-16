package com.example.ProjectServer.controllers;

import com.example.ProjectServer.parser.VolgeduParser;
import com.example.ProjectServer.entity.Day;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class MainController {

    private VolgeduParser parser;

    public MainController() {
        parser = VolgeduParser.getInstance();
    }


    // GET, чтобы с браузера посмотреть, потом в post
    @GetMapping("/getDays")
    public List<Day> getDays(@RequestParam String username, @RequestParam String password) {
        List<Day> days = parser.getDays(username,password);
        days.forEach(day -> {
            System.out.println(day.getDayOfWeek());
            day.getLessons().forEach(l->System.out.println("    "+l.toString()));
            System.out.println("===========");
        });
        return days;
    }
}
