import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.io.*;
import java.util.*;
import javax.imageio.*;
import javax.swing.*;

/**
 * The class that creates the panel in which the game is played.
 * @author OOD'19
 */
class GamePanel extends JPanel{
    //private final ScheduledExecutorService gameExec = Executors.newScheduledThreadPool(2);

    private GameBoard board;
    private Player player;
    private javax.swing.Timer timer;
    private boolean gameStarted = false;
    private FallSpeed speed;
    private int highScore;
    private JTextArea pointsLine;
    private JTextArea linesClearedLine;
    private JTextArea highScoreLine;
    //private Queue<BlockSet> blockSetQueue = new Queue<BlockSet>();
    GridBagConstraints constraints = new GridBagConstraints();
    /**
     * The constructor for the class. The constructor creates a GridBagLayout that displays the game board array and other aspects of the game.
     * These include the hold tile, next in line, the current points, the highscore, the number of lines cleared, and the player icon.
     */
    public GamePanel(){

        pointsLine = new JTextArea();
        pointsLine.setEditable(true);
        linesClearedLine = new JTextArea();

        linesClearedLine.setEditable(false);
        highScoreLine = new JTextArea();
        highScoreLine.setEditable(false);

        setLayout(new GridBagLayout());
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.fill = GridBagConstraints.BOTH;

        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        //addGB(new JPanel(hold), 0, 0);
        constraints.gridwidth = 2;
        constraints.gridheight = 5;
        //addGB(gameBoard, 1, 0);
        constraints.gridwidth = 1;
        constraints.gridheight = 5;
        //addGB(new JPanel(nextInLine), 3, 0);
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        addGB(pointsLine, 0, 1);
        pointsLine.setText(" Points:\n " + Integer.toString(0));
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        addGB(linesClearedLine, 0, 2);
        linesClearedLine.setText(" Lines Cleared:\n " + Integer.toString(0));
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        addGB(highScoreLine, 0, 3);
        highScoreLine.setText(" High Score:\n " + Integer.toString(0));
        constraints.gridwidth = 1;
        constraints.gridheight = 1;
        /*
        addGB(playerDraw, 0, 4);
        addGB(queueDrawFirst, 0, 5);
        addGB(queueDrawSecond, 0, 6);
        addGB(queueDrawThird, 0, 7);
        */

        //setPreferredSize(new Dimension(100, 100));
        this.setBorder(BorderFactory.createEtchedBorder(1, Color.GREEN, Color.BLACK));
    }

    /**
    This method allow components to be added to the GridBagLayout
    @param Component component
    @param int x
    @param int y
    @return void
    */
    void addGB(Component component, int x, int y){
        constraints.gridx = x;
        constraints.gridy = y;
        add(component, constraints);
    }

    /**
     * The method that starts the game when the player presses the start key.
     */
    public void start(){
    }
    /**
     * The method which pauses the game at any point.
     */
    public void pause(){
  	}

   /**
    * The method which displays a Game Over message upon the player's failure to
    * adequetly play the game to the developer's satisfaction.
    * @param s the string to be printed.
    * @param g2 the graphics used to print the message.
    */
    public void showMessage(String s, Graphics2D g2){
		Font myFont = new Font("SansSerif", Font.BOLD + Font.ITALIC, 40);
		g2.setFont(myFont);
		g2.setColor(Color.RED);
		Rectangle2D textBox = myFont.getStringBounds(s,g2.getFontRenderContext());
		g2.drawString(s,(int) (getWidth()/2-textBox.getWidth()/2),(int)(getHeight()/2-textBox.getHeight()));
	}
    /**
     * This method changes the speed at which the blocks fall.
     * @param newspeed the new speed at which the blocks will fall, taken from them
       enum.
     */
    public void changeFallSpeed(FallSpeed newspeed){
		this.speed = newspeed;
		repaint();
  	}

    /**
     * The method which sets the speed as the game progresses.
     */
    public void speedSetter(){
        if(player.getPoints() < 50){
            changeFallSpeed(FallSpeed.FIRST);
        }
        if(player.getPoints() >= 50 && player.getPoints() < 100){
            changeFallSpeed(FallSpeed.SECOND);
        }
        if(player.getPoints() >= 100 && player.getPoints() < 200){
            changeFallSpeed(FallSpeed.THIRD);
        }
        if(player.getPoints() >= 200 && player.getPoints() < 400){
            changeFallSpeed(FallSpeed.FOURTH);
        }
        if(player.getPoints() >= 400 && player.getPoints() < 800){
            changeFallSpeed(FallSpeed.FIFTH);
        }
    }
    public void setPointsLine(int points){
        pointsLine.setText("Points:\n " + Integer.toString(points));
    }
    public void setLinesClearedLine(int linesCleared){
        linesClearedLine.setText("Lines Cleared:\n" + Integer.toString(linesCleared));
    }
    public void setHighScoreLine(int points){
        highScoreLine.setText("High Score:\n " + Integer.toString(points));
    }



}
/*

class PlayerDraw extends JPanel{
    Graphics2D g2;
    private Player player;

    public PlayerDraw(){
        setSize(50,50);
        player = new Player();
    }
    /**
     * The method which repaints the panel to change the positioning the objects.
     * @param g the graphics used to make the game visible.
     */
     /*
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        this.player.draw(g2);
    }
}
class QueueDraw extends JPanel{
    Graphics g2;
    int num;

    public QueueDraw(int number){
        setSize(50,50);
        this.num = number;
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        GameBoard.getInQueue(this.num).draw(g2);
    }
}

class HoldDraw extends JPanel{
    Graphics g2;


    public HoldDraw(){
        setSize(50,50);
    }

    public void paintComponent(Graphics g){

    }
}
*/
