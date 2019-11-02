package pers.mrwangx.tools.cquptcrawler;

import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;
import pers.mrwangx.tools.cquptcrawler.entity.URLConfig;
import pers.mrwangx.tools.cquptcrawler.entity.jwzx.User;


/**
 * Unit test for simple App.
 */
public class AppTest {


    @Test
    public void test() {
        String sessionId = CquptCrawler.getJWZXSessionId(URLConfig.LAN);
        System.out.println(sessionId);
        User user = new User(2016211995, "102558", sessionId);
        String vCode = CquptCrawler.vCodeInput(URLConfig.LAN, sessionId);
        System.out.println(JSON.toJSONString(CquptCrawler.JWZXLogin(URLConfig.LAN, user, vCode)));
        CquptCrawler.downloadChineseTranscripts(URLConfig.LAN, user, "C:\\Users\\MrWangx\\Desktop");
        CquptCrawler.downloadStudentStatusProof(URLConfig.LAN, user.getSno(), user.getSessionId(), "C:\\Users\\MrWangx\\Desktop");
        CquptCrawler.downloadStuPic(URLConfig.LAN, 2016211945, user.getSessionId(), "C:\\Users\\MrWangx\\Desktop");
        CquptCrawler.downloadStudentsOfCourseExcel(URLConfig.LAN, "A04191A1100010018", "C:\\Users\\MrWangx\\Desktop");
    }

    @Test
    @Ignore
    public void test1() {
        System.out.println("\"12345\"679\"".replaceAll("(^\\\")|(\\\"$)", ""));
    }


}
