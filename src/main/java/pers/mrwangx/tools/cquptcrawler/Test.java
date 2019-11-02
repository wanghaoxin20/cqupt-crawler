package pers.mrwangx.tools.cquptcrawler;

import pers.mrwangx.tools.cquptcrawler.entity.URLConfig;
import pers.mrwangx.tools.cquptcrawler.entity.jwzx.User;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

import static pers.mrwangx.tools.cquptcrawler.CquptCrawler.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/9/8
 * \* Time: 11:08
 * \* Description:
 **/
public class Test {

    /**
     * 图片压缩...打印
     * @param url
     * @param x         将图片缩小几倍(偶数，2,4,6,12,24....)
     */
    public static void compressImg(String url ,int x){

        try {

            //获取图像资源，转成BufferedImage对象
            BufferedImage bimg = ImageIO.read(new URL(url).openStream());
            //创建一个二维数组，用来存放图像每一个像素位置的颜色值
            int height = bimg.getHeight(), width = bimg.getWidth();
            int[][] data = new int[bimg.getWidth()][bimg.getHeight()];
            //以高度为范围，逐列扫描图像，存进数组对应位置
            for (int i = 0; i < bimg.getWidth(); i++) {
                for (int j = 0; j < bimg.getHeight(); j++) {
                    data[i][j] = bimg.getRGB(i, j);//得到的是sRGB值，4字节
                }
            }

            //将数组旋转90°输出，实现逐行输出图像
            for (int i = 0; i < height; i+=x) {
                for (int j = 0; j < width; j+=x) {
                    if (data[j][i] != -1) {//有颜色输出
                        System.out.print("*");
                    } else {            //无颜色输出
                        System.out.print(" ");
                    }
                }
                System.out.println();//每行结束换行
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
//        List<Room> rooms = CquptCrawler.searchEmptyRoom(URLConfig.LAN, 1, 1, 1, 1);
//        System.out.println(rooms.size());
//        rooms.forEach(r -> {
//            System.out.println(CquptCrawler.display(r) + System.lineSeparator() + "-------------------");
//        });
        User user = new User();
        user.setSno(2016211995);
        user.setPwd("123456;");
        System.out.println(display(JWZXLogin(URLConfig.LAN, user)));
    }



}
