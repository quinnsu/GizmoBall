package Front;

import Config.Mode;
import Config.Tools;
import Impl.GameImpl;
import Impl.ToolButton;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 对右侧工具栏的设置
 *
 * @author 1
 */
public class ToolPanel extends JPanel {

    List<ToolButton> components;

    public ToolPanel() {
        super.setBorder(new TitledBorder(new EtchedBorder(), "工具栏"));

        ToolButton btnRotate = readImg("pic/rotate.png", Tools.Rotation);
        ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(btnRotate);
        ToolButton btnRemove = readImg("pic/delete.png", Tools.Remove);
        btnGroup.add(btnRemove);
        ToolButton btnMinus = readImg("pic/minus.png", Tools.Minus);
        btnGroup.add(btnMinus);
        ToolButton btnPlus = readImg("pic/plus.png", Tools.Plus);
        btnGroup.add(btnPlus);

        super.add(btnRotate);
        super.add(btnRemove);
        super.add(btnMinus);
        super.add(btnPlus);

        components = new ArrayList<>();
        components.add(btnRotate);
        components.add(btnRemove);
        components.add(btnMinus);
        components.add(btnPlus);

        GameImpl tool = new GameImpl();
        tool.addButtonActionListener(components, Mode.Tool);

    }

    private ToolButton readImg(String imgPath, Tools tool) {
        ImageIcon img = new ImageIcon(imgPath);
        img.setImage(img.getImage().getScaledInstance(55, 55, Image.SCALE_DEFAULT));
        ToolButton btn = new ToolButton(img, tool);
        btn.setEnabled(true);
        return btn;
    }
}
