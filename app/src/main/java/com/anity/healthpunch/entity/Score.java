package com.anity.healthpunch.entity;

import lombok.Data;

@Data
public class Score {
    //课程属性
    private String curriculumAttributes;

    //课程名称
    private String courseName;

    //课程性质
    private String courseNature;

    //正常考试
    private String examinationNature;

    //课程编号
    private String kcbh;

    //学分
    private String credit;

    //
    private String cj0708id;

    //分数
    private String fraction;
}
