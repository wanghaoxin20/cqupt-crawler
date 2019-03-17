package pers.mrwangx;

import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;
import pers.mrwangx.cquptcrawler.pojo.Student;
import pers.mrwangx.cquptcrawler.util.StudentUtil;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;


/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    @Ignore
    public void test1() throws NoSuchFieldException, NoSuchMethodException, InvocationTargetException, IllegalAccessException, ClassNotFoundException {
        Student stu = new Student("2016211995", "王昊鑫", "男", "土家族", "1998-02-20", "201609", "2016", "0403", "计算机科学与技术学院", "0403", "网络工程", "0403");
//        Class cs = Class.forName("pers.mrwangx.cquptcrawler.pojo.Student");
//        for (Field f : cs.getDeclaredFields()) {
//            f.setAccessible(true);
//            System.out.println(f.getName() + "," + f.get(stu));
//        }
        System.out.println(StudentUtil.toJson(stu, Student.class, "sno", "name", "sex"));

    }

    @Test
    public void test2() {
        String str1 = " sdf  \n";
        System.out.println(str1.matches("^\\s+|$"));
        System.out.println(Byte.parseByte(new Integer(2).toString()));
    }

}
