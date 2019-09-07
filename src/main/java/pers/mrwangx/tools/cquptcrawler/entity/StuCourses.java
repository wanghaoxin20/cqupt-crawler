package pers.mrwangx.tools.cquptcrawler.entity;

import static pers.mrwangx.tools.cquptcrawler.CquptCrawler.*;

/**
 * \* Author: MrWangx
 * \* Date: 2019/9/6
 * \* Time: 12:43
 * \* Description:
 **/
public class StuCourses {

    private ListCourse[][] listCourses;

    public StuCourses(ListCourse[][] listCourses) {
        this.listCourses = listCourses;
    }

    /**
     * @param schoolweek 行课周数
     * @param weekday    星期
     * @param coursenum  第几节课
     * @return
     */
    public ListCourse getCourse(int schoolweek, int weekday, int coursenum) {
        if (!((schoolweek > 0 && schoolweek <= schoolweeks) && (weekday > 0 && weekday <= weekdays) && (coursenum > 0 && coursenum <= coursenums))) {
            return null;
        }
        ListCourse listCourse = this.listCourses[coursenum - 1][weekday - 1];
        ListCourse listCourseTemp = new ListCourse();
        listCourse.getCourses().forEach(course -> {
            if (course.getWeeksList().contains(schoolweek)) {
                listCourseTemp.addCourse(course);
            }
        });
        return listCourseTemp;
    }

    public ListCourse[][] getListCourses() {
        return listCourses;
    }
}
