import javax.swing.*;
import java.awt.*;
import java.awt.geom.*;
import java.awt.event.*;
import java.io.*;
import javax.imageio.*;
/**
This enum determines the speed at which the tetriminos fall. There are 5 levels
of speed.

*/
public enum FallSpeed{
    FIRST(500), SECOND(400), THIRD(300), FOURTH(200), FIFTH(100);
    private int speed;
    /**
    This method sets the speed.
    @param speed the speed that it will be set to.
    */
    FallSpeed(int speed){
        this.speed = speed;
    }
    /**
    This method is used in order to get what the speed is.
    @return the speed.
    */
    int speed(){
        return speed;
    }
}
