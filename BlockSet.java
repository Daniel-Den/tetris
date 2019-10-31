import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.ImageIO;

/**
The BlockSet class is a public class that creates, rotates, and prints the tetrominos for use in tetris. BlockSet contains a BlockType blocktype,
a 4x2 int array currentBlockCoords, an integer to assist in the rotation calculations, and the four Blocks that compose each tetromino. The array
containing the coordinates will remain constant as the tetromino falls, only changing when a rotation method is called. It can be returned as a
Block array when called.
*/
public class BlockSet{
    private BlockType blockType;
    private int[][] currentBlockCoords = new int[4][2];
    private int rotationFacilitation;
    private Block blockOne;
    private Block blockTwo;
    private Block blockThree;
    private Block blockFour;

    public static int IMAGE_DISTANCE = 3;
    public static int IMAGE_Y_POSITION = 0;
    public static int IMAGE_H_GAP = 0;

    public boolean hasBeenHeld;

    /**
    The BlockSet constructor takes in a block type, makes it an attribute of the blockset and, using the blockType, assigns it to a 4x2 int array,
    currentBlockCoords, giving it the nessessary coordinates to build the tetromino. blockOne though blockFour is then assigned the color of the
    corresponding blockColor taken from the getter method of blockType.
    @param BlockType blockType
    @return nothing
    */
    public BlockSet(BlockType blockType){
        this.blockType = blockType;
        currentBlockCoords = blockType.gimmeDaShape().clone();
        blockOne = new Block(blockType.gimmeDaColor());
        blockTwo = new Block(blockType.gimmeDaColor());
        blockThree = new Block(blockType.gimmeDaColor());
        blockFour = new Block(blockType.gimmeDaColor());
    }

    /**
     * Rotates each block around its centerpoint 90 degrees to the left.
     * This is accomplished by assigning each X in the currrentBlockCoords
     * to the negative Y value and each Y value to the X value
     * @param none
     * @return none
     */
    public void rotateLeft(){
        if(blockType.name() == "O"){
            return;
        }

        for(int[] coordinateRow : currentBlockCoords){
            rotationFacilitation = coordinateRow[0];
            coordinateRow[0] = -coordinateRow[1];
            coordinateRow[1] = rotationFacilitation;
        }
    }

    /**
     * Rotates each block around its centerpoint 90 degrees to the left.
     * This is accomplished by assigning each Y in the currrentBlockCoords
     * to the negative X value and each X value to the Y value
     * @param none
     * @return none
     */
    public void rotateRight(){
        if(blockType.name() == "O"){
            return;
        }

        for(int[] coordinateRow : currentBlockCoords){
            rotationFacilitation = coordinateRow[1];
            coordinateRow[1] = -coordinateRow[0];
            coordinateRow[0] = rotationFacilitation;
        }
    }


    /**
     * Getter method for an array that has the proper shape of the trimino,
     * constructs a Block[][] with the X-Y pairs in currentBlockCoords and
     * returns the blocks
     * @param none
     * @return Block[][] with blocks in the shape of the tetrimino
     */
    public int[][] getSetCoords(){
        return currentBlockCoords;
    }

    public Block[] getBlocks(){
        Block[] blocks = new Block[4];
        blocks[0] = blockOne;
        blocks[1] = blockTwo;
        blocks[2] = blockThree;
        blocks[3] = blockFour;
        return blocks;
    }

    public Color getColor(){
        return blockType.gimmeDaColor();
    }

    public BlockSet(){
        double index = Math.random() * 7;
        //System.out.println(index);
        if(index <= 1){
            blockType = BlockType.O;
            currentBlockCoords = blockType.gimmeDaShape().clone();
        }
        else if(index <= 2){
            blockType = BlockType.T;
            currentBlockCoords = blockType.gimmeDaShape().clone();
        }
        else if(index <= 3){
            blockType = BlockType.J;
            currentBlockCoords = blockType.gimmeDaShape().clone();
        }
        else if(index <= 4){
            blockType = BlockType.L;
            currentBlockCoords = blockType.gimmeDaShape().clone();
        }
        else if(index <= 5){
            blockType = BlockType.Z;
            currentBlockCoords = blockType.gimmeDaShape().clone();
        }
        else if(index <= 6){
            blockType = BlockType.S;
            currentBlockCoords = blockType.gimmeDaShape().clone();
        }
        else{
            blockType = BlockType.I;
            currentBlockCoords = blockType.gimmeDaShape().clone();
        }
    }


    public void draw(Graphics2D g2){
        try{
          Image image = ImageIO.read(new File(this.blockType.name() + ".png"));
          Image newImage = image.getScaledInstance(50, 50, Image.SCALE_DEFAULT);
          g2.drawImage(newImage, (newImage.getWidth(null) + IMAGE_H_GAP), IMAGE_Y_POSITION, null);
        }
        catch (Exception newException) {
        }
    }

    public void changeHeldState(boolean state){
        this.hasBeenHeld = state;
    }

    public boolean getHeldState(){
        return this.hasBeenHeld;
    }

}
