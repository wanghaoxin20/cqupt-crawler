package pers.mrwangx.cquptcrawler.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import pers.mrwangx.cquptcrawler.pojo.Student;

import java.awt.*;
import java.io.IOException;
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


    public static final String BASE_URL = "http://jwzx.cqu.pt";


    /**
     * @description 获取学生信息
     * @param key 搜寻关键词
     * @return 学生信息list
     */
    public static List<Student> getStudents(String key) {
        List<Student> list = null;
        String url = "/data/json_StudentSearch.php";
        try {
            Document doc = Jsoup.connect(BASE_URL + url).data("searchKey", key).get();
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
    public static void showStuPhoto(String sno) {
        if (Desktop.isDesktopSupported()) {
            Desktop dp = Desktop.getDesktop();
            if (dp.isSupported(Desktop.Action.BROWSE)) {
                try {
                    dp.browse(new URI(BASE_URL + "/showstupic.php?xh=" + sno));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }
            }
        }
    }



}
