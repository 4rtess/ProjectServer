package com.example.ProjectServer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@Data
@AllArgsConstructor
@ToString
public class Lesson {
    private String num;
    private String lessonName;
    private String time;
    private String room;
    private String homework;


}
