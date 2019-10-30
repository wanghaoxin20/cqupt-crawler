package pers.mrwangx.tools.cquptcrawler;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;
import pers.mrwangx.tools.cquptcrawler.entity.*;
import pers.mrwangx.tools.cquptcrawler.entity.jwzx.LoginResponseInfo;
import pers.mrwangx.tools.cquptcrawler.entity.jwzx.User;

import javax.imageio.ImageIO;
import javax.print.DocFlavor;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/9/4
 * \* Time: 22:24
 * \* Description:
 **/
public class CquptCrawler {

    private static final String JWZX_SESSIONID_NAME = "PHPSESSID";
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
     * 模拟访问，获取教务在线的SESSIONID
     *
     * @param netType
     * @return
     */
    public static String getJWZXSessionId(URLConfig netType) {
        String sessionId = null;
        try {
            Connection.Response response = Jsoup.connect(netType.getUrl() + URLConfig.JWZX_SESSIONID.getUrl()).execute();
            sessionId = response.cookie(JWZX_SESSIONID_NAME);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sessionId;
    }

    /**
     * 获取验证码图片
     *
     * @param netType
     * @param sessionId
     * @return
     */
    public static Image getVImage(URLConfig netType, String sessionId) {
        try {
            Connection.Response response = Jsoup.connect(netType.getUrl() + URLConfig.VIMG.getUrl()).method(Connection.Method.POST).ignoreContentType(true).cookie(JWZX_SESSIONID_NAME, sessionId).execute();
            return ImageIO.read(response.bodyStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 输入验证码
     *
     * @param netType
     * @param sessionId
     * @return
     */
    public static String vCodeInput(URLConfig netType, String sessionId) {
        String vCode = null;
        Object ans = JOptionPane.showInputDialog(null, "请输入验证码", "提示", 1, new ImageIcon(CquptCrawler.getVImage(URLConfig.LAN, sessionId)), null, null);
        return ans.toString();
    }

    /**
     * 登录教务在线
     * @param netType
     * @param user
     * @param vCode
     * @return
     */
    public static LoginResponseInfo JWZXLogin(URLConfig netType, User user, String vCode) {
        LoginResponseInfo loginInfo = null;
        try {
            System.out.println(String.format("登录{sno:%s, pwd:%s, vCode:%s}", user.getSno(), user.getPwd(), vCode));
            Connection.Response response = Jsoup.connect(netType.getUrl() + URLConfig.LOGIN.getUrl()).method(Connection.Method.POST).data("name", user.getSno()).data("password", user.getPwd()).data("vCode", vCode).cookie(JWZX_SESSIONID_NAME, user.getSessionId()).execute();
            loginInfo = JSON.parseObject(response.body()).toJavaObject(LoginResponseInfo.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loginInfo;
    }

    /**
     * 生成中文成绩单
     * @param netType
     * @param user
     * @return 下载的id
     */
    public static int createChineseTranscripts(URLConfig netType, User user) {
        int downloadId = -1;
        try {
            Connection.Response response = Jsoup.connect(netType.getUrl() + URLConfig.CREATE_CHINESE_TRANSCRIPTS.getUrl() + user.getSno())
                    .method(Connection.Method.GET)
                    .cookie(JWZX_SESSIONID_NAME, user.getSessionId())
                    .execute();
            String str = response.body();
            downloadId = Integer.parseInt(str.substring(str.indexOf("id="), str.lastIndexOf("'")).split("=")[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return downloadId;
    }

    /**
     * 生成中文成绩单
     * @param netType
     * @param sno
     * @param sessionId
     * @return 下载的id
     */
    public static int createChineseTranscripts(URLConfig netType, int sno, String sessionId) {
        int downloadId = -1;
        try {
            Connection.Response response = Jsoup.connect(netType.getUrl() + URLConfig.CREATE_CHINESE_TRANSCRIPTS.getUrl() + sno)
                    .method(Connection.Method.GET)
                    .cookie(JWZX_SESSIONID_NAME, sessionId)
                    .execute();
            String str = response.body();
            System.out.println(str);
            downloadId = Integer.parseInt(str.substring(str.indexOf("id="), str.lastIndexOf("'")).split("=")[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return downloadId;
    }

    /**
     * 下载成绩单
     * @param netType
     * @param user
     * @param downloadId 下载的id
     * @param dir
     */
    public static void downloadChineseTranscripts(URLConfig netType, User user, int downloadId, String dir) {
        downloadChineseTranscripts(netType, user.getSessionId(), downloadId, dir);
    }

    /**
     * 下载成绩单, 可以下载其他人的成绩单
     * @param netType
     * @param downloadId 下载的id
     * @param dir
     */
    public static void downloadChineseTranscripts(URLConfig netType, String sessionId, int downloadId, String dir) {
        try {
            Connection.Response response = Jsoup.connect(netType.getUrl() + URLConfig.DOWNLOAD_CHINESE_TRANSCRIPTS.getUrl() + downloadId)
                    .method(Connection.Method.GET)
                    .cookie(JWZX_SESSIONID_NAME, sessionId)
                    .ignoreContentType(true)
                    .execute();
            response.headers().forEach((key, value) -> {
                System.out.println(key + ":" + value);
            });
            String filename = response.header("Content-Disposition").split(";")[1].split("=")[1];
            BufferedInputStream bin = response.bodyStream();
            FileOutputStream fout = new FileOutputStream(dir + File.separator + filename);
            byte[] data = new byte[1024];
            int len = 0;
            while ((len = bin.read(data, 0, data.length)) != -1) {
                fout.write(data, 0, len);
            }
            System.out.println("下载成功");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 下载成绩单 不需要downloadId
     * @param netType
     * @param user
     * @param dir
     */
    public static void downloadChineseTranscripts(URLConfig netType, User user, String dir) {
        int downloadId = createChineseTranscripts(netType, user);
        if (downloadId != -1) {
            downloadChineseTranscripts(netType, user, downloadId, dir);
        }
    }


    /**
     * 下载学生照片
     * @param netType
     * @param sno
     * @param sessionId
     * @param dir
     * @param picName
     */
    public static void downloadStuPic(URLConfig netType, int sno, String sessionId, String dir, String picName) {
        try {
            Connection.Response response = Jsoup.connect(netType.getUrl() + URLConfig.STUPIC.getUrl() + sno)
                    .method(Connection.Method.GET)
                    .cookie(JWZX_SESSIONID_NAME, sessionId)
                    .ignoreContentType(true)
                    .execute();
            BufferedImage image = ImageIO.read(response.bodyStream());
            ImageIO.write(image, "jpg", new File(dir + File.separator + picName));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 下载学生照片
     * @param netType
     * @param sno
     * @param sessionId
     * @param dir
     */
    public static void downloadStuPic(URLConfig netType, int sno, String sessionId, String dir) {
        downloadStuPic(netType, sno, sessionId, dir, sno + ".jpg");
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
            ListCourse[][] listCourses = new ListCourse[coursenums][weekdays];
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
        StringBuilder courseWeek = new StringBuilder();
        course.getWeeksList().forEach(w -> courseWeek.append(w + ","));
        courseWeek.append(lineSeparator);
        return "课程名:" + course.getId_name() + lineSeparator +
                "老师:" + course.getTeacher() + lineSeparator +
                "学分:" + course.getCredit() + lineSeparator +
                course.getPlace() + lineSeparator +
                "上课节数:" + course.getLength() + lineSeparator +
                "上课周数:" + courseWeek +
                "课程类型:" + course.getType()
                ;

    }

    /**
     * 显示多节课表
     *
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
     * 显示该学期所有课表
     *
     * @param stuCourses
     * @return
     */
    public static String stuCoursesDisplay(StuCourses stuCourses) {
        if (stuCourses == null) {
            return null;
        }
        StringBuilder builder = new StringBuilder();
        String lineSeparator = System.lineSeparator();
        ListCourse[][] listCourses = stuCourses.getListCourses();
        for (int weekday = 0; weekday < listCourses[0].length; weekday++) {
            for (int coursenum = 0; coursenum < listCourses.length; coursenum++) {
                ListCourse listCourse = listCourses[coursenum][weekday];
                if (listCourse != null && !listCourse.isEmpty()) {
                    builder.append("+--------------+" + lineSeparator);
                    builder.append("| 星期" + (weekday + 1) + "第" + (coursenum + 1) + "节课 |" + lineSeparator);
                    builder.append("+--------------+" + lineSeparator);
                    builder.append(listCourseDisplay(listCourse) + lineSeparator);
                }
            }
        }
        return builder.toString();
    }

    /**
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
