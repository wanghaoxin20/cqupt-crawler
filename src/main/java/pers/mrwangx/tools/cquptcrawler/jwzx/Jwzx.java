package pers.mrwangx.tools.cquptcrawler.jwzx;

import pers.mrwangx.tools.command.CommandProcesser;
import pers.mrwangx.tools.command.annotation.Cmd;
import pers.mrwangx.tools.command.entity.CmdInfo;
import pers.mrwangx.tools.cquptcrawler.CquptCrawler;
import pers.mrwangx.tools.cquptcrawler.entity.URLConfig;

import java.util.Scanner;

import static pers.mrwangx.tools.cquptcrawler.CquptCrawler.JWZXLogin;
import static pers.mrwangx.tools.cquptcrawler.CquptCrawler.display;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/2
 * \* Time: 18:24
 * \* Description:
 **/
public class Jwzx {

    private Scanner input = new Scanner(System.in);
    private URLConfig netType;
    private User user;
    private CommandProcesser processer;

    public void run() {
        while (true) {
            System.out.print(">");
            String line = input.nextLine();
            processer.process(line);
        }
    }

    public Jwzx(URLConfig netType) {
        this.netType = netType;
        this.processer = new CommandProcesser("pers.mrwangx.tools.cquptcrawler.jwzx.command", this);
    }

    @Cmd.Processor("login")
    public void login(String sno, String password) {
        User user = new User(Integer.parseInt(sno), password);
        LoginResponseInfo info = JWZXLogin(netType, user);
        if (info.getCode() == 0) {
            System.out.println("登录成功");
            this.user = user;
        } else {
            this.user = null;
            System.out.println("登录失败:" + display(info));
        }
    }

    @Cmd.Processor("dspic")
    public void stuPicDownload(String sno, String dir) {
        if (user != null) {
            CquptCrawler.downloadStuPic(netType, Integer.parseInt(sno), user.getSessionId(), dir);
        } else {
            System.out.println("请先登录");
        }
    }

    @Cmd.Processor("dcts")
    public void downloadChineseTranscripts(String dir) {
        if (user != null) {
            CquptCrawler.downloadChineseTranscripts(netType, user, dir);
        } else {
            System.out.println("请先登录");
        }
    }

    @Cmd.Processor("dssp")
    public  void downloadStudentStatusProof(String dir) {
        if (user != null) {
            CquptCrawler.downloadStudentStatusProof(netType, user.getSno(), user.getSessionId(), dir);
        } else {
            System.out.println("请先登录");
        }
    }

    @Cmd.Processor("help")
    public void help() {
        System.out.println(display(processer));
    }

    @Cmd.Processor("exit")
    public void exit() {
        System.exit(0);
    }



    public URLConfig getNetType() {
        return netType;
    }

    public void setNetType(URLConfig netType) {
        this.netType = netType;
    }

    public User getUser() {
        return user;
    }

    public Scanner getInput() {
        return input;
    }

    public Jwzx setInput(Scanner input) {
        this.input = input;
        return this;
    }

    public Jwzx setUser(User user) {
        this.user = user;
        return this;
    }

    public CommandProcesser getProcesser() {
        return processer;
    }

    public Jwzx setProcesser(CommandProcesser processer) {
        this.processer = processer;
        return this;
    }
}
