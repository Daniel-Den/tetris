import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
import java.util.concurrent.*;
import static java.util.concurrent.TimeUnit.*;


public class TetrisFrame extends JFrame {
    public static final int HEIGHT = 768;
    public static final int WIDTH = 620;

    private GamePanel panel = new GamePanel();
    private GameBoard board = new GameBoard();

    public TetrisFrame(){
        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(board);
        //board.add(panel, BorderLayout.EAST);
        setResizable(true);
        setVisible(true);
        displayMenu();
    }
    public void displayMenu(){
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(new GameMenu());
        setJMenuBar(menuBar);
    }
    /**
    This private class creates the GameMenu as it extends a JMenu. It only has a
    constructor that creates the Pause, Start, and Quit selections for the menu.
    */
    private class GameMenu extends JMenu{
        /**
        The constructor creates the menu and its different menu items. And the
        actions that will occur when the items are selected. Finally it adds them
        to the GameMenu.
        */
        public GameMenu(){
            super("Game");
            JMenuItem startGameMI = new JMenuItem("Start", 'S');
            startGameMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK));
            JMenuItem pauseMI = new JMenuItem("Pause", 'P');
            pauseMI.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P, InputEvent.CTRL_MASK));
            JMenuItem quitMI = new JMenuItem("Quit");
            JMenuItem restartMI = new JMenuItem("Restart");
            JMenuItem soundMI = new JMenuItem("Sound (On/Off)");
            startGameMI.addActionListener(new ActionListener(){
                /**
                The action performed when the start item is selected, and it is
                in the panel class and is used here.
                @param e and ActionEvent which is when the "start" is selected
                */
                public void actionPerformed(ActionEvent e){
                    if(board.gameStarted()){
                        board.resumeGame();
                    }else{
                        board.startGame();
                    }
                }
            });
            pauseMI.addActionListener(new ActionListener(){
                /**
                The action performed when the pause item is selected, and it is
                in the panel class and is used here.
                @param e and ActionEvent which is when the "pause" is selected
                */
                public void actionPerformed(ActionEvent e){
                    board.pauseGame();
                }
            });

            quitMI.addActionListener(new ActionListener(){
                /**
                The action performed when the quit item is selected, and it just
                exits the program.
                @param e and ActionEvent which is when the "pause" is selected
                */
                public void actionPerformed(ActionEvent e){
                    System.exit(0);
                }
            });
            restartMI.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    board.startGame();
                }
            });

            soundMI.addActionListener(new ActionListener(){
                public void actionPerformed(ActionEvent e){
                    if(board.musicOn){
                        board.startMusic();
                        board.changeSound();
                    }else{
                        board.stopMusic();
                        board.changeSound();
                    }
                }
            });

            add(restartMI);
            add(startGameMI);
            add(pauseMI);
            add(quitMI);
            add(soundMI);
        }
    }

}


class TestDraw extends JPanel{
    Graphics2D g2;
    private Player player;
    private GameBoard board;

    public TestDraw(){
        repaint();
    }

    public void paintComponent(Graphics g){
        Graphics2D g2 = (Graphics2D) g;
        player = new Player();
        board = new GameBoard();
        board.draw(g2);
        player.draw(g2);
    }
}
