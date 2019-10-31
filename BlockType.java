import java.awt.Color;

/**
The BlockType enum is an enumeration containing all of the possible tetrominos that are used in tetris. These tetrominos
are O, I, J, L, Z, S, T as well as their corresponding color. The coordinate points for each tetromino is stored in a 4x2 int array called blockPoints which,
depending on the type, are filled in inside the constructor. There is also a Color blockColor which is also assigned inside the constructor.
*/
public enum BlockType{
    O, I, J, L, Z, S, T;

    private int[][] blockPoints = new int[4][2];
    private Color blockColor;

    /**
    The setBlockType method() takes in an BlockType and assigns it to the variable blockType.
    @param nothing
    @return BlockType blockType
    */

    private BlockType(){
        if(this.name() == "O"){
            blockPoints[0][0] = 0; blockPoints[0][1] = 0; // 0, 0
            blockPoints[1][0] = 0; blockPoints[1][1] = 1; // 0, 1
            blockPoints[2][0] = 1; blockPoints[2][1] = 0; // 1, 0
            blockPoints[3][0] = 1; blockPoints[3][1] = 1; // 1, 1
            blockColor = Color.YELLOW;
        }

        else if(this.name() == "I"){
            blockPoints[0][0] = 0; blockPoints[0][1] = 0;  // 0, 0
            blockPoints[1][0] = 0; blockPoints[1][1] = 1;  // 0, 1
            blockPoints[2][0] = 0; blockPoints[2][1] = -1;  // 0, -1
            blockPoints[3][0] = 0; blockPoints[3][1] = 2; // 0, 2
            blockColor = Color.CYAN;
        }

        else if(this.name() == "T"){
            blockPoints[0][0] = 0; blockPoints[0][1] = 0;  // 0, 0
            blockPoints[1][0] = 1; blockPoints[1][1] = 0;  // 1, 0
            blockPoints[2][0] = 0; blockPoints[2][1] = -1;  // 0, -1
            blockPoints[3][0] = -1; blockPoints[3][1] = 0; // -1, 0
            blockColor = Color.MAGENTA;
        }

        else if(this.name() == "L"){
            blockPoints[0][0] = 0; blockPoints[0][1] = 0;  // 0, 0
            blockPoints[1][0] = 0; blockPoints[1][1] = -1;  // 0, -1
            blockPoints[2][0] = 0; blockPoints[2][1] = 1; // 0, 1
            blockPoints[3][0] = 1; blockPoints[3][1] = -1; // 1, -1
            blockColor = Color.ORANGE;
        }
        else if(this.name() == "J"){
            blockPoints[0][0] = 0; blockPoints[0][1] = 0;   // 0, 0
            blockPoints[1][0] = 0; blockPoints[1][1] = -1;   // 0, -1
            blockPoints[2][0] = 0; blockPoints[2][1] = 1;  // 0, 1
            blockPoints[3][0] = -1; blockPoints[3][1] = -1; //-1, -1
            blockColor = Color.BLUE;
        }

        else if(this.name() == "Z"){
            blockPoints[0][0] = 0; blockPoints[0][1] = 0; // 0, 0
            blockPoints[1][0] = 1; blockPoints[1][1] = 0; // 1, 0
            blockPoints[2][0] = 1; blockPoints[2][1] = 1; // 1, 1
            blockPoints[3][0] = 0; blockPoints[3][1] = -1; // 0, -1
            blockColor = Color.RED;
        }

        else if(this.name() == "S"){
            blockPoints[0][0] = 0; blockPoints[0][1] = 0; // 0, 0
            blockPoints[1][0] = 1; blockPoints[1][1] = 0; // 1, 0
            blockPoints[2][0] = 0; blockPoints[2][1] = 1; // 0, 1
            blockPoints[3][0] = 1; blockPoints[3][1] = -1; // 1, -1
            blockColor = Color.GREEN;
        }
        else{
            return;
        }
    }

    /**
    The getter method for blockPoints, this returnes a 4x2 int array of coordinate points.
    @param nothing
    @return int[][] blockPoints
    */
    public int[][] gimmeDaShape(){
        return blockPoints;
    }

    /**
    The getter method for blockColor, this returnes a Color corresponding to a certain tetromino.
    @param nothing
    @return Color blockColor
    */
    public Color gimmeDaColor(){
        return blockColor;
    }

}
