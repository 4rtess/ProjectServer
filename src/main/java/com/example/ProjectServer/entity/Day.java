package com.example.ProjectServer.entity;

import java.util.List;

public class Day {
    private String dayOfWeek;
    private List<Lesson> lessons;

    public Day(String dayOfWeek,List<Lesson> lessons) {
        this.dayOfWeek=dayOfWeek;
        this.lessons = lessons;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
