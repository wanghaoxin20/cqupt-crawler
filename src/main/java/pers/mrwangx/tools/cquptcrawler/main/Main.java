package pers.mrwangx.tools.cquptcrawler.main;

import org.apache.commons.cli.*;
import pers.mrwangx.tools.cquptcrawler.CquptCrawler;
import pers.mrwangx.tools.cquptcrawler.entity.ListCourse;
import pers.mrwangx.tools.cquptcrawler.entity.Room;
import pers.mrwangx.tools.cquptcrawler.entity.StuCourses;
import pers.mrwangx.tools.cquptcrawler.entity.URLConfig;
import pers.mrwangx.tools.cquptcrawler.jwzx.Jwzx;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import static pers.mrwangx.tools.cquptcrawler.CquptCrawler.*;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/2/3 0:43
 *****/
public class Main {

    public static void main(String[] args) throws IOException {

        Options options = createCommandOptions();
        String lineSeparator = System.lineSeparator();

        CommandLineParser parser = new DefaultParser();
        CommandLine line = null;
        try {
            line = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println("参数转换错误:" + e.getMessage());
        }
        if (line != null) {
            if (line.getOptions().length == 0) {
                HelpFormatter helpFormatter = new HelpFormatter();
                helpFormatter.printHelp("cqupt-crawler", options);
            } else {
                URLConfig netType = URLConfig.LAN;

                if (line.hasOption("nt")) {
                    if ("wan".equals(line.getOptionValue("nt"))) {
                        netType = URLConfig.WAN;
                    } else if ("lan".equals(line.getOptionValue("nt"))) {
                        netType = URLConfig.LAN;
                    } else {
                        System.out.println("网络类型只能为lan或者wan");
                        return;
                    }
                }

                if (line.hasOption("stu")) {
                    String keyword = line.getOptionValue("stu");
                    searchStudent(keyword, netType).forEach(student -> {
                        System.out.println(studentDisplay(student) + System.lineSeparator() + "-------------------------------------");
                    });
                }


                else if (line.hasOption("cs")) {
                    String[] values = line.getOptionValues("cs");
                    try {
                        if (values.length >= 1) {
                            int sno = Integer.parseInt(values[0]);
                            int schoolweek = 1, weekday = 1, coursenum = 1;
                            if (values.length == 1) {

                            } else if (values.length == 2) {
                                schoolweek = currentSchoolWeek(netType);
                                weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                                weekday = weekday == 0 ? 7 : weekday - 1;
                                coursenum = Integer.parseInt(values[1]);
                            } else if (values.length == 3) {
                                schoolweek = currentSchoolWeek(netType);
                                weekday = Integer.parseInt(values[1]);
                                coursenum = Integer.parseInt(values[2]);
                            } else if (values.length == 4) {
                                schoolweek = Integer.parseInt(values[1]);
                                weekday = Integer.parseInt(values[2]);
                                coursenum = Integer.parseInt(values[3]);
                            }
                            ListCourse listCourse = searchCourse(sno, netType).getCourse(schoolweek, weekday, coursenum);
                            System.out.println(
                                    String.format("%d周星期%d第%d节课", schoolweek, weekday, coursenum) + System.lineSeparator() +
                                            "**********************************" + System.lineSeparator() +
                                            listCourseDisplay(listCourse)
                            );


                            if (line.hasOption("css")) {
                                URLConfig finalNetType = netType;
                                listCourse.getCourses().forEach(cs -> {
                                    System.out.println("+--------------------------+" + lineSeparator +
                                            cs.getCourseNo() + "学生名单" + lineSeparator +
                                            "+--------------------------+"
                                    );
                                    StringBuilder builder = new StringBuilder();
                                    studentOfCourse(finalNetType, cs.getCourseNo()).forEach(
                                            stu -> {
                                                builder.append(display(stu) + lineSeparator + "-------------------------------------" + lineSeparator);
                                            }
                                    );
                                    System.out.println(builder);
                                });
                            }

                            if (line.hasOption("d")) {
                                File dir = new File(line.getOptionValue("d"));
                                if (!dir.exists()) {
                                    System.out.println(String.format("指定文件夹[%s]不存在", dir.getAbsolutePath()));
                                } else {
                                    URLConfig finalNetType1 = netType;
                                    listCourse.getCourses().forEach(cs -> {
                                        CquptCrawler.downloadStudentsOfCourseExcel(finalNetType1, cs.getCourseNo(), dir.getAbsolutePath());
                                    });
                                }
                            }

                        }
                    } catch (NumberFormatException e) {
                        System.out.println("参数不正确:" + e.getMessage());
                    }
                }

                else if (line.hasOption("scs")) {
                    String value = line.getOptionValue("scs");
                    StuCourses stuCourses = CquptCrawler.searchCourse(Integer.parseInt(value), netType);
                    System.out.println(CquptCrawler.stuCoursesDisplay(stuCourses));
                }


                else if (line.hasOption("er")) {
                    String[] value = line.getOptionValues("er");
                    int weekStart = value[0].equals("crt") ? currentSchoolWeek(netType) : Integer.parseInt(value[0]);
                    int weekEnd = value[1].equals("crt") ? currentSchoolWeek(netType) : Integer.parseInt(value[1]);
                    int weekday = Calendar.getInstance().get(Calendar.DAY_OF_WEEK);
                    weekday = weekday == 0 ? 7 : weekday - 1;
                    weekday = value[2].equals("crt") ? weekday : Integer.parseInt(value[2]);
                    String[] strs = value[3].split(",");
                    int[] courseNums = new int[strs.length];
                    for (int i = 0; i < strs.length; i++) {
                        courseNums[i] = Integer.parseInt(strs[i]);
                    }
                    StringBuilder builder = new StringBuilder();
                    List<Room> rooms = searchEmptyRoom(netType, weekStart, weekEnd, weekday, courseNums);
                    System.out.println("+---------------------------------------+" + lineSeparator +
                            weekStart + "-" + weekEnd + "星期" + weekday + "第" + value[3] + "节课" + "空教室情况:" + lineSeparator +
                            "共" + rooms.size() + "个教室可用" + lineSeparator +
                            "+---------------------------------------+");
                    rooms.forEach(r -> {
                        builder.append(CquptCrawler.display(r) + lineSeparator + "---------------------------" + lineSeparator);
                    });
                    System.out.println(builder.toString());
                }


                else if (line.hasOption("wk")) {
                    System.out.println("当前学校周数:第" + currentSchoolWeek(netType) + "周");
                }


                else if (line.hasOption("jwzx")) {
                    Jwzx jwzx = new Jwzx(URLConfig.LAN);
                    jwzx.run();
                }


                else if (line.hasOption("h")) {
                    HelpFormatter helpFormatter = new HelpFormatter();
                    helpFormatter.printHelp("cqupt-crawler", options);
                }
            }
        }
    }

