package pers.mrwangx.tools.cquptcrawler.jwzx.command;

import pers.mrwangx.tools.command.annotation.Cmd;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/4
 * \* Time: 18:52
 * \* Description:
 **/
@Cmd(name = "login", desc = "登录教务在线")
public class Login {

    @Cmd.Arg(desc = "登录教务在线的账号", argName = "学号", regex = "^[0-9]{10}$")
    String sno;
    @Cmd.Arg(desc = "登录教务在线的密码", argName = "密码", regex = "^.{6,}$")
    String password;

}
