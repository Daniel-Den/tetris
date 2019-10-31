import java.util.*;
import java.awt.geom.*;
import java.awt.*;
import javax.swing.*;

/**
This class holds the information of each individual Block
and allows for the state to be gathered.
@author OOD'19
*/
public class Block{
    public static final int size = 23;
    private boolean frozen;
    private Color color;
    private RectangularShape shape;
/**
The block constructor. It has a default to have an unfrozen block and the Color
is black. These will be the blocks that will fill the entire board before a BlockSet
is added.
*/
    public Block(){
        this.color = Color.BLACK;
        this.frozen = false;
    }
/**
The other block constructor. It takes in a color and the default is always
unfrozen. These wil lbe the blocks that are added to the board in blocksets.
@param color the color of the blocks.
*/
    public Block(Color color){
        this.color = color;
        this.frozen = false;
    }

    /**
    This method is the setter for the frozen attribute.
    @return This method returns the state of the Block.
    */
    public boolean isFrozen(){
        return this.frozen;
    }

    /**
    This method is the setter for the frozen attribute.
    @param state This method sets the state of the Block to the passed boolean.
    */
    public void changeState(boolean state){
        this.frozen = state;
    }
    /**
    This method gets the color of the block.
    @return it returns the color of the block.
    */
    public Color getColor(){
        return this.color;
    }
    /**
    This method sets the color of the block.
    @param color the color of the block.
    */
    public void setColor(Color color){
        this.color = color;
    }
    /**
    This is the function that draws the block. It draws it a certain location and
    in the color of the block.

    @param g2 the graphics2d object to be used for drawing the block.
    @param location_x the x location of the block
    @param location_y the y location of the block
    */
    public void draw(Graphics2D g2,int location_x, int location_y){
        g2.setPaint(this.color);
        g2.fillRoundRect(location_x, location_y, size, size, 5, 5);

    }
}
