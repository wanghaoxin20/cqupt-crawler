package pers.mrwangx.cquptcrawler.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import pers.mrwangx.cquptcrawler.pojo.BaseURL;
import pers.mrwangx.cquptcrawler.pojo.Course;
import pers.mrwangx.cquptcrawler.util.StudentUtil;

import java.io.IOException;
import java.util.List;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/2/2 20:50
 *****/
public class StuCourses {

    public static final int schoolweeks = 20;
    public static final int coursenums = 6;  //每天一共的课节数
    public static final int weekdays = 7;   //每周天数
    public static final String url = "/kebiao/kb_stu.php?xh=";

    private Course[][][] courses;
    private BaseURL baseURL;

    public StuCourses(String sno, BaseURL baseURL) {
        try {
            setBaseURL(baseURL);
            setCourses(sno);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * @param schoolweek 行课周数
     * @param weekday    星期
     * @param coursenum  第几节课
     * @return
     */
    public Course getCourse(int schoolweek, int weekday, int coursenum) {
        if (!((schoolweek > 0 && schoolweek <= schoolweeks) && (weekday > 0 && weekday <= weekdays) && (coursenum > 0 && coursenum <= coursenums))) {
            System.out.println("输入参数错误!");
            return null;
        }
        return this.courses[schoolweek - 1][coursenum - 1][weekday - 1];
    }

    /**
     * @description 设置课表
     * @param sno 学号
     * @throws IOException
     */
    public void setCourses(String sno) throws IOException {
        this.courses = new Course[schoolweeks][coursenums][weekdays];
        Document doc = Jsoup.connect(baseURL.getUrl() + url).data("xh", sno).get();
        Elements table = doc.select("table"); //获取课表table
        Elements trs = table.select("tr"); //获取所有一行


        trs.remove(6); //移除下午间隙列
        trs.remove(3); //移除中午间歇列
        trs.remove(0); //移除星期

        for (int i = 0; i < coursenums; i++) { //课节数

            Element tr = trs.get(i);    //获取一行tr
            Elements tds = tr.select("td"); //获取该行所有单元格

            for (int j = 1; j <= weekdays; j++) { //星期
                Elements divs = tds.get(j).select("div"); //获取单元格内所有div
                if (!(divs == null || divs.isEmpty())) {
                    for (int divnum = 0; divnum < divs.size(); divnum++) {
                        Course course = getCourse(divs.get(divnum).textNodes(), divs.get(divnum), j - 1, i); //获取课程
                        for (Integer w : course.getWeeksList()) {
                            this.courses[w - 1][i][j - 1] = course;
                        }

                    }
                }

            }
        }
    }

    private Course getCourse(List<TextNode> list, Element div, int weekday, int coursenum) {
        String[] strs = div.select("span").text().split(" ");
        String no = list.get(0).text();          //该节课编号
        String id_name = list.get(1).text();     //课程号和名字
        String place = list.get(2).text();       //上课地点
        String weeks = list.get(3).text();       //上课的周数
        String length = div.select("font").text();      //一节课的长度
        String teacher = strs[0];     //上课老师
        String type = strs[1];        //选修或者必修
        String credit = strs[2];      //学分
        return new Course(no, id_name, place, weeks, length, teacher, type, credit, weekday, coursenum);
    }


    /**
     * @description 将课表转换为840位01格式
     * @return
     */
    public String toBinaryCode() {
        StringBuffer codes = new StringBuffer();
        for (int sw = 0; sw < schoolweeks; sw++) {
            for (int w = 0; w < weekdays; w++) {
                for (int cn = 0; cn < coursenums; cn++) {
                    if (courses[sw][cn][w] == null) {
                        codes.append('0');
                    } else {
                        codes.append('1');
                        String len = courses[sw][cn][w].getLength();
                        if (len.indexOf("4") > -1) {
                            codes.append("1");
                            cn++;
                        }
                    }
                }
            }
        }
        return codes.toString();
    }

    public BaseURL getBaseURL() {
        return baseURL;
    }

    public void setBaseURL(BaseURL baseURL) {
        this.baseURL = baseURL;
    }
}
