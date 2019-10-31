import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;


class DrawFrame extends JFrame{
    public static final int HEIGHT = 800;
    public static final int WIDTH = 400;

    private GameBoard panel = new GameBoard();
    private TestDraw panel2 = new TestDraw();

    public static void main(String[] args){
        DrawFrame frame = new DrawFrame();
        frame.show();
        // System.out.println(player.getPoints());
    }
    public DrawFrame(){
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        add(panel);
        setResizable(true);
        setVisible(true);
    }
}
class TestDraw extends JPanel{
    Graphics2D g2;
    private Player player;

    public TestDraw(){
        setSize(50,50);
        repaint();
    }
    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        player = new Player();
        player.draw(g2);
    }
}
