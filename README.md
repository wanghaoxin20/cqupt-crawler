# CQUPTCrawler

重邮学生信息等查询的JAVA api,**目前还在完善中**

# How to Use
```java
StuCourses stuCourses = new StuCourses("${sno}"); //${sno}为学号
Course course = stuCourses.getCourse(1, 1, 1); //获取第一周星期一第一节课的信息
System.out.println(course);
stuCourses.setCourses("2010111666"); //重新设置学生课表
System.out.println(stuCourses.toBinaryCode()); //将课表转换为840位01格式 0 有课 1 无课

StudentUtil.getStudents("${key}"); //key为关键字
StudentUtil.showStuPhoto("${sno}"); //用默认浏览器打开该学号学生的图片

```

# THANKS
+ **[重邮内网外入](https://cqu.pt)**@**[Cong Min](http://congm.in/)**

# About
####  **Author** 
+ Email:**[wang.haoxin@nexuslink.cn](wang.haoxin@nexuslink.cn)**
+ QQ:**706519033**
