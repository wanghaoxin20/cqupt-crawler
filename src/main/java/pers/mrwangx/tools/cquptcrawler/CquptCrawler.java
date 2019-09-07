package pers.mrwangx.tools.cquptcrawler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import pers.mrwangx.tools.cquptcrawler.entity.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * \* Author: MrWangx
 * \* Date: 2019/9/4
 * \* Time: 22:24
 * \* Description:
 **/
public class CquptCrawler {

    public static final int schoolweeks = 20; //一共的周数
    public static final int coursenums = 6;  //每天一共的课节数
    public static final int weekdays = 7;  //每周天数

    /**
     * 搜索学生信息
     *
     * @param keyword 搜寻关键词 学号或者姓名
     * @return 学生信息list
     * @description 获取学生信息
     */
    public static List<Student> searchStudent(String keyword, URLConfig netType) {
        List<Student> list = null;
        try {
            Document doc = Jsoup.connect(netType.getUrl() + URLConfig.STUMES.getUrl()).data("searchKey", keyword).get();
            JSONObject resdata = JSONObject.parseObject(doc.body().text());
            if (resdata.getInteger("code") == 0) {
                list = new ArrayList<>();
                JSONArray stuarray = resdata.getJSONArray("returnData");
                for (int i = 0; i < stuarray.size(); i++) {
                    list.add(new Student(stuarray.getJSONObject(i)));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 搜索学生课程信息
     *
     * @param sno
     * @param netType
     * @return
     */
    public static StuCourses searchCourse(int sno, URLConfig netType) {
        StuCourses stuCourses = null;
        try {
            ListCourse[][] listCourses = new ListCourse[coursenums][schoolweeks];
            Document doc = Jsoup.connect(netType.getUrl() + URLConfig.COURSE.getUrl()).data("xh", Integer.toString(sno)).get();
            Elements table = doc.select("table"); //获取课表table
            Elements trs = table.select("tr"); //获取所有一行

            trs.remove(6); //移除下午间隙列
            trs.remove(3); //移除中午间歇列
            trs.remove(0); //移除星期

            for (int i = 0; i < coursenums; i++) { //课节数

                Element tr = trs.get(i);    //获取一行tr
                Elements tds = tr.select("td"); //获取该行所有单元格

                for (int j = 1; j <= weekdays; j++) { //星期
                    ListCourse listCourse = new ListCourse();
                    Elements divs = tds.get(j).select("div"); //获取单元格内所有div
                    int weekday = j - 1, coursenum = i;
                    if (!(divs == null || divs.isEmpty())) {
                        for (int divnum = 0; divnum < divs.size(); divnum++) {
                            List<TextNode> list = divs.get(divnum).textNodes();
                            Element div = divs.get(divnum);

                            //获取课程信息
                            String[] strs = div.select("span").text().split(" ");
                            String no = list.get(0).text();          //该节课编号
                            String id_name = list.get(1).text();     //课程号和名字
                            String place = list.get(2).text();       //上课地点
                            String weeks = list.get(3).text();       //上课的周数
                            String length = div.select("font").text();      //一节课的长度
                            String teacher = strs[0];     //上课老师
                            String type = strs[1];        //选修或者必修
                            String credit = strs[2];      //学分
                            Course course = new Course(no, id_name, place, weeks, length, teacher, type, credit, weekday, coursenum);

                            listCourse.addCourse(course);
                        }
                    }
                    listCourses[coursenum][weekday] = listCourse;
                }
            }
            stuCourses = new StuCourses(listCourses);
        } catch (IOException e) {
            e.printStackTrace();
            stuCourses = null;
        }
        return stuCourses;
    }

    /**
     * 获取当前周数
     *
     * @param netType 网络类型 内网或者外网
     * @return
     */
    public static Integer currentSchoolWeek(URLConfig netType) {
        String url = netType.getUrl() + URLConfig.COURSE.getUrl();
        Integer week = null;
        try {
            Document doc = Jsoup.connect(url).data("xh", "2016211995").get();
            Element ele = doc.select("#head div").get(2);
            String text = ele.text();
            week = Integer.parseInt(text.substring(text.indexOf("第") + 1, text.indexOf("周")).trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return week;
    }


    /**
     * 显示课表
     *
     * @param course
     * @return
     */
    public static String courseDisplay(Course course) {
        if (course == null) {
            return "你没有课噢!";
        }
        String lineSeparator = System.lineSeparator();
        return "课程名:" + course.getId_name() + lineSeparator +
                "老师:" + course.getTeacher() + lineSeparator +
                "学分:" + course.getCredit() + lineSeparator +
                course.getPlace() + lineSeparator +
                "上课节数:" + course.getLength() + lineSeparator +
                "课程类型:" + course.getType()
                ;

    }

    /**
     * 显示多节课表
     * @param listCourse
     * @return
     */
    public static String listCourseDisplay(ListCourse listCourse) {
        if (listCourse == null || listCourse.isEmpty()) {
            return "你没有课噢";
        }
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        List<Course> courses = listCourse.getCourses();
        for (int i = 0; i < courses.size(); i++) {
            if (i == courses.size() - 1) {
                builder.append(courseDisplay(courses.get(i)));
            } else {
                builder.append(courseDisplay(courses.get(i)) + lineSeparator);
                builder.append("--------------------------" + lineSeparator);
            }
        }
        return builder.toString();
    }

    /**
     *
     * @param student
     * @return
     */
    public static String studentDisplay(Student student) {
        if (student == null) {
            return null;
        }
        String lineSeparator = System.lineSeparator();
        return
                "学号:" + student.getSno() + lineSeparator +
                        "姓名:" + student.getName() + lineSeparator +
                        "性别:" + student.getSex() + lineSeparator +
                        "民族:" + student.getNation() + lineSeparator +
                        "生日:" + student.getBirthday() + lineSeparator +
                        "入学日期:" + student.getRxrq() + lineSeparator +
                        "年级:" + student.getGrade() + lineSeparator +
                        "班级号:" + student.getClassid() + lineSeparator +
                        "学院名:" + student.getCollegename() + lineSeparator +
                        "学院号:" + student.getCollegeid() + lineSeparator +
                        "专业名:" + student.getMajorname() + lineSeparator +
                        "专业号:" + student.getMajorid()
                ;
    }


}
