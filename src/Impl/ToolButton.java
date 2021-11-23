package Impl;

import Config.GizmoShape;
import Config.Tools;
import javax.swing.*;
import java.awt.*;


public class ToolButton extends JButton {
    private Tools tool;
    private GizmoShape gizmoShape;
    private Image img;

    public ToolButton(Icon icon, GizmoShape gizmoShape){
        super(icon);
        super.setContentAreaFilled(false);
        super.setBorderPainted(false);
        this.gizmoShape = gizmoShape;
    }

    public ToolButton(Icon icon, Tools tool){
        super(icon);
        super.setContentAreaFilled(false);
        super.setBorderPainted(false);
        this.tool = tool;
    }

    public void setImg(Image img) {
        this.img = img;
    }

    public Image getImg() {
        return img;
    }

    public GizmoShape getShape() {
        return gizmoShape;
    }

    public Tools getTool() {
        return tool;
    }
}
