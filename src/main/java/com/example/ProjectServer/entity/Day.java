package com.example.ProjectServer.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@AllArgsConstructor
@ToString
public class Day {
    private String dayOfWeek;
    private List<Lesson> lessons;

}
