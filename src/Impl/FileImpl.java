package Impl;

import javax.swing.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 实现文件存储、新建、读取
 *
 * @author 1
 */
public class FileImpl {
    /**
     * 保存文件
     */
    public static void save(List<Gizmo> components, String path) {
        List<FileGizmo> list = new ArrayList();
        for (Gizmo g : components) {
            FileGizmo fg = new FileGizmo();
            fg.setAngle(g.getAngle());
            fg.setShape(g.getShape());
            fg.setSizeRate(g.getSizeRate());
            fg.setX(g.getX());
            fg.setY(g.getY());
            list.add(fg);
        }
        writeObjectToFile(list, path);
    }

    /**
     * 读取文件
     */
    public static List<Gizmo> load(String path) {
        List<FileGizmo> list = (List<FileGizmo>) readObjectFromFile(path);
        List<Gizmo> components = new ArrayList<>();
        ImageIcon pinball = new ImageIcon("pic/pinball.png");
        ImageIcon circle = new ImageIcon("pic/ball.png");
        ImageIcon absorber = new ImageIcon("pic/absorber.png");
        ImageIcon square = new ImageIcon("pic/square.png");
        ImageIcon triangle = new ImageIcon("pic/triangle.png");
        ImageIcon track = new ImageIcon("pic/track.png");
        ImageIcon curve = new ImageIcon("pic/curve.png");
        ImageIcon paddle = new ImageIcon("pic/paddle.png");

        for (FileGizmo g : list) {
            switch (g.getShape()) {
                case Paddle:
                    g.setImg(paddle.getImage());
                    break;
                case Square:
                    g.setImg(square.getImage());
                    break;
                case Circle:
                    g.setImg(circle.getImage());
                    break;
                case Track:
                    g.setImg(track.getImage());
                    break;
                case Triangle:
                    g.setImg(triangle.getImage());
                    break;
                case Absorber:
                    g.setImg(absorber.getImage());
                    break;
                case Ball:
                    g.setImg(pinball.getImage());
                    break;
                case Curve:
                    g.setImg(curve.getImage());
                    break;
                default:
                    break;
            }
            Gizmo gizmo = new Gizmo(g.getX(), g.getY(), g.getSizeRate(), g.getShape(), g.getImg());
            if (g.getAngle() != 0) {
                gizmo.setAngle(g.getAngle());
                gizmo.updateBody();
            }
            components.add(gizmo);
        }
        return components;
    }

    /**
     * 依次读取文件中的设置和组件
     */
    public static Object readObjectFromFile(String path) {
        Object temp = null;
        File file = new File(path);
        FileInputStream in;
        try {
            in = new FileInputStream(file);
            ObjectInputStream objIn = new ObjectInputStream(in);
            temp = objIn.readObject();
            objIn.close();
            System.out.println("Read Success!");
        } catch (IOException e) {
            System.out.println("Read Failure");
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return temp;
    }

    /**
     * 依次把布局的设置和组件写入
     */
    public static void writeObjectToFile(Object obj, String path) {
        File file = new File(path);
        FileOutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream objOut = new ObjectOutputStream(out);
            objOut.writeObject(obj);
            objOut.flush();
            objOut.close();
            System.out.println("Write Success!");
        } catch (IOException e) {
            System.out.println("Write Failure");
            e.printStackTrace();
        }
    }
}
