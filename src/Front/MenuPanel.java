package Front;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * 文件系统的设置，采用自带JMenuBar类
 *
 * @author 1
 */
public class MenuPanel extends JMenuBar {
    private final String fileEnd = ".psc";

    public MenuPanel(BoardPanel boardPanel) {
        JMenu fileMenu = new JMenu("文件");
        JMenuItem newGame = new JMenuItem("新建游戏");
        fileMenu.add(newGame);
        JMenuItem saveGame = new JMenuItem("保存游戏");
        fileMenu.add(saveGame);
        JMenuItem openGame = new JMenuItem("读取游戏");
        fileMenu.add(openGame);

        super.add(fileMenu);

        newGame.addActionListener(e -> boardPanel.newScene());
        saveGame.addActionListener(e -> {
            boardPanel.getBoard().setCanFocus(false);
            JFileChooser chooser = new JFileChooser();
            chooser.showSaveDialog(this);
            File file = chooser.getSelectedFile();
            String fileName = file.getAbsolutePath();
            if (!fileName.endsWith(fileEnd)) {
                fileName += fileEnd;
            }
            boardPanel.getBoard().saveScene(fileName);
            boardPanel.getBoard().setCanFocus(true);
        });
        openGame.addActionListener(e -> {
            boardPanel.getBoard().setCanFocus(false);
            JFileChooser chooser = new JFileChooser();
            chooser.setFileFilter(new PinballFileFilter());
            chooser.showOpenDialog(this);
            File file = chooser.getSelectedFile();
            boardPanel.loadScene(file.getAbsolutePath());
            boardPanel.getBoard().setCanFocus(true);
        });
    }
}


class PinballFileFilter extends FileFilter {
    // 筛选出后缀名为psc的文件

    String fileEnd = ".psc";

    @Override
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }
        return f.getName().endsWith(fileEnd);
    }

    @Override
    public String getDescription() {
        return ".psc";
    }
}
