package pers.mrwangx.tools.cquptcrawler.entity;

import pers.mrwangx.tools.cquptcrawler.annotation.Display;

import java.util.ArrayList;
import java.util.List;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/2/2 17:14
 *****/
public class Course {

    @Display("课程标识")
    private String courseNo;          //该节课编号
    @Display("课程名")
    private String id_name;     //课程号和名字
    @Display("上课地点")
    private String place;       //上课地点
    private List<Integer> weeks;       //上课的周数
    @Display("上课节数")
    private String length;      //一节课的长度
    @Display("上课老师")
    private String teacher;     //上课老师
    @Display("课程类型")
    private String type;        //选修或者必修
    @Display("学分")
    private String credit;      //学分
    @Display(value = "星期", display = false)
    private int weekday;        //星期几
    @Display(value = "第几节课", display = false)
    private int coursenum;      //第几节课

    @Override
    public String toString() {
        return "Course{" +
                "no='" + courseNo + '\'' +
                ", id_name='" + id_name + '\'' +
                ", place='" + place + '\'' +
                ", weeks=" + getWeeks() +
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
        this.courseNo = no;
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

    public String getCourseNo() {
        return courseNo;
    }

    public void setCourseNo(String courseNo) {
        this.courseNo = courseNo;
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

    @Display("上课周数")
    public String getWeeks() {
        StringBuffer sb = new StringBuffer();
        for (Integer w : weeks) {
            sb.append(w + ",");
        }
        return sb.toString();
    }

    public void setWeeks(String weeks) {
        this.weeks = new ArrayList<>();
        int flag = 0;
        if (weeks.contains("单")) { //判断单双周
            flag = 1;
        } else if (weeks.contains("双")) {
            flag = 2;
        }
        String[] wks = weeks.replaceAll("单|双|周", "").split(",");
        for (String w : wks) {
            String[] num = w.split("-");
            if (num.length > 1) {
                int start = Integer.parseInt(num[0]);
                int end = Integer.parseInt(num[1]);
                if (flag == 0) {
                    for (int i = start; i <= end; i++) {
                        this.weeks.add(i);
                    }
                } else if (flag == 1) {
                    for (int i = start; i <= end; i++) {
                        if (i % 2 == 1) {
                            this.weeks.add(i);
                        }
                    }
                } else if (flag == 2) {
                    for (int i = start; i <= end; i++) {
                        if (i % 2 == 0) {
                            this.weeks.add(i);
                        }
                    }
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



    public static class Student {
        @Display("学号")
        private String sno;         //学号
        @Display("姓名")
        private String name;        //姓名
        @Display("性别")
        private String sex;         //性别
        @Display("班级")
        private String classid;     //班级
        @Display("专业号")
        private String majorid;     //专业号
        @Display("专业名")
        private String majorname;   //专业名
        @Display("学院名")
        private String collegename; //学院名
        @Display("年级")
        private String grade;       //年级
        @Display("学籍状态")
        private String xjzt;        //学籍状态
        @Display("选课状态")
        private String xkzt;        //选课状态
        @Display("课程类别")
        private String kclb;        //课程类别

        public Student() {
        }

        public Student(String sno, String name, String sex, String classid, String majorid, String majorname, String collegename, String grade, String xjzt, String xkzt, String kclb) {
            this.sno = sno;
            this.name = name;
            this.sex = sex;
            this.classid = classid;
            this.majorid = majorid;
            this.majorname = majorname;
            this.collegename = collegename;
            this.grade = grade;
            this.xjzt = xjzt;
            this.xkzt = xkzt;
            this.kclb = kclb;
        }

        @Override
        public String toString() {
            return "Student{" +
                    "sno='" + sno + '\'' +
                    ", name='" + name + '\'' +
                    ", sex='" + sex + '\'' +
                    ", classid='" + classid + '\'' +
                    ", majorid='" + majorid + '\'' +
                    ", majorname='" + majorname + '\'' +
                    ", collegename='" + collegename + '\'' +
                    ", grade='" + grade + '\'' +
                    ", xjzt='" + xjzt + '\'' +
                    ", xkzt='" + xkzt + '\'' +
                    ", kclb='" + kclb + '\'' +
                    '}';
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

        public String getClassid() {
            return classid;
        }

        public void setClassid(String classid) {
            this.classid = classid;
        }

        public String getMajorid() {
            return majorid;
        }

        public void setMajorid(String majorid) {
            this.majorid = majorid;
        }

        public String getMajorname() {
            return majorname;
        }

        public void setMajorname(String majorname) {
            this.majorname = majorname;
        }

        public String getCollegename() {
            return collegename;
        }

        public void setCollegename(String collegename) {
            this.collegename = collegename;
        }

        public String getGrade() {
            return grade;
        }

        public void setGrade(String grade) {
            this.grade = grade;
        }

        public String getXjzt() {
            return xjzt;
        }

        public void setXjzt(String xjzt) {
            this.xjzt = xjzt;
        }

        public String getXkzt() {
            return xkzt;
        }

        public void setXkzt(String xkzt) {
            this.xkzt = xkzt;
        }

        public String getKclb() {
            return kclb;
        }

        public void setKclb(String kclb) {
            this.kclb = kclb;
        }
    }
}
