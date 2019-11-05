package pers.mrwangx.tools.cquptcrawler.entity;

import com.alibaba.fastjson.JSONObject;
import pers.mrwangx.commons.tool.display.annotation.Display;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/2/2 14:02
 *****/
public class Student {

    @Display("学号")
    private String sno;         //学号
    @Display("姓名")
    private String name;        //姓名
    @Display("性别")
    private String sex;         //性别
    @Display("民族")
    private String nation;      //民族
    @Display("生日")
    private String birthday;    //生日
    @Display("入学日期")
    private String rxrq;        //入学日期
    @Display("年级")
    private String grade;       //年级
    @Display("班级代号")
    private String classid;     //班级代号
    @Display("学院名")
    private String collegename; //学院名
    @Display("学院号")
    private String collegeid;   //学院号
    @Display("专业名")
    private String majorname;   //专业名
    @Display(value = "专业号")
    private String majorid;     //专业号

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
