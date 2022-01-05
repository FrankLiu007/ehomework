package name.wanghwx.ehomework.pojo;

import android.graphics.Bitmap;

import com.onyx.android.sdk.utils.CollectionUtils;

import java.util.List;

public class Item{

	private Integer itemId;
	
	private String category;

	private String title;
	
	private List<Question> questions;
	
	private String reference;
	
	private Unit unit;
	
	private Answer answer;

	private Bitmap manuscript;

	private Bitmap postil;

	private Boolean collected;

	private boolean remind;

	public Integer getItemId(){
		return itemId;
	}

	public void setItemId(Integer itemId){
		this.itemId = itemId;
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

	public List<Question> getQuestions(){
		return questions;
	}

	public void setQuestions(List<Question> questions){
		this.questions = questions;
	}
	
	public String getReference(){
		return reference;
	}

	public void setReference(String reference){
		this.reference = reference;
	}

	public Unit getUnit(){
		return unit;
	}

	public void setUnit(Unit unit){
		this.unit = unit;
	}

	public Answer getAnswer(){
		return answer;
	}

	public void setAnswer(Answer answer){
		this.answer = answer;
	}

	public Bitmap getManuscript() {
		return manuscript;
	}

	public void setManuscript(Bitmap manuscript) {
		this.manuscript = manuscript;
	}

	public Bitmap getPostil() {
		return postil;
	}

	public void setPostil(Bitmap postil) {
		this.postil = postil;
	}

	public Boolean isCollected(){
		return collected;
	}
	
	public void setCollected(Boolean collected){
		this.collected = collected;
	}

	public boolean isRemind(){
		return remind;
	}

	public void setRemind(boolean remind){
		this.remind = remind;
	}

	public boolean isSubjective(){
		if(!CollectionUtils.isNullOrEmpty(questions)){
			for(Question question:questions){
				if(question.getType() == Question.Type.GENERAL){
					return true;
				}
			}
		}
		return false;
	}

}