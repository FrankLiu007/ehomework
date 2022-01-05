package com.zxrh.ehomework.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.zxrh.ehomework.mapper.AnswerMapper;
import com.zxrh.ehomework.mapper.ChoiceMapper;
import com.zxrh.ehomework.mapper.HomeworkMapper;
import com.zxrh.ehomework.mapper.ItemMapper;
import com.zxrh.ehomework.mapper.MistakeBookMapper;
import com.zxrh.ehomework.mapper.QuestionMapper;
import com.zxrh.ehomework.mapper.ReviewMapper;
import com.zxrh.ehomework.pojo.Answer;
import com.zxrh.ehomework.pojo.Choice;
import com.zxrh.ehomework.pojo.Homework;
import com.zxrh.ehomework.pojo.Question;
import com.zxrh.ehomework.pojo.Review;
import com.zxrh.ehomework.pojo.Subject;
import com.zxrh.ehomework.pojo.Task.Form;
import com.zxrh.ehomework.pojo.Homework.Status;
import com.zxrh.ehomework.pojo.MistakeBook;
import com.zxrh.ehomework.service.HomeworkService;

@Service
@Transactional
public class HomeworkServiceImpl implements HomeworkService{

	@Autowired
	private HomeworkMapper homeworkMapper;
	@Autowired
	private AnswerMapper answerMapper;
	@Autowired
	private ChoiceMapper choiceMapper;
	@Autowired
	private ItemMapper itemMapper;
	@Autowired
	private QuestionMapper questionMapper;
	@Autowired
	private ReviewMapper reviewMapper;
	@Autowired
	private MistakeBookMapper mistakeBookMapper;
	
	@Override
	public void create(Homework homework){
		
	}

	@Override
	public Homework getDetail(Integer homeworkId){
		return homeworkMapper.getDetail(homeworkId);
	}

	@Override
	public int submit(Homework homework){
		int wrong = 0;
		homeworkMapper.update(homework);
		List<Answer> answers = homework.getAnswers();
		if(!CollectionUtils.isEmpty(answers)){
			for(Answer answer:answers){
				List<Choice> choices = answer.getChoices();
				if(!CollectionUtils.isEmpty(choices)){
					for(Choice choice:choices){
						if(!choice.isRight()) wrong++;
					}
					if(itemMapper.getDetail(answer.getItem().getItemId()).isAllSingle()){
						float score = 0,total = 0;
						for(Choice choice:choices){
							Question question = questionMapper.get(choice.getQuestion().getQuestionId());
							total += question.getScore();
							if(question.getSolution().equals(choice.getContent())){
								score+=question.getScore();
							}
						}
						answer.setReviewed(true);
						answerMapper.insert(answer);
						choiceMapper.insert(answer);
						Review review = new Review();
						review.setScore(homework.getTask().getForm()==Form.TEST?score:score*5/total);
						review.setAnswerId(answer.getAnswerId());
						reviewMapper.insert(review);
					}else{
						answerMapper.insert(answer);
						choiceMapper.insert(answer);
					}
				}else{
					answerMapper.insert(answer);
				}
			}
		}
		correctIfCorrected(homework);
		return wrong;
	}

	@Override
	public List<Homework> findSubmittedByTask(Integer taskId){
		return homeworkMapper.findSubmittedByTask(taskId);
	}

	@Override
	public List<Homework> findCompliant(Homework homework){
		return homeworkMapper.findCompliant(homework);
	}

	@Override
	public List<Homework> findByTask(Integer taskId){
		return homeworkMapper.findByTask(taskId);
	}

	@Override
	public void update(Homework homework){
		homeworkMapper.update(homework);
	}

	@Override
	public int countRemind(Integer studentId, Form form, Boolean submitted, Subject subject){
		return homeworkMapper.countRemind(studentId,form,submitted,subject);
	}

	@Override
	public List<Homework> reviewByHomework(Integer taskId){
		return homeworkMapper.reviewByHomework(taskId);
	}

	@Override
	public void correctIfCorrected(Homework homework){
		if(homeworkMapper.isActuallyCorrected(homework)){
			homework.setStatus(Status.CORRECTED);
			homework.setCorrected(new Date());
			homework.setScore(homework.getTask().getForm() == Form.TEST?homeworkMapper.getSum(homework):homeworkMapper.getAverage(homework));
			homework.setRemind(true);
			homeworkMapper.update(homework);
			Integer homeworkId = homework.getHomeworkId();
			List<Answer> answers = answerMapper.findByHomework(homeworkId);
			MistakeBook mistakeBook = mistakeBookMapper.find(homework.getStudent().getStudentId(),homework.getTask().getCourse().getCourseId());
			for(Answer answer:answers){
				if(answer.getReview().getScore() < answer.getItem().getTotal()) mistakeBookMapper.collect(mistakeBook.getMistakeBookId(),answer.getItem().getItemId());
			}
		}
	}

}