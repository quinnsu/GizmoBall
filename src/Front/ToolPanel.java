package Front;

import Impl.ToolButton;
import Config.Mode;
import Config.Tools;
import Impl.BoardImpl;
import Impl.GameImpl;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ToolPanel extends JPanel {
    private ToolButton btnRotate = readImg("pic/rotate.png", Tools.Rotation);
    private ToolButton btnRemove = readImg("pic/delete.png", Tools.Remove);
    private ToolButton btnPlus = readImg("pic/plus.png", Tools.Plus);
    private ToolButton btnMinus = readImg("pic/minus.png", Tools.Minus);

    List<ToolButton> compoments;
    private GameImpl tool = new GameImpl();
    private ButtonGroup btnGroup = new ButtonGroup();

    public ToolPanel(BoardImpl board) {
        super.setBorder(new TitledBorder(new EtchedBorder(), "工具栏"));

        btnGroup.add(btnRotate);
        btnGroup.add(btnRemove);
        btnGroup.add(btnMinus);
        btnGroup.add(btnPlus);

        super.add(btnRotate);
        super.add(btnRemove);
        super.add(btnMinus);
        super.add(btnPlus);

        compoments = new ArrayList<ToolButton>();
        compoments.add(btnRotate);
        compoments.add(btnRemove);
        compoments.add(btnMinus);
        compoments.add(btnPlus);

        tool.addButtonActionListener(compoments, Mode.Tool);

    }

    private ToolButton readImg(String imgPath, Tools tool) {
        ImageIcon img = new ImageIcon(imgPath);
        img.setImage(img.getImage().getScaledInstance(55, 55, Image.SCALE_DEFAULT));
        ToolButton btn = new ToolButton(img, tool);
        btn.setEnabled(true);
        return btn;
    }
}
