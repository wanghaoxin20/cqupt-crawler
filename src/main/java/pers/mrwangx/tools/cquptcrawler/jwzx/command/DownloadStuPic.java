package pers.mrwangx.tools.cquptcrawler.jwzx.command;

import pers.mrwangx.tools.command.annotation.Cmd;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/4
 * \* Time: 18:59
 * \* Description:
 **/
@Cmd(name = "dspic", desc = "下载学生照片")
public class DownloadStuPic {

    @Cmd.Arg(argName = "学号", desc = "需要下载的学生学号", regex = "^[0-9]{10}$")
    String sno;

    @Cmd.Arg(argName = "文件夹", desc = "需要保存图片的文件夹路径")
    String dir;

}
