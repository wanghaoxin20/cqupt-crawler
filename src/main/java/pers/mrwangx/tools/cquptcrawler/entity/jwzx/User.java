package pers.mrwangx.tools.cquptcrawler.entity.jwzx;

/**
 * \* Author: MrWangx
 * \* Date: 2019/10/30
 * \* Time: 18:35
 * \* Description:
 **/
public class User {

    private String sno;
    private String pwd;
    private String sessionId;

    public User() {}

    public User(String sno, String pwd) {
        this.sno = sno;
        this.pwd = pwd;
    }

    public User(String sno, String pwd, String sessionId) {
        this.sno = sno;
        this.pwd = pwd;
        this.sessionId = sessionId;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getSno() {
        return sno;
    }

    public void setSno(String sno) {
        this.sno = sno;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
}
