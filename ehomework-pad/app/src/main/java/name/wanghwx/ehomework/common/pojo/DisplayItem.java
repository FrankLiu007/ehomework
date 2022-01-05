package name.wanghwx.ehomework.common.pojo;

import java.io.Serializable;
import java.util.List;

public class DisplayItem implements Serializable{

    private Integer homeworkStatus;
    private String category;
    private String title;
    private List<DisplayQuestion> questions;
    private String reference;

    public Integer getHomeworkStatus() {
        return homeworkStatus;
    }

    public void setHomeworkStatus(Integer homeworkStatus) {
        this.homeworkStatus = homeworkStatus;
    }

    public String getCategory(){
        return category;
    }

    public void setCategory(String category){
        this.category = category;
    }

    public String getTitle(){
        return title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public List<DisplayQuestion> getQuestions(){
        return questions;
    }

    public void setQuestions(List<DisplayQuestion> questions){
        this.questions = questions;
    }

    public String getReference(){
        return reference;
    }

    public void setReference(String reference){
        this.reference = reference;
    }

}