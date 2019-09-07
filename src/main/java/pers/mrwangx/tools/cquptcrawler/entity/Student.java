package pers.mrwangx.tools.cquptcrawler.entity;

import com.alibaba.fastjson.JSONObject;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/2/2 14:02
 *****/
public class Student {


    private String sno;         //学号

    private String name;        //姓名

    private String sex;         //性别

    private String nation;      //民族

    private String birthday;    //生日

    private String rxrq;        //入学日期

    private String grade;       //年级

    private String classid;     //班级代号

    private String collegename; //学院名

    private String collegeid;   //学院号

    private String majorname;   //专业名

    private String majorid;     //专业号

    @Override
    public String toString() {
        return "Student{" +
                "学号='" + sno + '\'' +
                ", 姓名='" + name + '\'' +
                ", 性别='" + sex + '\'' +
                ", 民族='" + nation + '\'' +
                ", 生日='" + birthday + '\'' +
                ", 入学日期='" + rxrq + '\'' +
                ", 年级='" + grade + '\'' +
                ", 班级号='" + classid + '\'' +
                ", 学院名='" + collegename + '\'' +
                ", 学院号='" + collegeid + '\'' +
                ", 专业名='" + majorname + '\'' +
                ", 专业号='" + majorid + '\'' +
                '}';
    }

    public Student() {}

    public Student(String sno, String name, String sex, String nation, String birthday, String rxrq, String grade, String classid, String collegename, String collegeid, String majorname, String majorid) {
        this.sno = sno;
        this.name = name;
        this.sex = sex;
        this.nation = nation;
        this.birthday = birthday;
        this.rxrq = rxrq;
        this.grade = grade;
        this.classid = classid;
        this.collegename = collegename;
        this.collegeid = collegeid;
        this.majorname = majorname;
        this.majorid = majorid;
    }

    public Student(JSONObject json) {
        setSno(json.getString("xh"));
        setName(json.getString("xm"));
        setSex(json.getString("xb"));
        setNation(json.getString("mz"));
        setBirthday(json.getString("csrq"));
        setRxrq(json.getString("rxrq"));
        setGrade(json.getString("nj"));
        setClassid(json.getString("bj"));
        setCollegename(json.getString("yxm"));
        setCollegeid(json.getString("yxh"));
        setMajorname(json.getString("zym"));
        setMajorid(json.getString("zyh"));
    }


    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getNation() {
        return nation;
    }

    public void setNation(String nation) {
        this.nation = nation;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getRxrq() {
        return rxrq;
    }

    public void setRxrq(String rxrq) {
        this.rxrq = rxrq;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getCollegename() {
        return collegename;
    }

    public void setCollegename(String collegename) {
        this.collegename = collegename;
    }

    public String getCollegeid() {
        return collegeid;
    }

    public void setCollegeid(String collegeid) {
        this.collegeid = collegeid;
    }

    public String getMajorname() {
        return majorname;
    }

    public void setMajorname(String majorname) {
        this.majorname = majorname;
    }

    public String getMajorid() {
        return majorid;
    }

    public void setMajorid(String majorid) {
        this.majorid = majorid;
    }
}
