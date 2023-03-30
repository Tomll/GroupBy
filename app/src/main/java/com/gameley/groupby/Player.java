package com.gameley.groupby;

/**
 * Created by dongrp on 2023-03-30-0030.
 */
public class Player {

    String name;//姓名
    int gender;//性别 0：女  1：男
    int weight;//权重

    public Player(String name, int gender, int weight) {
        this.name = name;
        this.gender = gender;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public int getGender() {
        return gender;
    }

    public int getWeight() {
        return weight;
    }
}
