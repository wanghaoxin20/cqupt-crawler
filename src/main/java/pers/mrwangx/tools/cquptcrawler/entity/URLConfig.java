package pers.mrwangx.tools.cquptcrawler.entity;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/3/17 2:13
 *****/
public enum URLConfig {


    LAN("http://jwzx.cqupt.edu.cn"),                                                        //教务在线内网地址
    WAN("http://jwzx.cquptx.cn"),                                                           //外网地址
    COURSE("/kebiao/kb_stu.php?xh="),                                                       //课表
    COURSE_LIST_OF_STUDENTS("/kebiao/kb_stuList.php?jxb="),                                 //选课学生名单
    COURSE_LIST_OF_STUDENTS_DOWNLOAD("/kebiao/ExcelDownloadXsmd.php?jxb="),                 //选课学生名单下载
    EMPTY_ROOM("/kebiao/emptyRoomSearch.php"),                                              //空教室查询
    STUMES("/data/json_StudentSearch.php"),                                                 //学生信息接口
    LOGIN("/checkLogin.php"),                                                               //登录接口
    VIMG("/createValidationCode.php"),                                                      //验证码接口
    JWZX_SESSIONID("/login.php"),                                                           //访问获取session
    CREATE_CHINESE_TRANSCRIPTS("/dzzm/stuCjZw.php?xh="),                                    //生成成绩单
    CREATE_STUDENT_STATUS_PROOF("/dzzm/stuXj.php?xh="),                                     //生成学籍证明
    PROOF_DOWNLOAD("/dzzm/downLoad.php?id="),                                               //下载证明接口
    STUPIC("/showstupic.php?xh=");                                                          //学生图片

    private String url;

    URLConfig(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
