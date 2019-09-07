package pers.mrwangx.tools.cquptcrawler.entity;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/3/17 2:13
 *****/
public enum URLConfig {


    LAN("http://jwzx.cqupt.edu.cn"),
    WAN("http://jwzx.cqu.pt"),
    COURSE("/kebiao/kb_stu.php?xh="),
    STUMES("/data/json_StudentSearch.php"),
    STUPIC("/showstupic.php?xh=");

    private String url;

    URLConfig(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
