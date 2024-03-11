package com.anity.healthpunch.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Course implements Serializable {
    private String buildingName;

    private String buttonCode;

    private String classTime;

    private String classWeek;

    private String classWeekDetails;

    private String classroomName;

    private String courseName;

    private Integer coursesNote;

    private String endTIme;

    private String fzmc;

    private String isRepeatCode;

    private String jx0404id;

    private String jx0408id;

    private String ktmc;

    private String location;

    private String startTime;

    private String teacherName;

    private String weekDay;

    private String weekNoteDetail;

    private Integer xkrs;

    @Override
    public String toString() {
        return  "课程名称：" + courseName + "\n" +
                "教师：" + teacherName + "\n" +
                "上课班级：" + ktmc + "\n" +
                "教室位置：" + classroomName + "\n" +
                "时间：" + startTime + " ~ " + endTIme;
    }
}
