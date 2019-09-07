package pers.mrwangx.tools.cquptcrawler.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * \* Author: MrWangx
 * \* Date: 2019/9/6
 * \* Time: 13:49
 * \* Description:
 **/
public class ListCourse {

    private List<Course> courses;

    public ListCourse() {
        this.courses = new ArrayList<>();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        courses.forEach(course -> {
            builder.append(course + System.lineSeparator());
        });
        return builder.toString();
    }

    public boolean isEmpty() {
        return this.courses.isEmpty();
    }

    public int size() {
        return this.courses.size();
    }

    public boolean addCourse(Course course) {
        return this.courses.add(course);
    }

    public List<Course> getCourses() {
        return courses;
    }
}
