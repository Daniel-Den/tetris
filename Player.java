import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.io.*;
import javax.imageio.*;
import javax.imageio.ImageIO;
/**
The player class essentially keeps track of the player's points. It has a setter
method and a getter method and it also adds the player icon drawing as well.
*/
class Player{
    private int points = 0;
    private int linesCleared = 0;
    private String playerIcon;
    public static int IMAGE_DISTANCE = 3;
    public static int IMAGE_Y_POSITION = 0;
    public static int IMAGE_H_GAP = 0;

    public Player(){
        this.points = points;
        this.linesCleared = linesCleared;
        ArrayList<String> players = new ArrayList<>();
        players.add("player.png");
        players.add("player1.jpg");
        players.add("player2.png");
        players.add("player3.jpg");
        players.add("player4.jpeg");
        players.add("player5.jpg");
        players.add("player6.jpg");
        this.playerIcon = players.get((int) (Math.random() * 7));
    }
/**
This method sets the points for the player.
@param points the points that are to be added to the player's score.
*/
    public void setPoints(int points){
        this.points = points;
    }
/**
This method gets the points of the player.
@return int number of points
*/
    public int getPoints(){
        return this.points;
    }

    public int getLinesCleared(){
        return this.linesCleared;
    }
    public void setLinesCleared(int lines){
        this.linesCleared = lines;
    }


    public int addLinesCleared(int lines){
        this.linesCleared = this.linesCleared + lines;
        return this.linesCleared;
    }
/**
This draws the player icon in the bottom left corner. It uses imageIO and a file
reader in order to get the image. A list of thedifferent player icons is used to
select a random icon for the player. All of this is in a try/catch block.
@param g2 the 2D graphics for the imageio
@exception newException the exception for the try catch block.
*/
    public void draw(Graphics2D g2){
        try{
          Image image = ImageIO.read(new File(this.playerIcon));
          Image newImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
          g2.drawImage(newImage, (newImage.getWidth(null) + IMAGE_H_GAP), IMAGE_Y_POSITION, null);
        }
        catch (Exception newException) {
        }
    }
}
