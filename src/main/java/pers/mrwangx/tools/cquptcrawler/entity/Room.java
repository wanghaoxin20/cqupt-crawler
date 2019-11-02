package pers.mrwangx.tools.cquptcrawler.entity;

import pers.mrwangx.tools.cquptcrawler.annotation.Display;

/**
 * \* Author: MrWangx
 * \* Date: 2019/11/2
 * \* Time: 14:50
 * \* Description:
 **/
@Display.Separator(",")
public class Room {

    @Display("教室名")
    private String name;
    @Display("可容纳人数")
    private int capacity;

    public Room() {
    }

    public Room(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}
