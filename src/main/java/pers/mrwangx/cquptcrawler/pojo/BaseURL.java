package pers.mrwangx.cquptcrawler.pojo;

/****
 * @author:MrWangx
 * @description
 * @Date 2019/3/17 2:13
 *****/
public enum BaseURL {

    LAN("http://jwzx.cqupt.edu.cn"),
    WAN("http://jwzx.cqu.pt");

    private String url;

    BaseURL(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
