package Front;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import Impl.ToolButton;

import Config.Mode;
import Config.GizmoShape;
import Impl.GameImpl;

public class ComponentPanel extends JPanel {

    private ToolButton btnFinger = readImg("pic/finger.png", GizmoShape.Finger);
    private ToolButton btnBall = readImg("pic/pinball.png", GizmoShape.Ball);
    private ToolButton btnCircle = readImg("pic/ball.png", GizmoShape.Circle);
    private ToolButton btnAbsorber = readImg("pic/absorber.png", GizmoShape.Absorber);
    private ToolButton btnSquare = readImg("pic/square.png", GizmoShape.Square);
    private ToolButton btnTriangle = readImg("pic/triangle.png", GizmoShape.Triangle);
    private ToolButton btnTrack = readImg("pic/track.png", GizmoShape.Track);
    private ToolButton btnTrack2 = readImg("pic/curve.png", GizmoShape.Curve);
    private ToolButton btnPaddle = readImg("pic/paddle.png", GizmoShape.Paddle);
    private ToolButton btnPaddle2 = readImg("pic/paddle.png", GizmoShape.Paddle);

    private ButtonGroup btnGroup = new ButtonGroup();
    List<ToolButton> compoments;
    private GameImpl tool = new GameImpl();

    public ComponentPanel() {
        super.setBorder(new TitledBorder(new EtchedBorder(), "组件栏"));

        btnGroup.add(btnFinger);
        btnGroup.add(btnBall);
        btnGroup.add(btnCircle);
        btnGroup.add(btnAbsorber);
        btnGroup.add(btnSquare);
        btnGroup.add(btnTriangle);
        btnGroup.add(btnTrack);
        btnGroup.add(btnTrack2);
        btnGroup.add(btnPaddle);
        btnGroup.add(btnPaddle2);

        btnFinger.setSelected(true);    //将finger设为初始默认选项

        super.add(btnFinger);       //显示在面板组件上
        super.add(btnBall);
        super.add(btnCircle);
        super.add(btnAbsorber);
        super.add(btnSquare);
        super.add(btnTriangle);
        super.add(btnTrack);
        super.add(btnTrack2);
        super.add(btnPaddle);
        super.add(btnPaddle2);

        //将button组成一个队列，方便重载button类的属性
        compoments = new ArrayList();
        compoments.add(btnFinger);
        compoments.add(btnBall);
        compoments.add(btnCircle);
        compoments.add(btnAbsorber);
        compoments.add(btnSquare);
        compoments.add(btnTriangle);
        compoments.add(btnTrack);
        compoments.add(btnTrack2);
        compoments.add(btnPaddle);
        compoments.add(btnPaddle2);

        tool.addButtonActionListener(compoments, Mode.Shape);

    }

    private ToolButton readImg(String imgPath, GizmoShape gizmoShape) {
        ImageIcon img = new ImageIcon(imgPath);
        img.setImage(img.getImage().getScaledInstance(40, 40, Image.SCALE_DEFAULT));
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
