package name.wanghwx.ehomework.common.pojo;

import java.io.Serializable;
import java.util.List;

public class DisplayQuestion implements Serializable{

    private String number;

    private Integer type;

    private String stem;

    private List<DisplayOption> options;

    private String solution;

    private String choice;

    public String getNumber(){
        return number;
    }

    public void setNumber(String number){
        this.number = number;
    }

    public Integer getType(){
        return type;
    }

    public void setType(Integer type){
        this.type = type;
    }

    public String getStem(){
        return stem;
    }

    public void setStem(String stem){
        this.stem = stem;
    }

    public List<DisplayOption> getOptions(){
        return options;
    }

    public void setOptions(List<DisplayOption> options){
        this.options = options;
    }

    public String getSolution(){
        return solution;
    }

    public void setSolution(String solution){
        this.solution = solution;
    }

    public String getChoice(){
        return choice;
    }

    public void setChoice(String choice){
        this.choice = choice;
    }
}