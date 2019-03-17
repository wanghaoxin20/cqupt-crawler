package pers.mrwangx.cquptcrawler.main;

import pers.mrwangx.cquptcrawler.pojo.BaseURL;
import pers.mrwangx.cquptcrawler.service.StuCourses;
import pers.mrwangx.cquptcrawler.util.StudentUtil;

import java.util.Scanner;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/2/3 0:43
 *****/
public class Main {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        StuCourses stuCourses = new StuCourses(input.nextLine(), BaseURL.LAN);
        String codes = stuCourses.toBinaryCode();
        while (true) {
            String s = input.nextLine();
            String[] key = s.trim().split(",");
            System.out.println(stuCourses.getCourse(Integer.parseInt(key[0]), Integer.parseInt(key[1]), Integer.parseInt(key[2])));
            System.out.println(codes.charAt((Integer.parseInt(key[0]) - 1) * 42 + (Integer.parseInt(key[1]) -1) * 6 + (Integer.parseInt(key[2]) - 1)));
        }
    }


    public static void StuPic() {
        Scanner input = new Scanner(System.in);
        String sno = null;
        while (true) {
            sno = input.nextLine();
            if (sno.equals("#")) {
                System.exit(0);
            } else {
                StudentUtil.showStuPhoto(sno, BaseURL.LAN);
            }
        }
    }

    public static void StuCourses() {
        Scanner input = new Scanner(System.in);
        String sno = null;
        System.out.println("请输入学号:");
        sno = input.nextLine();
        StuCourses stuCourses = new StuCourses(sno, BaseURL.LAN);
        System.out.println("课表信息查询完毕\n");
        String bc = stuCourses.toBinaryCode();
        System.out.println(bc);
        while (true) {
            System.out.println("请输入周数,星期,课节数:");
            String key = input.nextLine();
            if (key.equals("#")){
                break;
            } else {
                String[] keys = key.split(",");
                if (keys.length == 3) {
                    System.out.println("第" + keys[0] + "周,星期" + keys[1] + ",第" + keys[2] + "节课:");
                    int schoolweek = Integer.parseInt(keys[0]);
                    int weekday = Integer.parseInt(keys[1]);
                    int coursenum = Integer.parseInt(keys[2]);
                    System.out.println(stuCourses.getCourse(schoolweek, weekday, coursenum));
                    System.out.println(bc.charAt(42 * (schoolweek - 1) + 7 * (weekday - 1) + (coursenum - 1)) + "\n");
                } else {
                    System.out.println("输入错误\n");
                }
            }
        }
    }


}
