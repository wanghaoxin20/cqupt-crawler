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
import pers.mrwangx.tools.cquptcrawler.annotation.Display;
import pers.mrwangx.tools.cquptcrawler.entity.*;
import pers.mrwangx.tools.cquptcrawler.entity.jwzx.LoginResponseInfo;
import pers.mrwangx.tools.cquptcrawler.entity.jwzx.User;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
     * 查找空教室
     * @param netType
     * @param weekStart
     * @param weekEnd
     * @param weekday
     * @param courseNums
     * @return
     */
    public static List<Room> searchEmptyRoom(URLConfig netType, int weekStart, int weekEnd, int weekday, int... courseNums) {
        List<Room> rooms = new ArrayList<>();
        String courseNumStr = "";
        for (int i = 0; i < courseNums.length; i++) {
            String end = "&";
            if (i == courseNums.length - 1) {
                end = "";
            }
            courseNumStr += "sd[]=" + courseNums[i] * 2 + end;
        }
        Connection connection = Jsoup.connect(netType.getUrl() + URLConfig.EMPTY_ROOM.getUrl() + "?" + courseNumStr + "&"
                + "zcStart=" + weekStart + "&"
                + "zcEnd=" + weekEnd + "&"
                + "xq=" + weekday + "&"
        );
        try {
            Document doc = connection.get();
            Elements tds = doc.select("tbody td");
            tds.forEach(td -> {
                String[] text = td.text().split(" ");
                String name = text[0];
                int capatity = Integer.parseInt(text[1].replaceAll("(^[(])|([)]$)", ""));
                rooms.add(new Room(name, capatity));
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rooms;
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
     * @return
     */
    public static LoginResponseInfo JWZXLogin(URLConfig netType, User user) {
        user.setSessionId(getJWZXSessionId(netType));
        String vcode = vCodeInput(netType, user.getSessionId());
        return JWZXLogin(netType, user, vcode);
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
            Connection.Response response = Jsoup.connect(netType.getUrl() + URLConfig.LOGIN.getUrl()).method(Connection.Method.POST)
                    .data("name", user.getSno() + "")
                    .data("password", user.getPwd())
                    .data("vCode", vCode).cookie(JWZX_SESSIONID_NAME, user.getSessionId()).execute();
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
        return createChineseTranscripts(netType, user.getSno(), user.getSessionId());
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
            downloadId = Integer.parseInt(str.substring(str.indexOf("id="), str.lastIndexOf("'")).split("=")[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return downloadId;
    }

    /**
     * 生成学籍证明
     * @param netType
     * @param user
     * @return
     */
    public static int createStudentStatusProof(URLConfig netType, User user) {
        return createStudentStatusProof(netType, user.getSno(), user.getSessionId());
    }


    /**
     * 生成学籍证明
     * @param netType
     * @param sno
     * @param sessionId
     * @return
     */
    public static int createStudentStatusProof(URLConfig netType, int sno, String sessionId) {
        int downloadId = -1;
        try {
            Connection.Response response = Jsoup.connect(netType.getUrl() + URLConfig.CREATE_STUDENT_STATUS_PROOF.getUrl() + sno)
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
     * 下载证明 sessionId可以不需要
     * @param netType
     * @param sessionId
     * @param downloadId 下载的id
     * @param dir
     */
    public static void downloadProof(URLConfig netType, String sessionId, int downloadId, String dir) {
       download(netType.getUrl() + URLConfig.PROOF_DOWNLOAD.getUrl() + downloadId, sessionId, dir);
    }

    public static void downloadStudentsOfCourseExcel(URLConfig netType, String courseNo, String dir) {
        download(netType.getUrl() + URLConfig.COURSE_LIST_OF_STUDENTS_DOWNLOAD.getUrl() + courseNo, "", dir);
    }

    /**
     * 下载文件
     * @param url
     * @param sessionId
     * @param dir
     */
    public static void download(String url, String sessionId, String dir) {
        try {
            Connection.Response response = Jsoup.connect(url)
                    .method(Connection.Method.GET)
                    .cookie(JWZX_SESSIONID_NAME, sessionId)
                    .ignoreContentType(true)
                    .execute();
            String filename = response.header("Content-Disposition").split(";")[1].split("=")[1];
            filename = filename.replaceAll("(^\\\")|(\\\"$)", "");
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
     * 下载成绩单
     * @param netType
     * @param user
     * @param dir
     */
    public static void downloadChineseTranscripts(URLConfig netType, User user, String dir) {
        int downloadId = createChineseTranscripts(netType, user.getSno(), user.getSessionId());
        if (downloadId != -1) {
            downloadProof(netType, user.getSessionId(), downloadId, dir);
        }
    }


    /**
     * 下载学籍证明
     * @param netType
     * @param sno
     * @param sessionId
     * @param dir
     */
    public static void downloadStudentStatusProof(URLConfig netType, int sno, String sessionId, String dir) {
        int downloadId = createStudentStatusProof(netType, sno, sessionId);
        if (downloadId != -1) {
            downloadProof(netType, sessionId, downloadId, dir);
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

    public static List<Course.Student> studentOfCourse(URLConfig netType, String courseNo) {
        List<Course.Student> students = new ArrayList<>();
        try {
            Document doc = Jsoup.connect(netType.getUrl() + URLConfig.COURSE_LIST_OF_STUDENTS.getUrl() + courseNo).get();
            Elements trs = doc.select("tbody tr");
            trs.forEach(tr -> {
                Course.Student stu = new Course.Student();
                Elements tds = tr.select("td");
                stu.setSno(tds.get(1).text());
                stu.setName(tds.get(2).text());
                stu.setSex(tds.get(3).text());
                stu.setClassid(tds.get(4).text());
                stu.setMajorid(tds.get(5).text());
                stu.setMajorname(tds.get(6).text());
                stu.setCollegename(tds.get(7).text());
                stu.setGrade(tds.get(8).text());
                stu.setXjzt(tds.get(9).text());
                stu.setXkzt(tds.get(10).text());
                stu.setKclb(tds.get(11).text());
                students.add(stu);
            });

        } catch (IOException e) {
            e.printStackTrace();
        }
        return students;
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
                "课程标识:" + course.getCourseNo() + lineSeparator +
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

    /**
     * @param student
     * @return
     */
    public static String studentDisplay(Course.Student student) {
        if (student == null) {
            return null;
        }
        String lineSeparator = System.lineSeparator();
        return
                "学号:" + student.getSno() + lineSeparator +
                        "姓名:" + student.getName() + lineSeparator +
                        "性别:" + student.getSex() + lineSeparator +
                        "年级:" + student.getGrade() + lineSeparator +
                        "班级:" + student.getClassid() + lineSeparator +
                        "专业号:" + student.getMajorid() + lineSeparator +
                        "专业名:" + student.getMajorname() + lineSeparator +
                        "学院:" + student.getCollegename() + lineSeparator +
                        "年级" + student.getGrade() + lineSeparator +
                        "学籍状态" + student.getXjzt() + lineSeparator +
                        "选课状态" + student.getXkzt() + lineSeparator +
                        "课程类别" + student.getKclb()
                ;
    }

    /**
     * 输出
     * @param o
     * @return
     */
    public static String display(Object o) {
        StringBuilder builder = new StringBuilder();
        Class clazz = o.getClass();
        Field[] fields = clazz.getDeclaredFields();
        String lineSeparator = System.lineSeparator();
        if (clazz.isAnnotationPresent(Display.Separator.class)) {
            Display.Separator s = (Display.Separator) clazz.getAnnotation(Display.Separator.class);
            lineSeparator = s.value();
        }
        int len = fields.length;
        for (int i = 0; i < len; i++) {
            Field f = fields[i];
            f.setAccessible(true);
            lineSeparator = i == len - 1 ? "" : lineSeparator;
            try {
                if (f.isAnnotationPresent(Display.class)) {
                    Display dis = f.getAnnotation(Display.class);
                    if (dis.display()) {
                        String name = dis.value().equals("") ? f.getName()  : dis.value();
                        builder.append(name + ":" + f.get(o) + lineSeparator);
                    }
                } else {
                    String fieldName = f.getName();
                    fieldName = Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                    try {
                        Method m = clazz.getMethod("get" + fieldName);
                        if (m.isAnnotationPresent(Display.class)) {
                            Display dis = m.getAnnotation(Display.class);
                            if (dis.display()) {
                                String name = dis.value().equals("") ? f.getName()  : dis.value();
                                builder.append(name + ":" + m.invoke(o) + lineSeparator);
                            }
                        }
                    } catch (NoSuchMethodException e) {
                        continue;
                    } catch (InvocationTargetException e) {
                        throw e;
                    }
                }
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
                break;
            }
        }
        return builder.toString();
    }


}
