import java.util.*;
import javax.swing.*;
import java.awt.Color;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import static java.util.concurrent.TimeUnit.*;
import java.util.concurrent.*;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

/**
This class holds the implementation that allows
for the game of Tetris to be played.
@author OOD'19
*/
public class GameBoard extends JPanel{
    public final ScheduledThreadPoolExecutor gameExec = new ScheduledThreadPoolExecutor(2);
    private ScheduledFuture updateHandle;
    private ScheduledFuture updateMusic;
    //private int fallSpeed = 500;

    private Block[][] gameBoard;
    public Player player;
    private int[] centerPoint;

    private GamePanel panel;
    private BoardDraw boardDraw;

    GridBagConstraints constraints = new GridBagConstraints();

    private Block defaultBlock = new Block();
    private BlockSet activeBSet;
    private int[] originpt = new int[2];
    private Block activeBlock;
    private static LinkedList<BlockSet> blockSetQueue = new LinkedList<>();

    private static BlockSet blocks;

    private FallSpeed fallSpeed;
    private boolean paused = true;
    private boolean gameStarted = false;
    private int highScore;

    public boolean musicOn;
    private static final int BUFFER_SIZE = 1280000;
    private static File soundFile;
    private static AudioInputStream audioStream;
    private static AudioFormat audioFormat;
    private static SourceDataLine sourceLine;
    private static BlockSet heldBlockset;


    public GameBoard(){

        player = new Player();

        panel = new GamePanel();

        this.highScore = 0;

        boardDraw = new BoardDraw();

        this.gameBoard = new Block[30][10];

        player = new Player();

        JPanel playerDraw = new PlayerDraw();

        JPanel queueDrawFirst = new QueueDraw(0);
        JPanel queueDrawSecond = new QueueDraw(1);
        JPanel queueDrawThird = new QueueDraw(2);
        JPanel holdDraw = new HoldDraw();
        this.musicOn = true;

        addKeyListener(new KeyAdapter(){
    		public void keyPressed(KeyEvent e){
    			String s = KeyEvent.getKeyText(e.getKeyCode());
                if(paused){
                    return;
                }
    			if(e.getKeyCode() == KeyEvent.VK_LEFT) {
                    gameExec.submit(goLeft);
    			}
    			if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    gameExec.submit(goRight);
    			}
                if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                    gameExec.submit(goDown);
    			}
                if(e.getKeyCode() == KeyEvent.VK_UP) {
                    gameExec.submit(turnRight);
    			}
                if(e.getKeyCode() == KeyEvent.VK_SPACE) {
                    gameExec.submit(hardDrop);
    			}
                if(e.getKeyCode() == KeyEvent.VK_SLASH) {
                    gameExec.submit(hold);
    			}
    			repaint();
    		}
    	});

        setFocusable(true);

        for(int j = 29; j > -1; j--){
            for(int i = 0; i < 10; i++){
                this.gameBoard[j][i] = defaultBlock;
            }
        }
        setLayout(new GridBagLayout());

        constraints.fill = GridBagConstraints.BOTH;

        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridheight = 5;
        addGB(boardDraw, 1, 0);
        constraints.gridheight = 1;
        constraints.weightx = 0.25;
        constraints.weighty = 1.0;
        addGB(playerDraw, 0, 0);
        addGB(holdDraw, 0, 1);
        addGB(panel, 2, 0);
        addGB(queueDrawFirst, 2, 1);
        addGB(queueDrawSecond, 2, 2);
        addGB(queueDrawThird, 2, 3);

        fallSpeed = FallSpeed.FIRST;

        populateQueueInit();
        newActiveBlockSet();
    }

    void addGB(Component component, int x, int y){
        constraints.gridx = x;
        constraints.gridy = y;
        add(component, constraints);
    }

    public void populateQueueInit(){
        for(int i = 0; i < 5; i++){
            BlockSet blockset = new BlockSet();
            this.blockSetQueue.add(blockset);
        }
    }

    public void refillQueue(){
        BlockSet blockset = new BlockSet();
        this.blockSetQueue.add(blockset);
    }
    public static BlockSet getHeldBlockSet(){
        return heldBlockset;
    }

    final Runnable hold = new Runnable(){
        public void run(){
            BlockSet tempBlockSet;
            if(heldBlockset == null){
                heldBlockset = activeBSet;
                refillBlocks();
                newActiveBlockSet();
            }else{
                if(heldBlockset.getHeldState()){
                    return;
                }
                refillBlocks();
                tempBlockSet = activeBSet;
                originpt[0] = 5;
                originpt[1] = 3;
                activeBSet = heldBlockset;
                heldBlockset = tempBlockSet;
            }
            heldBlockset.changeHeldState(true);
        }
    };

    public static BlockSet getInQueue(int num){
        blocks = blockSetQueue.get(num);
        return blocks;
    }

    final Runnable hardDrop = new Runnable(){
        public void run(){
            boolean keepDropping = true;
            while(keepDropping){
                refillBlocks();
                keepDropping = dropBlockSet();
            }
            blocksToGameboard();
        }
    };

    final Runnable goLeft = new Runnable(){
        public void run(){
            refillBlocks();
            moveBlockSetLeft();
            blocksToGameboard();
        }
    };

    final Runnable turnRight = new Runnable(){
        public void run(){
            refillBlocks();
            rotateSetRight();
            blocksToGameboard();
        }
    };

    final Runnable goRight = new Runnable(){
        public void run(){
            refillBlocks();
            moveBlockSetRight();
            blocksToGameboard();
        }
    };

    final Runnable goDown = new Runnable(){
        public void run(){
            refillBlocks();
            dropBlockSet();
            blocksToGameboard();
        }
    };

    /**
    This method awards points to the player based on the number
    of rows deleted at one time
    @param numRows This integer is the number of rows deleted
    **/

    private void awardPoints(int numRows){
        if(numRows == 1){
            player.setPoints(player.getPoints() + 40);
        }
        if(numRows == 2){
            player.setPoints(player.getPoints() + 100);
        }
        if(numRows == 3){
            player.setPoints(player.getPoints() + 300);
        }
        if(numRows == 4){
            player.setPoints(player.getPoints() + 1200);
        }
    }

