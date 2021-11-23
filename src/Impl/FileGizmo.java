package Impl;

import Config.GizmoShape;

import java.awt.*;
import java.io.Serializable;

public class FileGizmo implements Serializable {
    private int x;
    private int y;
    private int sizeRate = 1;
    private GizmoShape gizmoShape;
    private Image img;
    private double angle = 0.0;

    public Image getImg() {
        return img;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSizeRate() {
        return sizeRate;
    }

    public void setSizeRate(int sizeRate) {
        this.sizeRate = sizeRate;
    }

    public GizmoShape getShape() {
        return gizmoShape;
    }

    public void setShape(GizmoShape gizmoShape) {
        this.gizmoShape = gizmoShape;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
