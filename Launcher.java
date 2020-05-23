import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import java.awt.Color;
import java.awt.Font;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.Random;

public class Launcher {

    static int TILE_SIZE = 200;
    static int TILE_MARGIN = 20;

    static int WIDTH, HEIGHT;

    public static void main(String[] args) {

        WIDTH = ((TILE_SIZE + (TILE_MARGIN)) * 4) - 30;
        HEIGHT = ((TILE_SIZE + (TILE_MARGIN)) * 4) - 10;

        new Window(WIDTH, HEIGHT, TILE_MARGIN, TILE_SIZE, "Game of 15");
    }
}

class Window extends JFrame implements ActionListener {

    // ------------------------------------------------------------------------//

    // CREATING THE WINDOW//

    // ------------------------------------------------------------------------//

    int index = 0;

    Timer timer = new Timer(1, this);

    JPanel background = new JPanel();
    Tile[] tiles = new Tile[15];
    Vec2D emptyPosition;

    public Window(int width, int height, int margin, int tileSize, String title) {
        setTitle(title);
        setSize(width, height);

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setFocusable(true);
        timer.start();

        setVisible(true);

        emptyPosition = new Vec2D(3, 3);
        setContent(width, height, tileSize, margin);
    }

    void setContent(int BACKGROUND_WIDTH, int BACKGROUND_HEIGHT, int TILE_SIZE, int MARGIN) {
        background.setBounds(0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
        background.setBackground(Color.BLACK);
        background.setLayout(null);

        // ------------------------------------------------------------------------//

        // CREATING TILES//

        // ------------------------------------------------------------------------//

        for (int rows = 0; rows < 4; rows++) {
            for (int collums = 0; collums < 4; collums++) {
                if (rows == 4 && collums == 4) {
                    index = 14;
                    break;
                } else {
                    try {
                        tiles[index] = new Tile(TILE_SIZE, MARGIN, collums, rows, index + 1, this);
                        tiles[index].getEmptyPosition(emptyPosition);
                        background.add(tiles[index]);
                    } catch (Exception e) {
                    }
                    System.out.println(index);
                    index++;
                }
            }
        }

        add(background);
        scramble();
    }

    void scramble() {

        // ------------------------------------------------------------------------//

        //SCRAMBLING THE TILES//

        // ------------------------------------------------------------------------//

        for (int row = 0; row < 4; row++) {
            for (int collum = 0; collum < 4; collum++) {
                Random random = new Random();
                int randomTileID = random.nextInt(tiles.length);

                if (!tiles[randomTileID].isScrambeled) {
                    tiles[randomTileID].setPosition(row, collum);
                    tiles[randomTileID].isScrambeled = true;
                } 
                
                else {
                    while (tiles[randomTileID].isScrambeled) {
                        randomTileID = random.nextInt(tiles.length);

                        if (!tiles[randomTileID].isScrambeled) {
                            tiles[randomTileID].setPosition(row, collum);
                            tiles[randomTileID].isScrambeled = true;
                            break;
                        }
                    }
                }
            }
        }
    }

    void changedPosition(int x, int y) {
        emptyPosition = new Vec2D(x, y);
        for (int i = 0; i < tiles.length; i++) {
            tiles[i].getEmptyPosition(emptyPosition);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}

//------------------------------------------------------------------------//

//TILE CLASS//

//------------------------------------------------------------------------//

class Tile extends JPanel implements MouseListener {

    int MARGIN, TILE_SIZE;
    boolean isScrambeled;

    int X;
    int Y;

    Vec2D emptyPosition;
    Window WINDOW;

    JLabel number = new JLabel("", null, JLabel.HORIZONTAL);

    public Tile(int TILE_SIZE, int MARGIN, int X, int Y, int INDEX, Window WINDOW) {

        this.X = X;
        this.Y = Y;

        int NEW_X = X * TILE_SIZE + ((MARGIN / 2) * X) + MARGIN / 2;
        int NEW_Y = Y * TILE_SIZE + ((MARGIN / 2) * Y) + MARGIN / 2;

        this.MARGIN = MARGIN;
        this.TILE_SIZE = TILE_SIZE;
        this.WINDOW = WINDOW;

        setBounds(NEW_X, NEW_Y, TILE_SIZE, TILE_SIZE);

        setLayout(null);
        setBackground(Color.RED);

        addMouseListener(this);

        setLabel(INDEX);
    }

    void getEmptyPosition(Vec2D pos) {
        emptyPosition = pos;
    }

    void setLabel(int INDEX) {
        number.setBounds(0, 0, TILE_SIZE, TILE_SIZE);
        number.setText(INDEX + "");
        number.setForeground(Color.WHITE);

        number.setFont(new Font("Monospaced", Font.BOLD, 60));

        add(number);
    }

    void setPosition(int X, int Y) {
        int NEW_X = X * TILE_SIZE + ((MARGIN / 2) * X) + MARGIN / 2;
        int NEW_Y = Y * TILE_SIZE + ((MARGIN / 2) * Y) + MARGIN / 2;

        this.X = X;
        this.Y = Y;

        setBounds(NEW_X, NEW_Y, TILE_SIZE, TILE_SIZE);
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) {

        //------------------------------------------------------------------------//

        //MOVING THE TILES//

        //------------------------------------------------------------------------//

        System.out.println("Clicked");

        System.out.println(X);
        System.out.println(Y);

        if (X - emptyPosition.x == 0) {
            if (Y - emptyPosition.y == -1) {
                int new_y = Y;
                setPosition(X, emptyPosition.y);
                WINDOW.changedPosition(X, new_y);
            }
            else if (Y-emptyPosition.y == 1){
                int new_y = Y;
                setPosition(X, emptyPosition.y);
                WINDOW.changedPosition(X, new_y);
            }
        }

        if (Y - emptyPosition.y == 0) {
            if (X - emptyPosition.x == -1) {
                int new_x = X;
                setPosition(emptyPosition.x, Y);
                WINDOW.changedPosition(new_x,Y);
            }
            else if (X-emptyPosition.x == 1){
                int new_x = X;
                setPosition(emptyPosition.x, Y);
                WINDOW.changedPosition(new_x,Y);
            }
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {}

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}


//------------------------------------------------------------------------//

//STORING THE EMPTY POSITION//

//------------------------------------------------------------------------//

class Vec2D {
    int x;
    int y;

    public Vec2D(int x, int y) {
        this.x = x;
        this.y = y;
    }
}
