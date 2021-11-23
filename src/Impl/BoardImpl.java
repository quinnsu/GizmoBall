package Impl;

import Config.BoardConstant;
import Config.Mode;
import Config.GizmoShape;
import Front.ToolPanel;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.World;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.RoundRectangle2D;
import java.util.ArrayList;

public class BoardImpl {
    private World world; //创建一个模拟世界World
    // 这是jbox2d当中抽象出来的存在引力、坐标范围的一个模拟的世界
    private static final Vec2 gravity = new Vec2(0, -10);//模拟世界的重力
    private float timeStep = 1.0f / 60.0f;//模拟世界的频率
    private static int velocityIterations = 6;//速度迭代器
    private static int positionIterations = 2;//迭代次数
    private static Body ground; // 地面
    private static int rowNum = BoardConstant.LINES - 1; // 格子的行数
    private int sizeRate = 1;//物理世界与屏幕环境缩放比列

    private boolean canFocus = true; //光标是否在棋盘中
    private final static int size = 5;

    public static Mode curMode;
    private static boolean buildMode = true;

    private java.util.List<Gizmo> components = new ArrayList();
    private ToolPanel dataSource;
    private int rowHeight = 10;

    private static float angularResistForce = 1f;
    private static float linearResistForce = 1f;

    public void newWorld() {
        world = new World(gravity);
        Gizmo.setWorld(world);
        Gizmo.setRowNum(rowNum);
        for (int i = 0; i <= 1; i++) {
            for (int j = 0; j <= 1; j++)
                Gizmo.addSingleBoarder(i, j);
        }
    }

    public void paintComponents(Graphics2D g2D, int grid, int length) {
        double px, py;
        int x, y;
        int sizeRate;
        Image curImg;
        this.rowHeight = grid;
        for (int i = 0; i < components.size(); i++) {
            AffineTransform transform = (AffineTransform) g2D.getTransform().clone();
            Gizmo gizmo = components.get(i);
            if (gizmo.getBody().getUserData() == null) {
                world.destroyBody(gizmo.getBody());
                continue;
            }
            x = gizmo.getX();
            y = gizmo.getY();
            curImg = gizmo.getImg();
            sizeRate = gizmo.getSizeRate();

            if (buildMode) {
                px = Coordinate(x) + BoardConstant.X0;
                py = Coordinate(y) + BoardConstant.Y0;
                if (gizmo.getShape() == GizmoShape.Triangle) {
                    g2D.setTransform(getTransform(px + 0.5 * sizeRate * rowHeight, py + 0.5 * sizeRate * rowHeight, -gizmo.getBody().getAngle(), g2D.getTransform()));
                }
            } else {
                Vec2 position = gizmo.getBody().getPosition();
                px = position.x / Gizmo.getLength() * rowNum * grid + BoardConstant.X0;
                py = position.y / Gizmo.getLength() * rowNum * grid;
                if (gizmo.getShape() != GizmoShape.Ball) {
                    if (gizmo.getShape() == GizmoShape.Paddle) {
                        px -= gizmo.getSizeRate() * rowHeight / 2.0f;
                        py += 0.875f * rowHeight;
                    } else if (gizmo.getShape() == GizmoShape.Track) {
                        px -= 0.875 * rowHeight;
                        py += rowHeight;
                    } else {
                        px -= gizmo.getSizeRate() * rowHeight / 2.0f;
                        py += gizmo.getSizeRate() * rowHeight / 2.0f;
                    }

                } else {
                    px -= rowHeight / 4.0f;
                    py += rowHeight / 4.0f;
                }
                py = length - py;
                if (gizmo.getShape() != GizmoShape.Ball )
                    g2D.setTransform(getTransform(px + 0.5 * sizeRate * rowHeight, py + 0.5 * sizeRate * rowHeight, -gizmo.getBody().getAngle(), g2D.getTransform()));
            }

            sizeRate = gizmo.getSizeRate();

            if (gizmo.getShape() == GizmoShape.Ball) {
                g2D.drawImage(curImg, (int) px, (int) py, rowHeight / 2, rowHeight / 2, null);
            } else if (gizmo.getShape() == GizmoShape.Track) {

                g2D.setColor(new Color(138, 204, 241));
                g2D.fill(paintPaddle(px, py, sizeRate));
            } else if (gizmo.getShape() == GizmoShape.Paddle) {
                g2D.drawImage(curImg, (int) px, (int) py, sizeRate * rowHeight, sizeRate * rowHeight, null);
            } else {
                g2D.drawImage(curImg, (int) px, (int) py, sizeRate * rowHeight, sizeRate * rowHeight, null);
            }
            g2D.setTransform(transform);
        }
    }

    public void updateCompoments() {
        for (Gizmo gizmo : components) {
            gizmo.updateBody();
        }
    }

    public void keyMove(int dx, int dy) {
        for (Gizmo gizmo : components) {
            if (gizmo.getShape() == GizmoShape.Paddle) {
                gizmo.move(dx, dy);
            }
        }
    }

    public void setStep() {
        world.step(timeStep, velocityIterations, positionIterations);
    }

