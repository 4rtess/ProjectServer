package com.example.ProjectServer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class Day {
    private String dayOfWeek;
    private List<Lesson> lessons;

}
