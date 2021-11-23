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

/**
 * 对左侧游戏界面的设置、操作监听
 *
 * @author 1
 */
public class BoardPanel extends JPanel {
    private int length = 200;
    private int grid;
    private final BoardImpl board;
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
                int x = (e.getX() - BoardConstant.X0) / grid;
                int y = (e.getY() - BoardConstant.X0) / grid;
                int sizeRate = board.getSizeRate();
                BoardImpl.setCurMode(BoardImpl.getCurMode());
                curGizmo = board.getGizmo(x, y);

                if (BoardImpl.getCurMode() == Mode.Shape) {
                    if (GameImpl.getShape() == GizmoShape.Finger) {
                        GameImpl.clear();
                    }
                    switch (GameImpl.getShape()) {
                        case Ball:
                            sizeRate = 1;
                            break;
                        case Paddle:
                        case Track:
                            sizeRate = 2;
                            break;
                        default:
                            break;
                    }
                    if (board.canAdd(x, y, sizeRate, GameImpl.getShape())) {
                        Gizmo tmp = new Gizmo(x, y, sizeRate, GameImpl.getShape(), GameImpl.getImg());
                        if (GameImpl.getShape() == GizmoShape.Track) {
                            Gizmo tmp1 = new Gizmo(x - 1, y, sizeRate, GameImpl.getShape(), GameImpl.getImg());
                            board.addComponents(tmp);
                            board.addComponents(tmp1);
                        } else {
                            board.addComponents(tmp);
                        }
                    }
                } else if (BoardImpl.getCurMode() == Mode.Tool) {
                    if (GameImpl.getTool() == Tools.Remove) {
                        board.removeGizmo(curGizmo);
                    } else if (GameImpl.getTool() == Tools.Rotation) {
                        if (board.canRotate(x, y, curGizmo.getSizeRate(), curGizmo)) {
                            board.rotateGizmo(curGizmo);
                        }
                    } else if (GameImpl.getTool() == Tools.Plus) {
                        board.addGizmo(curGizmo);
                    } else if (GameImpl.getTool() == Tools.Minus) {
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
                if (BoardImpl.isBuildMode()) {
                    return;
                }

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
                        default:
                            break;
                    }
                    board.keyMove(dx, dy);
                }
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                if (board.getCanFocus()) {
                    requestFocus();
                }
            }
        });
        repaint();
    }

    @Override
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
            //绘画横线
            g2D.draw(row);
            //绘画纵线
            g2D.draw(col);
        }
    }

    public void build() {
        if (BoardImpl.isBuildMode()) {
            thread.interrupt();
            board.updateComponents();
        } else {
            thread = new RunThread();
            thread.start();
            this.requestFocus();
        }
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
            while (!BoardImpl.isBuildMode()) {
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
