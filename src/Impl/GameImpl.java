package Impl;

import Config.GizmoShape;
import Config.Mode;
import Config.Tools;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

/**
 * 实现左侧游戏界面的辅助类
 *
 * @author 1
 */
public class GameImpl {
    private static GizmoShape gizmoShape;
    private static Mode curMode;
    private static Tools tool;
    private static Image img;

    public void addButtonActionListener(List<ToolButton> components, Mode mode) {
        for (ToolButton button : components) {
            button.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseReleased(MouseEvent e) {
                    if (BoardImpl.isBuildMode()) {
                        curMode = mode;
                        BoardImpl.setCurMode(curMode);
                        gizmoShape = button.getShape();
                        tool = button.getTool();
                        img = button.getImg();
                    }
                }
            });
        }
    }

    public static Image getImg() {
        return img;
    }

    public static Tools getTool() {
        return tool;
    }

    public static void clear() {
        curMode = null;
        gizmoShape = null;
        tool = null;
        img = null;
    }

    public static GizmoShape getShape() {
        return gizmoShape;
    }

}
