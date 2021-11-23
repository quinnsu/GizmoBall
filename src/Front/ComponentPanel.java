package Front;

import Config.GizmoShape;
import Config.Mode;
import Impl.GameImpl;
import Impl.ToolButton;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 对右侧组件栏的设置
 *
 * @author 1
 */
public class ComponentPanel extends JPanel {
    List<ToolButton> components;

    public ComponentPanel() {
        super.setBorder(new TitledBorder(new EtchedBorder(), "组件栏"));

        ToolButton btnFinger = readImg("pic/finger.png", GizmoShape.Finger);
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(btnFinger);
        ToolButton btnBall = readImg("pic/pinball.png", GizmoShape.Ball);
        btnGroup.add(btnBall);
        ToolButton btnCircle = readImg("pic/ball.png", GizmoShape.Circle);
        btnGroup.add(btnCircle);
        ToolButton btnAbsorber = readImg("pic/absorber.png", GizmoShape.Absorber);
        btnGroup.add(btnAbsorber);
        ToolButton btnSquare = readImg("pic/square.png", GizmoShape.Square);
        btnGroup.add(btnSquare);
        ToolButton btnTriangle = readImg("pic/triangle.png", GizmoShape.Triangle);
        btnGroup.add(btnTriangle);
        ToolButton btnTrack = readImg("pic/track.png", GizmoShape.Track);
        btnGroup.add(btnTrack);
        ToolButton btnPaddle = readImg("pic/paddle.png", GizmoShape.Paddle);
        btnGroup.add(btnPaddle);
        //将finger设为初始默认选项
        btnFinger.setSelected(true);
        //显示在面板组件上
        super.add(btnFinger);
        super.add(btnBall);
        super.add(btnCircle);
        super.add(btnAbsorber);
        super.add(btnSquare);
        super.add(btnTriangle);
        super.add(btnTrack);
        super.add(btnPaddle);

        //将button组成一个队列，方便重载button类的属性
        components = new ArrayList();
        components.add(btnFinger);
        components.add(btnBall);
        components.add(btnCircle);
        components.add(btnAbsorber);
        components.add(btnSquare);
        components.add(btnTriangle);
        components.add(btnTrack);
        components.add(btnPaddle);

        GameImpl tool = new GameImpl();
        tool.addButtonActionListener(components, Mode.Shape);

    }

    private ToolButton readImg(String imgPath, GizmoShape gizmoShape) {
        ImageIcon img = new ImageIcon(imgPath);
        img.setImage(img.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT));
        ToolButton btn = new ToolButton(img, gizmoShape);
        btn.setImg(img.getImage());
        btn.setEnabled(true);
        return btn;
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
    }
}
