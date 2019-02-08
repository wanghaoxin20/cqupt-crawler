package pers.mrwangx.cquptcrawler.pojo;

import java.util.ArrayList;
import java.util.List;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/2/2 17:14
 *****/
public class Course {

    private String no;          //该节课编号
    private String id_name;     //课程号和名字
    private String place;       //上课地点
    private List<Integer> weeks;       //上课的周数
    private String length;      //一节课的长度
    private String teacher;     //上课老师
    private String type;        //选修或者必修
    private String credit;      //学分
    private int weekday;        //星期几
    private int coursenum;      //第几节课

    @Override
    public String toString() {
        return "Course{" +
                "no='" + no + '\'' +
                ", id_name='" + id_name + '\'' +
                ", place='" + place + '\'' +
                ", weeks=" + getWeeksString() +
                ", length='" + length + '\'' +
                ", teacher='" + teacher + '\'' +
                ", type='" + type + '\'' +
                ", credit='" + credit + '\'' +
                ", weekday=" + weekday +
                ", coursenum=" + coursenum +
                '}';
    }

    public Course() {}

    public Course(String no, String id_name, String place, String weeks, String length, String teacher, String type, String credit, int weekday, int coursenum) {
        this.no = no;
        this.id_name = id_name;
        this.place = place;
        setWeeks(weeks);
        setLength(length);
        this.teacher = teacher;
        this.type = type;
        this.credit = credit;
        this.weekday = weekday;
        this.coursenum = coursenum;
    }

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getId_name() {
        return id_name;
    }

    public void setId_name(String id_name) {
        this.id_name = id_name;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public List<Integer> getWeeksList() {
        return weeks;
    }

    public String getWeeksString() {
        StringBuffer sb = new StringBuffer();
        for (Integer w : weeks) {
            sb.append(w + ",");
        }
        return sb.toString();
    }

    public void setWeeks(String weeks) {
        this.weeks = new ArrayList<>();
        String[] wks = weeks.replaceAll("单|双|周", "").split(",");
        for (String w : wks) {
            String[] num = w.split("-");
            if (num.length > 1) {
                int start = Integer.parseInt(num[0]);
                int end = Integer.parseInt(num[1]);
                for (int i = start; i <= end; i++) {
                    this.weeks.add(i);
                }
            } else {
                this.weeks.add(Integer.parseInt(num[0]));
            }
        }
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        if (length.matches("\\s+|")) {
            this.length = "2节连上";
        } else {
            this.length = length;
        }
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    public int getCoursenum() {
        return coursenum;
    }

    public void setCoursenum(int coursenum) {
        this.coursenum = coursenum;
    }
}
