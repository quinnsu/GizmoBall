import Front.*;
import Impl.*;

import javax.swing.*;
import java.awt.*;

public class App {
    public static JFrame jFrame = new JFrame("Gizmo Ball!");
    public static GridBagLayout gridBagLayout = new GridBagLayout();      //放组件的动态矩形单元网格
    public static GridBagConstraints constraints = new GridBagConstraints();
    public static BoardImpl board = new BoardImpl();

    public static BoardPanel boardPanel = new BoardPanel(board);
    public static ComponentPanel componentPanel = new ComponentPanel();   //组件栏
    public static ToolPanel toolPanel = new ToolPanel();       //工具栏
    public static PatternPanel patternPanel = new PatternPanel(boardPanel);   //模式栏

    public static void main(String[] args) {

        jFrame.setSize(800, 600);
        jFrame.setLayout(gridBagLayout);
        constraints.fill = GridBagConstraints.BOTH;     //组件自动填满分配的格子空间
        constraints.weightx = 0.0;
        constraints.gridwidth = GridBagConstraints.REMAINDER;   //组件自动占据本行所有剩余空间

        jFrame.setJMenuBar(new MenuPanel(boardPanel));

        ChessBoardInit();       //左侧区域
        ToolBoxInit();          //右侧区域

        jFrame.setResizable(false);
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.pack();      //调整窗口的大小，以适合子组件的preferred size和布局
        jFrame.setVisible(true);
    }

    public static void ChessBoardInit() {
        boardPanel.setPreferredSize(new Dimension(600, 600));
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 4;
        jFrame.add(boardPanel, constraints);
    }

    //一个界面只能有一个JFrame窗体组件，但是可以有多个JPanel面板组件
    public static void ToolBoxInit() {
        componentPanel.setPreferredSize(new Dimension(200, 300)); //组件栏
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        jFrame.add(componentPanel, constraints);

        toolPanel.setPreferredSize(new Dimension(200, 180));
        constraints.gridx = 1;
        constraints.gridy = 1;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        jFrame.add(toolPanel, constraints);

        patternPanel.setPreferredSize(new Dimension(200, 120));
        constraints.gridx = 1;
        constraints.gridy = 2;
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        jFrame.add(patternPanel, constraints);
    }
}