    public static Options createCommandOptions() {
        Options options = new Options();

        Option help = Option.builder("h")
                .longOpt("help")
                .desc("显示帮助信息")
                .build();

        Option nt = Option.builder("nt")
                .longOpt("nettype")
                .required(false)
                .argName("netType")
                .hasArg()
                .desc("网络类型,默认为内网, lan:内网, wan:外网")
                .build();

        Option stu = Option.builder("stu")
                .longOpt("student")
                .argName("keyword")
                .numberOfArgs(1)
                .type(String.class)
                .desc("搜索学生信息, 需要搜索关键词")
                .build();

        Option cs = Option.builder("cs")
                .longOpt("course")
                .argName("params...")
                .hasArgs()
                .desc(
                        "搜索课程信息, 学号 具体某节课" + System.lineSeparator() +
                                "[sno]" + System.lineSeparator() +
                                "-cs 2016666666" + System.lineSeparator() +
                                "[sno coursenum]" + System.lineSeparator() +
                                "-cs 2016666666 1" + System.lineSeparator() +
                                "[sno weekday,coursenum]" + System.lineSeparator() +
                                "-cs 2016666666 3,1" + System.lineSeparator() +
                                "[sno schoolweek, weekday,coursenum]" + System.lineSeparator() +
                                "-cs 2016666666 15,3,5"
                )
                .build();

        Option scs = Option.builder("scs")
                .longOpt("showcourse")
                .argName("stuno")
                .hasArg()
                .desc(
                        "显示所有课程信息，参数为学号"
                )
                .build();

        Option css = Option.builder("css")
                .hasArg(false)
                .desc(
                        "显示该节课的学生名单, 需要与cs一起用"
                )
                .build();

        Option d = Option.builder("d")
                .argName("directory")
                .hasArg()
                .longOpt("donwload")
                .desc(
                        "下载"
                )
                .build();

        Option er = Option.builder("er")
                .argName("params...")
                .numberOfArgs(4)
                .longOpt("emptyroom")
                .desc(
                        "查找空教室, crt为当前的周数或者星期数" + System.lineSeparator() +
                                "weekStart weekEnd weekday courseNums(split with ,)"
                )
                .build();

        Option wk = Option.builder("wk")
                .hasArg(false)
                .longOpt("week")
                .desc("当前学校周数")
                .build();

        Option jwzx = Option.builder("jwzx")
                .hasArg(false)
                .desc("教务在线")
                .build();

        options.addOption(help);
        options.addOption(nt);
        options.addOption(stu);
        options.addOption(cs);
        options.addOption(scs);
        options.addOption(css);
        options.addOption(d);
        options.addOption(er);
        options.addOption(wk);
        options.addOption(jwzx);

        return options;
    }


}
