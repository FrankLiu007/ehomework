package name.wanghwx.ehomework.common.pojo;

import java.io.Serializable;

public class EraserSetting implements Serializable{

    private static final long serialVersionUID = 1L;

    private Stroke.Mode mode = Stroke.Mode.STROKE;

    private double width = MIN_WIDTH;
    private static final double MIN_WIDTH = 10;
    private static final double MAX_WIDTH = 50;

    public Stroke.Mode getMode(){
        return mode;
    }

    public void setMode(Stroke.Mode mode){
        this.mode = mode;
    }

    public double getWidth(){
        return width;
    }

    public void setWidth(double width){
        if(width < MIN_WIDTH) width = MIN_WIDTH;
        if(width > MAX_WIDTH) width = MAX_WIDTH;
        this.width = width;
    }

    public void decreaseWidth(){
        setWidth(width-5);
    }

    public void increaseWidth(){
        setWidth(width+5);
    }

}