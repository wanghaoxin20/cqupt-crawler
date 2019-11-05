package pers.mrwangx.tools.cquptcrawler.jwzx.command;

import pers.mrwangx.tools.command.annotation.Cmd;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/4
 * \* Time: 19:56
 * \* Description:
 **/
@Cmd(name = "dcts", desc = "下载中文成绩单")
public class DownloadChineseTranscripts {

    @Cmd.Arg(argName = "文件夹", desc = "需要保存成绩单的文件夹路径")
    String dir;

}
