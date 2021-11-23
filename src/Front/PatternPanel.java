package Front;

import Impl.BoardImpl;
import Impl.GameImpl;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class PatternPanel extends JPanel {

    private JButton btnOperation = new JButton("布局模式");
    private JButton btnPlay = new JButton("游玩模式");

    public PatternPanel(BoardPanel boardPanel) {
        super.setBorder(new TitledBorder(new EtchedBorder(), "模式栏"));
        btnOperation.setBounds(30, 30, 140, 30);
        btnOperation.setFont(new Font("等线light", Font.BOLD, 20));
        btnOperation.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                BoardImpl.setBuildMode(true);
                boardPanel.build();
            }
        });

        btnPlay.setBounds(30, 75, 140, 30);
        btnPlay.setFont(new Font("等线light", Font.BOLD, 20));
        btnPlay.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                BoardImpl.setBuildMode(false);
                boardPanel.build();
                GameImpl.clear();
            }
        });

        super.add(btnOperation);
        super.add(btnPlay);

    }

}
