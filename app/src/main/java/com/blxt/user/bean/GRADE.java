package com.blxt.user.bean;


/**
 * @brief：权限定义
 * @author: Zhang
 * @date: 2019/6/6 - 14:04
 * @note Created by com.blxt.securitybox.model.
 */
public enum GRADE {

    /** 临时，游客 */
    TMP( 0 )
    /** 公开*/
    ,PUBLIC(1)
    /** 敏感*/
    ,SENSITIVITY(2)
    /** 秘密*/
    ,SECRET(3)
    /** 机密*/
    ,CONFIDENTIAL(4)
    /** 绝密*/
    ,TOP_SECRET(5)
    /** 超级管理员,系统账号,谨慎开放 */
    ,SUP_ADMIN(6);


    /**
     * 构造
     * @param grade
     */
    GRADE(int grade) {
        this.grade = grade;
        switch (grade){
            case 1:
                gradeStr = "公开";
                break;
            case 2:
                gradeStr = "敏感";
                break;
            case 3:
                gradeStr = "秘密";
                break;
            case 4:
                gradeStr = "机密";
                break;
            case 5:
                gradeStr = "绝密";
                break;
            case 6:
                gradeStr = "超级管理员";
                break;
            default:
                gradeStr = "临时";
                break;
        }
    }

    /** 权限等级 */
    public int grade;
    /** 等级描述*/
    public String gradeStr;

    /**
     * 根据等级id，格式化描述
     * @param grade
     * @return
     */
    public static GRADE getGRADE(int grade){
        switch(grade){
            case 1:return PUBLIC;
            case 2:return SENSITIVITY;
            case 3:return SECRET;
            case 4:return CONFIDENTIAL;
            case 5:return TOP_SECRET;
            case 6:return SUP_ADMIN;
            default:return TMP;

        }
    }
}



