package name.wanghwx.ehomework.common.pojo;

import java.io.Serializable;

public class DisplayOption implements Serializable{

    private String label;

    private String content;

    public String getLabel(){
        return label;
    }

    public void setLabel(String label){
        this.label = label;
    }

    public String getContent(){
        return content;
    }

    public void setContent(String content){
        this.content = content;
    }
}