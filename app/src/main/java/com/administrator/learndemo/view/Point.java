package com.administrator.learndemo.view;

public class Point {
    public int x;
    public int y;

    public Point() {}

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(Point src) {
        this.x = src.x;
        this.y = src.y;
    }

    /**
     * Set the point's x and y coordinates
     */
    public void set(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
