package com.example.ProjectServer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Lesson {
    private String num;
    private String lessonName;
    private String time;
    private String room;
    private String homework;


}