    private AffineTransform getTransform(double x, double y, double angle, AffineTransform transform) {
        transform.rotate(angle, x, y);
        return transform;
    }

    private RoundRectangle2D paintPaddle(double x, double y, int sizeRate) {
        double X, Y;
        double length = 0.25 * rowHeight;
        double weight = Coordinate(2);
        X = x + 0.75 * rowHeight;
        Y = y;
        RoundRectangle2D d = new RoundRectangle2D.Double(X, Y, length, weight, 0.5 * length, 0.5 * length);
        return d;
    }

    private RoundRectangle2D paintSlider(double x, double y, int sizeRate) {
        double length = Coordinate(1 * sizeRate);
        double weight = Coordinate(0.25);
        double px = x;
        double py = y + Coordinate(0.75);
        RoundRectangle2D slider = new RoundRectangle2D.Double(px, py, length, weight, 0.25 * length, 0.5 * length);
        return slider;
    }

    public void addComponents(Gizmo tmp) {
        components.add(tmp);
    }

    public static void setCurMode(Mode curMode) {
        BoardImpl.curMode = curMode;
    }

    public static Mode getCurMode() {
        return curMode;
    }

    public void addGizmo(Gizmo gizmo) {
        if (gizmo != null) {
            int sizeRate = gizmo.getSizeRate();
            gizmo.setSizeRate(sizeRate + 1);
            gizmo.updateBody();
            System.out.println("Add!");
        }
    }

    public void minusGizmo(Gizmo gizmo) {
        if (gizmo != null) {
            int sizeRate = gizmo.getSizeRate();
            gizmo.setSizeRate(sizeRate - 1);
            gizmo.updateBody();
            System.out.println("Minus!");
        }
    }

    public void rotateGizmo(Gizmo gizmo) {
        if (gizmo != null) {
            System.out.println("Rotate!");
            gizmo.setAngle(gizmo.getAngle() + Math.PI / 2);
            gizmo.updateBody();
        }
    }

    public void removeGizmo(Gizmo gizmo) {
        if (gizmo != null) {
            world.destroyBody(gizmo.getBody());
            components.remove(gizmo);
            System.out.println("Remove!");
        }
    }

    public boolean canAdd(int x, int y, int size, GizmoShape gizmoShape) {
        for (int i = x; i < x + size; i++) {
            for (int j = y; j < y + size; j++) {
                if (getGizmo(i, j) != null) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean canRotate(int x, int y, int size, Gizmo curGizmo) {
        int dx, dy;
        for (Gizmo giz : components) {
            dx = giz.getX();
            dy = giz.getY();
            if (dx >= x && dy <= y && dx <= x + size && dy >= y - size && giz.getShape() != curGizmo.getShape()) {
                return false;
            }
        }
        return true;
    }

    public Gizmo getGizmo(int x, int y) {
        for (int i = 0; i < components.size(); i++) {
            Gizmo temp = components.get(i);
            int tempX = temp.getX();
            int tempY = temp.getY();
            int sizeRate = temp.getSizeRate();
            if (x >= tempX && x < tempX + sizeRate && y >= tempY && y < tempY + sizeRate)
                return temp;
        }
        return null;
    }

    public static float getLinearResistForce() {
        return linearResistForce;
    }

    public static float getAngularResistForce() {
        return angularResistForce;
    }

    public static void setAngularResistForce(float angularResistForce) {
        BoardImpl.angularResistForce = angularResistForce;
    }

    private double Coordinate(double i)//获取真实坐标
    {
        return i * rowHeight;
    }

    public static boolean isBuildMode() {
        return buildMode;
    }

    public static void setBuildMode(boolean buildMode) {
        BoardImpl.buildMode = buildMode;
    }

    public static int getSize() {
        return size;
    }

    public boolean getCanFocus() {
        return canFocus;
    }

    public void setCanFocus(boolean canFocus) {
        this.canFocus = canFocus;
    }

    public void setTimeStep(float timeStep) {
        this.timeStep = timeStep;
    }

    public float getTimeStep() {
        return timeStep;
    }

    public int getVelocityIterations() {
        return velocityIterations;
    }

    public int getPositionIterations() {
        return positionIterations;
    }

    public void setGround(Body ground) {
        BoardImpl.ground = ground;
    }

    public Body getGround() {
        return ground;
    }

    public void setRowNum(int rowNum) {
        BoardImpl.rowNum = rowNum;
    }

    public int getRowNum() {
        return rowNum;
    }

    public void setRowHeight(int rowHeight) {
        this.rowHeight = rowHeight;
    }

    public void setSizeRate(int sizeRate) {
        this.sizeRate = sizeRate;
    }

    public int getSizeRate() {
        return sizeRate;
    }

    //文件系统的操作--新建、保存和加载
    public void newScene() {
        newWorld();
        components.clear();
    }

    public void saveScene(String fileName) {
        FileImpl.save(components, fileName);
    }

    public void loadScene(String fileName) {
        World tmp_world = world;
        newWorld();
        java.util.List<Gizmo> list = FileImpl.load(fileName);
        if (components == null) {
            world = tmp_world;
        } else {
            components = list;
        }
    }

}
