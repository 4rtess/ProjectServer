package com.example.ProjectServer.entity;

public class Lesson {
    private String num;
    private String lessonName;
    private String time;
    private String room;
    private String homework;

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getLessonName() {
        return lessonName;
    }

    public void setLessonName(String lessonName) {
        this.lessonName = lessonName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getHomework() {
        return homework;
    }

    public void setHomework(String homework) {
        this.homework = homework;
    }


    public Lesson(String num, String lessonName, String time, String room, String homework) {
        this.num = num;
        this.lessonName = lessonName;
        this.time = time;
        this.room = room;
        this.homework = homework;
    }

    @Override
    public String toString() {
        return "Lesson{" +
                "num='" + num + '\'' +
                ", lessonName='" + lessonName + '\'' +
                ", time='" + time + '\'' +
                ", room='" + room + '\'' +
                ", homework='" + homework + '\'' +
                '}';
    }
}
