package pers.mrwangx.cquptcrawler.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pers.mrwangx.cquptcrawler.pojo.BaseURL;
import pers.mrwangx.cquptcrawler.pojo.Student;

import java.awt.*;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/2/2 13:09
 *****/
public class StudentUtil {

    /**
     * @description 获取学生信息
     * @param key 搜寻关键词
     * @return 学生信息list
     */
    public static List<Student> getStudents(String key, BaseURL baseURL) {
        List<Student> list = null;
        String url = "/data/json_StudentSearch.php";
        try {
            Document doc = Jsoup.connect(baseURL.getUrl() + url).data("searchKey", key).get();
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
     * @description 展示学生图片
     * @param sno 学号
     */
    public static void showStuPhoto(String sno, BaseURL baseURL) {
        if (Desktop.isDesktopSupported()) {
            Desktop dp = Desktop.getDesktop();
            if (dp.isSupported(Desktop.Action.BROWSE)) {
                try {
                    dp.browse(new URI(baseURL.getUrl() + "/showstupic.php?xh=" + sno));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String toJson(Object obj, Class clazz, String... fieldNames) throws NoSuchFieldException, IllegalAccessException {

        StringBuffer sb = new StringBuffer();

        sb.append("{");

        for (int i = 0; i < fieldNames.length; i++) {
            Field field = clazz.getDeclaredField(fieldNames[i]);
            field.setAccessible(true);
            if (i == fieldNames.length - 1) {
                sb.append(field.getName() + ":" + field.get(obj) + "}");
            } else {
                sb.append(field.getName() + ":" + field.get(obj) + ",");
            }
        }

        return sb.toString();
    }

}
