package Front;

import Config.BoardConstant;
import Config.GizmoShape;
import Config.Mode;
import Config.Tools;
import Impl.BoardImpl;
import Impl.GameImpl;
import Impl.Gizmo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class BoardPanel extends JPanel {
    private int length = 200;
    private int grid;
    private GameImpl dataSource;        //数据来源是旁边的ToolPanel
    private BoardImpl board;
    private RunThread thread;
    private static Gizmo curGizmo;

    public BoardPanel(BoardImpl inboard) {
        this.board = inboard;
        grid = getGridSize();
        board.setRowHeight(grid);
        board.newWorld();

        //鼠标选择组件
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                length = getMinLength();
                int x = (int) ((e.getX() - BoardConstant.X0) / grid);
                int y = (int) ((e.getY() - BoardConstant.X0) / grid);
                int sizeRate = board.getSizeRate();
                board.setCurMode(board.getCurMode());
                curGizmo = board.getGizmo(x, y);

                if (board.getCurMode() == Mode.Shape) {
                    if (dataSource.getShape() == GizmoShape.Finger) {
                        dataSource.clear();
                    }
                    if (dataSource.getShape() == GizmoShape.Ball)
                        sizeRate = 1;
                    else if (dataSource.getShape() == GizmoShape.Paddle)
                        sizeRate = 2;
                    else if (dataSource.getShape() == GizmoShape.Track)
                        sizeRate = 2;
                    if (board.canAdd(x, y, sizeRate, dataSource.getShape())) {
                        if (dataSource.getShape() == GizmoShape.Track) {
                            Gizmo tmp = new Gizmo(x, y, sizeRate, dataSource.getShape(), dataSource.getImg());
                            Gizmo tmp1 = new Gizmo(x - 1, y, sizeRate, dataSource.getShape(), dataSource.getImg());
                            board.addComponents(tmp);
                            board.addComponents(tmp1);
                        } else {
                            Gizmo tmp = new Gizmo(x, y, sizeRate, dataSource.getShape(), dataSource.getImg());
                            board.addComponents(tmp);
                        }
                    }
                } else if (board.getCurMode() == Mode.Tool) {
                    if (dataSource.getTool() == Tools.Remove) {
                        board.removeGizmo(curGizmo);
                    } else if (dataSource.getTool() == Tools.Rotation) {
                        if (board.canRotate(x, y, curGizmo.getSizeRate(), curGizmo)) {
                            board.rotateGizmo(curGizmo);
                        }
                    } else if (dataSource.getTool() == Tools.Plus) {
                        board.addGizmo(curGizmo);
                    } else if (dataSource.getTool() == Tools.Minus) {
                        board.minusGizmo(curGizmo);
                    }
                }
                repaint();
            }
        });

        //挡板左右上下移动
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int code = e.getKeyCode();
                if (board.isBuildMode())
                    return;

                if (code == KeyEvent.VK_LEFT || code == KeyEvent.VK_RIGHT || code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
                    int dx = 0, dy = 0;
                    switch (code) {
                        case KeyEvent.VK_LEFT:
                            dx = -5;
                            break;
                        case KeyEvent.VK_RIGHT:
                            dx = 5;
                            break;
                        case KeyEvent.VK_UP:
                            dy = 5;
                            break;
                        case KeyEvent.VK_DOWN:
                            dy = -5;
                            break;
                    }
                    board.keyMove(dx, dy);
                }
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (board.getCanFocus())
                    requestFocus();
            }
        });
        repaint();
    }

    public void paint(Graphics g) {
        Image image = new BufferedImage(getWidth(), getHeight(), Image.SCALE_DEFAULT);
        image.getGraphics().drawRect(0, 0, getWidth(), getHeight());

        length = getMinLength();
        Graphics2D g2D = (Graphics2D) image.getGraphics();
        super.setBackground(new Color(84, 115, 135));
        g2D.setColor(this.getBackground());
        g2D.fill(new Rectangle(0, 0, getWidth(), getHeight()));
        drawChessTable(g2D);

        board.paintComponents(g2D, grid, length);
        g.clearRect(0, 0, getWidth(), getHeight());
        g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
    }

    public void updateScreen() {
        repaint();
    }

    private void drawChessTable(Graphics2D g2D) {
        g2D.setColor(Color.WHITE);
        grid = getGridSize();
        for (int i = 0; i <= BoardConstant.LINES; i++) {
            Line2D row = new Line2D.Double(BoardConstant.X0, BoardConstant.Y0 + grid * i, length, BoardConstant.Y0 + grid * i);
            Line2D col = new Line2D.Double(BoardConstant.X0 + grid * i, BoardConstant.Y0, BoardConstant.X0 + grid * i, length);
            g2D.draw(row);     //绘画横线
            g2D.draw(col);     //绘画纵线
        }
    }

    public void build() {
        if (board.isBuildMode()) {
            thread.interrupt();
            board.updateCompoments();
        } else {
            thread = new RunThread();
            thread.start();
            this.requestFocus();
        }
    }

    public static Gizmo getCurGizmo() {
        return curGizmo;
    }

    public BoardImpl getBoard() {
        return board;
    }

    public void newScene() {
        board.newScene();
        updateScreen();
    }

    public void loadScene(String filename) {
        board.loadScene(filename);
        updateScreen();
    }

    public int getGridSize() {
        return getMinLength() / (BoardConstant.LINES - 1);
    }

    public int getMinLength() {
        return Math.min(getHeight(), getWidth()) - 10;
    }

    class RunThread extends Thread {
        @Override
        public void run() {
            while (!board.isBuildMode()) {
                board.setStep();
                updateScreen();
                try {
                    Thread.sleep((long) (board.getTimeStep() * 1000));
                } catch (InterruptedException e) {
                    break;
                }
            }
            updateScreen();
        }
    }

}
