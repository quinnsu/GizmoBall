package Front;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.io.File;

public class MenuPanel extends JMenuBar {
    private JMenu fileMenu = new JMenu("文件");
    private JMenuItem newGame = new JMenuItem("新建游戏");
    private JMenuItem saveGame = new JMenuItem("保存游戏");
    private JMenuItem openGame = new JMenuItem("读取游戏");

    public MenuPanel(BoardPanel boardPanel) {
        fileMenu.add(newGame);
        fileMenu.add(saveGame);
        fileMenu.add(openGame);

        super.add(fileMenu);

        newGame.addActionListener(e -> {
            boardPanel.newScene();
        });
        saveGame.addActionListener(e -> {
            boardPanel.getBoard().setCanFocus(false);
            JFileChooser chooser = new JFileChooser();
            chooser.showSaveDialog(this);
            File file = chooser.getSelectedFile();
            String fileName = file.getAbsolutePath();
            if (!fileName.endsWith(".psc")) {
                fileName += ".psc";
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

    @Override
    public boolean accept(File f) {
        if (f.isDirectory())
            return true;
        return f.getName().endsWith(".psc");
    }

    @Override
    public String getDescription() {
        return ".psc";
    }
}
