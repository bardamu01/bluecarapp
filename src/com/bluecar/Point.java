package com.bluecar;

import static java.lang.Math.hypot;

public class Point{
    public float x;
    public float y;

    public Point(float x, float y){
        this.x = x;
        this.y = y;
    }

    public double distance(Point another){
       return hypot((this.x - another.x), (this.y - another.y));
    }

    public boolean equals(java.lang.Object o) {
        Point p = (Point) o;
        return p.x == x && p.y == y;
    }
}
