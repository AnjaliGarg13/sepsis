package com.nsut.spotsepsis.models;

public class GraphPointModel {
    private double xPoint;
    private double yPoint;

    public GraphPointModel(double xPoint, double yPoint) {
        this.xPoint = xPoint;
        this.yPoint = yPoint;
    }

    public double getxPoint() {
        return xPoint;
    }

    public void setxPoint(double xPoint) {
        this.xPoint = xPoint;
    }

    public double getyPoint() {
        return yPoint;
    }

    public void setyPoint(double yPoint) {
        this.yPoint = yPoint;
    }
}
