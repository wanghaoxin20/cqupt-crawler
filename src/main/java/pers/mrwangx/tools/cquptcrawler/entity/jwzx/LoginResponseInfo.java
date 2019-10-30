package pers.mrwangx.tools.cquptcrawler.entity.jwzx;

/**
 * \* Author: MrWangx
 * \* Date: 2019/10/30
 * \* Time: 18:29
 * \* Description: 登录教务在线后的返回信息
 *                  code: 0 登录成功 | 1 登录失败
 *
 **/
public class LoginResponseInfo {

    private int code;
    private String info;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}
