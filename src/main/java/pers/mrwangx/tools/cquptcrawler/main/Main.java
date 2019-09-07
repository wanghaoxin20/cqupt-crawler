package pers.mrwangx.tools.cquptcrawler.main;

import org.apache.commons.cli.*;
import pers.mrwangx.tools.cquptcrawler.entity.URLConfig;

import java.io.IOException;
import java.util.Calendar;

import static pers.mrwangx.tools.cquptcrawler.CquptCrawler.*;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/2/3 0:43
 *****/
public class Main {

    public static void main(String[] args) throws IOException {

        Options options = createCommandOptions();

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
                        return ;
                    }
                }

                if (line.hasOption("stu")) {
                    String keyword = line.getOptionValue("stu");
                    searchStudent(keyword, netType).forEach(student -> {
                        System.out.println(studentDisplay(student) + System.lineSeparator() + "-------------------------------------");
                    });
                } else if (line.hasOption("cs")) {
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
                            System.out.println(
                                    String.format("%d周星期%d第%d节课", schoolweek, weekday, coursenum) + System.lineSeparator() +
                                            "**********************************" + System.lineSeparator() +
                                            listCourseDisplay(searchCourse(sno, netType).getCourse(schoolweek, weekday, coursenum))
                            );
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("参数不正确:" + e.getMessage());
                    }
                }

            }
        }
    }

    public static Options createCommandOptions() {
        Options options = new Options();

        Option help = Option.builder("help")
                .desc("显示帮助信息")
                .build();

        Option nt = Option.builder("nt")
                .required(false)
                .argName("netType")
                .hasArg()
                .desc("网络类型,默认为内网, lan:内网, wan:外网")
                .build();

        Option stu = Option.builder("stu")
                .argName("keyword")
                .numberOfArgs(1)
                .type(String.class)
                .desc("搜索学生信息, 需要搜索关键词")
                .build();

        Option cs = Option.builder("cs")
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

        options.addOption(help);
        options.addOption(nt);
        options.addOption(stu);
        options.addOption(cs);

        return options;
    }


}
