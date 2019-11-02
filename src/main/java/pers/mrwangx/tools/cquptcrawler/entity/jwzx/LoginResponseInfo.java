package pers.mrwangx.tools.cquptcrawler.entity.jwzx;

import pers.mrwangx.tools.cquptcrawler.annotation.Display;

/**
 * \* Author: MrWangx
 * \* Date: 2019/10/30
 * \* Time: 18:29
 * \* Description: 登录教务在线后的返回信息
 *                  code: 0 登录成功 | 1 登录失败
 *
 **/
public class LoginResponseInfo {

    @Display("code")
    private int code;
    @Display("info")
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
