package pers.mrwangx.tools.cquptcrawler.jwzx.command;

import pers.mrwangx.tools.command.annotation.Cmd;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/4
 * \* Time: 19:54
 * \* Description:
 **/
@Cmd(name = "dssp", desc = "下载学籍证明")
public class DownloadStudentStatusProof {

    @Cmd.Arg(argName = "文件夹", desc = "需要保存学籍的文件夹路径")
    String dir;
}
