package name.wanghwx.ehomework.common.pojo;

import java.io.Serializable;

public class PenSetting implements Serializable{

    private Stroke.Mode mode;

    public Float weight;
    public static final float MIN_WEIGHT = 1f;
    public static final float MAX_WEIGHT = 20f;

    public PenSetting(){
        this.mode = Stroke.Mode.BRUSH;
        this.weight = 1f;
    }

    public Stroke.Mode getMode() {
        return mode;
    }

    public void setMode(Stroke.Mode mode) {
        this.mode = mode;
    }

}