/**
This method checks to see if there are frozen blocks in
the creation zone above the game.
**/
    public boolean gameOver(){
        for(int i = 0; i < 9; i++){
            if(this.gameBoard[4][i].isFrozen()){
                return true;
            }
        }
        return false;
    }

    public void clearPanel(){
        player.setPoints(0);
        player.setLinesCleared(0);
    }

    /**
    This method iterates through the entire list and
    checks to see which rows are completed.
    @return This method returns an integer array of the completed rows.
    **/
    public int[] rowComplete(){
        int[] rows = new int[5];
        for(int i = 0; i < 4; i++){
            rows[i] = -1;
        }

        int placeKeeper = 0;

        for(int j = 29; j > 4; j--){
            int numFrozenBlocks = 0;
            for(int i = 0; i < 10; i++){
                if(this.gameBoard[j][i].isFrozen()){
                    numFrozenBlocks++;
                }
            }
            if(numFrozenBlocks == 10){
                rows[placeKeeper] = j;
                placeKeeper++;
            }
        }
        return rows;
    }


    /**
    This method shifts the rows down after a varying
    number of rows have been deleted. It also awards points.
    It does this by creating a new temporary array, and only copies the uncompleted rows
    to this new array. It then transfers the new array to the old one.
    @param rows This is the list of completed rows
    **/
    public void rowShift(int[] rows){
        int counter = 0;
        int iterator = 29;
        for(int j = 29; j > 4; j--){
            if(rows[counter] != j){
                for(int i = 0; i < 10; i++){
                    gameBoard[iterator][i] = this.gameBoard[j][i];
                }
                iterator--;
            } else{
                counter++;
            }
        }
        for(int j = 4; j>0; j--){
            for(int i = 0; i < 10; i++){
                gameBoard[j][i] = defaultBlock;
            }
        }
        awardPoints(counter);
        player.addLinesCleared(counter);
    }



    /**
    This method is what draws the board. It uses graphics and graphics2d to do
    this. The method has two for loops that go through the rows and columns of
    the board. Then it is draws a block at each place on the board.

    @param g this is the graphics object that is used to draw.
    */

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        if(!gameOver()){
            boardDraw.draw(gameBoard, g);
        }else{
            showMessage("GAME OVER", g2);
        }
    }

    public void showMessage(String s, Graphics2D g2){
    	Font myFont = new Font("SansSerif", Font.BOLD + Font.ITALIC, 40);
    	g2.setFont(myFont);
    	g2.setColor(Color.RED);
    	Rectangle2D textBox = myFont.getStringBounds(s,g2.getFontRenderContext());
    	g2.drawString(s,(int) (getWidth()/2-textBox.getWidth()/2),(int)(getHeight()/2-textBox.getHeight()));
	}
    /**
    A getter method that returns a Block. used for testing.
    Used for testing.
    @param int x,y
    @return a block
    */

    public Block boardBlockGetter(int x, int y){
        return this.gameBoard[y][x];
    }

    public void addBlock(int x, int y){
        Block block = new Block(Color.RED);
        block.changeState(true);
        this.gameBoard[y][x] = block;

    }

    public void newActiveBlockSet(){
        this.activeBSet = blockSetQueue.removeFirst();
	    refillQueue();
        originpt[0] = 5;
        originpt[1] = 3;
        activeBlock = new Block(activeBSet.getColor());
    }

    private void blocksToGameboard(){
        for(int i = 0; i < 4; i++){
            gameBoard[originpt[1] + activeBSet.getSetCoords()[i][1]][originpt[0] - activeBSet.getSetCoords()[i][0]] = activeBlock;
        }
    }

    private void setBlockSetOrigin(int x, int y){
        originpt[0] = x;
        originpt[1] = y;
    }

    private void refillBlocks(){
        for(int i = 0; i < 4; i++){
            gameBoard[originpt[1] + activeBSet.getSetCoords()[i][1]][originpt[0] - activeBSet.getSetCoords()[i][0]] = defaultBlock;
        }
    }

    public boolean dropBlockSet(){
        for(int i = 0; i < 4; i++){
            if(originpt[1] + activeBSet.getSetCoords()[i][1] == 29 || gameBoard[originpt[1] + activeBSet.getSetCoords()[i][1] + 1][originpt[0] - activeBSet.getSetCoords()[i][0]].isFrozen()){
                return false;
            }
        }
        originpt[1]+=1;
        return true;
    }

    public void moveBlockSetRight(){
        for(int i = 0; i < 4; i++){
            if(originpt[0] - activeBSet.getSetCoords()[i][0] == 9){
                return;
            }
            else if (gameBoard[originpt[1] + activeBSet.getSetCoords()[i][1]][originpt[0] - activeBSet.getSetCoords()[i][0] + 1].isFrozen()){

                return;
            }
        }
        originpt[0]+=1;
    }

    public void rotateSetRight(){
        activeBSet.rotateRight();
        for(int i = 0; i < 4; i++){
            if(originpt[0] - activeBSet.getSetCoords()[i][0] > 9 ||
                        originpt[0] - activeBSet.getSetCoords()[i][0] < 0 ||
                        originpt[1] + activeBSet.getSetCoords()[i][1] > 29 ||
                        gameBoard[originpt[1] + activeBSet.getSetCoords()[i][1]][originpt[0] - activeBSet.getSetCoords()[i][0]].isFrozen()
            ){
                activeBSet.rotateLeft();
                return;
            }
            /*
            else if (gameBoard[originpt[1] + activeBSet.getSetCoords()[i][1]][originpt[0] - activeBSet.getSetCoords()[i][0] + 1].isFrozen()){

                return;
            }
            */
        }
    }

    public void moveBlockSetLeft(){
        for(int i = 0; i < 4; i++){
            if(originpt[0] - activeBSet.getSetCoords()[i][0] == 0){
                return;
            }
            else if (gameBoard[originpt[1] + activeBSet.getSetCoords()[i][1]][originpt[0] - activeBSet.getSetCoords()[i][0] - 1].isFrozen()){
                return;
            }
        }
        originpt[0]-=1;
    }

    private void releaseBlockSet(){
        activeBlock.changeState(true);
        blocksToGameboard();
    }

    public void draw(Graphics2D g2){
        repaint();
    }

    public void clearBoard(){
        this.gameBoard = new Block[30][10];
        for(int j = 29; j > -1; j--){
            for(int i = 0; i < 10; i++){
                this.gameBoard[j][i] = defaultBlock;
            }
        }
    }

    final Runnable boardUpdate = new Runnable(){
        public void run(){
            if(paused){
                repaint();
            }else{
                if(!gameOver()){
                    refillBlocks();
                    if(!dropBlockSet()){
                        releaseBlockSet();
                        heldBlockset.changeHeldState(false);
                        if(rowComplete()[0] != -1){
                            rowShift(rowComplete());
                        }
                        newActiveBlockSet();
                    }
                    blocksToGameboard();
                    repaint();
                    panel.setLinesClearedLine(player.getLinesCleared());
                    panel.setPointsLine(player.getPoints());
                    if (player.getPoints() > highScore){
                        highScore = player.getPoints();
                        panel.setHighScoreLine(highScore);
                    }else{
                        panel.setHighScoreLine(highScore);
                    }
                    panel.setHighScoreLine(highScore);
                    panel.repaint();
                }else{
                    repaint();
                    pauseGame();
                    gameStarted = false;
                }
            }
        }
    };

    private void runGameAt(FallSpeed newFallSpeed){
        this.fallSpeed = newFallSpeed;
        updateHandle = gameExec.scheduleAtFixedRate(boardUpdate, 0, fallSpeed.speed(), MILLISECONDS);
        paused = false;
	}

    public void pauseGame(){
        updateHandle.cancel(false);
        paused = true;
    }

    public boolean gameStarted(){
        return this.gameStarted;
    }

    public void resumeGame(){
        if(!paused){
            paused = false;
            return;
        }
        runGameAt(this.fallSpeed);
        paused = false;
    }

    public void startGame(){
        try{
            updateHandle.cancel(false);
        }catch(Exception e){
        }
        paused = false;
        fallSpeed = FallSpeed.FIRST;
        clearBoard();
        clearPanel();
        newActiveBlockSet();
        runGameAt(fallSpeed);
        gameStarted = true;
    }
    final Runnable playSound = new Runnable(){
        public void run(){
            String strFilename = "TetrisMusic.wav";

            try {
                soundFile = new File(strFilename);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

            try {
                audioStream = AudioSystem.getAudioInputStream(soundFile);
            } catch (Exception e){
                e.printStackTrace();
                System.exit(1);
            }

            audioFormat = audioStream.getFormat();

            DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
            try {
                sourceLine = (SourceDataLine) AudioSystem.getLine(info);
                sourceLine.open(audioFormat);
            } catch (LineUnavailableException e) {
                e.printStackTrace();
                System.exit(1);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1);
            }

            sourceLine.start();

            int nBytesRead = 0;
            byte[] abData = new byte[BUFFER_SIZE];
            while (nBytesRead != -1) {
                try {
                    nBytesRead = audioStream.read(abData, 0, abData.length);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                if (nBytesRead >= 0) {
                    @SuppressWarnings("unused")
                    int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                }
            }

            sourceLine.drain();
            sourceLine.close();
        }
    };

    public void startMusic(){
        updateMusic = gameExec.scheduleAtFixedRate(playSound, 0, 3000, MILLISECONDS);
    }

    public void stopMusic(){
        updateMusic.cancel(true);
    }

   public void changeSound(){
       if(musicOn){
           musicOn = false;
       }else{
           musicOn = true;
       }
   }
}

class BoardDraw extends JPanel{
    private Block[][] tempBoard;

    public BoardDraw(){
        super();
        setPreferredSize(new Dimension(700, 300));
    }

    public void draw(Block[][] board, Graphics g){
        this.tempBoard = board;
        repaint();
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        for(int rowIndex = 29; rowIndex > 4; rowIndex--){
           for(int blockIndex = 0; blockIndex < 10; blockIndex++){
               g2.setColor(tempBoard[rowIndex][blockIndex].getColor());
               tempBoard[rowIndex][blockIndex].draw(g2, blockIndex * 24 + 5, (rowIndex-4) * 24);
           }
        }
    }












}

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
        if(GameBoard.getHeldBlockSet() == null){
            return;
        }
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        GameBoard.getHeldBlockSet().draw(g2);
    }
}
