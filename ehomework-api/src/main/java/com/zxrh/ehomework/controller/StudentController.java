package com.zxrh.ehomework.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.constraints.NotBlank;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zxrh.ehomework.common.constant.Default;
import com.zxrh.ehomework.common.constant.MapKey;
import com.zxrh.ehomework.common.constant.PreSuf;
import com.zxrh.ehomework.common.pojo.FailException;
import com.zxrh.ehomework.common.pojo.Result;
import com.zxrh.ehomework.common.util.MD5Utils;
import com.zxrh.ehomework.common.util.UUIDUtils;
import com.zxrh.ehomework.common.util.UploadUtils;
import com.zxrh.ehomework.pojo.Answer;
import com.zxrh.ehomework.pojo.Choice;
import com.zxrh.ehomework.pojo.Course;
import com.zxrh.ehomework.pojo.Homework;
import com.zxrh.ehomework.pojo.Homework.Status;
import com.zxrh.ehomework.pojo.Item;
import com.zxrh.ehomework.pojo.MistakeBook;
import com.zxrh.ehomework.pojo.Question;
import com.zxrh.ehomework.pojo.Question.Type;
import com.zxrh.ehomework.pojo.Student;
import com.zxrh.ehomework.pojo.Subject;
import com.zxrh.ehomework.pojo.Task;
import com.zxrh.ehomework.pojo.Task.Form;
import com.zxrh.ehomework.pojo.Unit;
import com.zxrh.ehomework.service.AnswerService;
import com.zxrh.ehomework.service.CourseService;
import com.zxrh.ehomework.service.HomeworkService;
import com.zxrh.ehomework.service.ItemService;
import com.zxrh.ehomework.service.MistakeBookService;
import com.zxrh.ehomework.service.RedisService;
import com.zxrh.ehomework.service.StudentService;
import com.zxrh.ehomework.service.UnitService;

@Validated
@RestController
@RequestMapping("student")
public class StudentController{

	@Autowired
	private StudentService studentService;
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private AnswerService answerService;
	@Autowired
	private MistakeBookService mistakeBookService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private RedisService redisService;
	
	private Student getCurrentStudent(){
		String token = (String)SecurityUtils.getSubject().getPrincipal();
		String auth = redisService.get(PreSuf.AUTH_KEY_PREFIX+token);
		if(!auth.startsWith(PreSuf.STUDENT_PREFIX)) throw new UnauthenticatedException("请使用学生账号登录");
		Student student = studentService.get(Integer.valueOf(auth.substring(PreSuf.STUDENT_PREFIX.length())));
		if(student == null) throw new UnauthenticatedException("请先登录");
		return student;
	}
	
	/**
	 * @return 当前学生信息
	 */
	@PostMapping("autoLogin")
	public Result<Student> test(){
		return new Result<Student>().code(Default.CODE_SUCCESS).message("自动登录成功").data(getCurrentStudent());
	}
	
	@GetMapping("remind")
	public Result<Object> findRemind(){
		Student currentStudent = getCurrentStudent();
		boolean homeworkRemind = studentService.findHomeworkRemind(currentStudent);
		boolean testRemind = studentService.findTestRemind(currentStudent);
		boolean mistakeRemind = studentService.findMistakeRemind(currentStudent);
		return new Result<>().code(MapKey.SUCCESS).message("查询提醒成功")
				.put(MapKey.HOMEWORK_REMIND,homeworkRemind)
				.put(MapKey.TEST_REMIND,testRemind)
				.put(MapKey.MISTAKE_REMIND,mistakeRemind);
	}
	
	/**
	 * @param account 学生账号
	 * @param password 学生密码
	 * @return 当前学生信息和登录token
	 */
	@PostMapping("login")
	public Result<Student> login(
			@NotBlank String account,
			@NotBlank String password){
		Student student = studentService.findByAccount(account);
		if(student == null || !MD5Utils.isCipher(student.getPassword(),password)) throw new FailException("用户名或密码错误");
		String auth = PreSuf.STUDENT_PREFIX + student.getStudentId();
		String token = redisService.get(PreSuf.TOKEN_KEY_PREFIX + auth);
		if(StringUtils.hasText(token)){
			redisService.delete(PreSuf.TOKEN_KEY_PREFIX + auth);
			redisService.delete(PreSuf.AUTH_KEY_PREFIX + token);
		}
		token = UUIDUtils.generate();
		redisService.set(PreSuf.AUTH_KEY_PREFIX + token,auth);
		redisService.set(PreSuf.TOKEN_KEY_PREFIX + auth,token);
		return new Result<Student>().code(Default.CODE_SUCCESS).message("登录成功").data(student).put(MapKey.TOKEN,token);
	}
	
	@PostMapping("logout")
	public Result<Object> logout(){
		String token = (String)SecurityUtils.getSubject().getPrincipal();
		String auth = redisService.get(PreSuf.AUTH_KEY_PREFIX+token);
		if(StringUtils.hasText(token)){
			redisService.delete(PreSuf.TOKEN_KEY_PREFIX + auth);
			redisService.delete(PreSuf.AUTH_KEY_PREFIX + token);
		}
		SecurityUtils.getSubject().logout();
		return new Result<>().code(1).message("退出登录成功");
	}
	
	/**
	 * @param formCode 任务形式码
	 * @return 作业计数信息
	 */
	@GetMapping("homework/count")
	public Result<Object> countHomework(Integer formCode){
		Task task = new Task();
		task.setForm(Form.getByCode(formCode));
		Homework homework = new Homework();
		homework.setTask(task);
		homework.setStudent(getCurrentStudent());
		Integer all = studentService.countHomework(homework);
		homework.setStatus(Status.UNSUBMITTED);
		Integer unsubmitted = studentService.countHomework(homework);
		homework.setStatus(Status.CORRECTED);
		Integer corrected = studentService.countHomework(homework);
		homework.setStatus(Status.UNCORRECTED);
		Integer uncorrected = studentService.countHomework(homework);
		return new Result<>().code(Default.CODE_SUCCESS).message("查询作业数量成功")
				.put(MapKey.ALL,all).put(MapKey.UNSUBMITTED,unsubmitted).put(MapKey.UNCORRECTED,uncorrected).put(MapKey.CORRECTED,corrected);
	}
	
	/**
	 * @param formCode 任务形式码
	 * @param statusCode 作业状态码
	 * @param pageNum 页码
	 * @param pageSize 页容量
	 * @return 分页作业数据
	 */
	@GetMapping("homework")
	public Result<List<Homework>> findHomeworks(
			Integer formCode,
			Boolean submitted,
			Integer subjectCode,
			Integer pageNum,
			Integer pageSize){
		if(pageNum == null) pageNum = 1;
		if(pageSize == null) pageSize = 6;
		Student currentStudent = getCurrentStudent();
		PageHelper.startPage(pageNum,pageSize);
		List<Homework> homeworks = studentService.find(currentStudent,Form.getByCode(formCode),submitted,Subject.getByCode(subjectCode));
		PageInfo<Homework> pageInfo = new PageInfo<>(homeworks);
		int unsubmittedRemind = homeworkService.countRemind(currentStudent.getStudentId(),Form.getByCode(formCode),false,Subject.getByCode(subjectCode));
		int submittedRemind = homeworkService.countRemind(currentStudent.getStudentId(),Form.getByCode(formCode),true,Subject.getByCode(subjectCode));
		return new Result<List<Homework>>().code(1).message("查询作业成功").data(pageInfo.getList())
				.put(MapKey.TOTAL,pageInfo.getTotal()).put(MapKey.MAX_PAGE_NUM,pageInfo.getPages())
				.put(MapKey.HAS_PREVIOUS,pageInfo.isHasPreviousPage()).put(MapKey.HAS_NEXT,pageInfo.isHasNextPage())
				.put(MapKey.UNSUBMITTED_REMIND,unsubmittedRemind).put(MapKey.SUBMITTED_REMIND,submittedRemind);
	}
	
	/**
	 * @param homeworkId 作业id
	 * @return 单次作业数据
	 */
	@GetMapping("homework/{homeworkId}")
	public Result<Homework> getHomework(@PathVariable Integer homeworkId){
		Homework homework = homeworkService.getDetail(homeworkId);
		homework.setRemind(false);
		homeworkService.update(homework);
		return new Result<Homework>().code(1).message("查询作业详情成功").data(homework);
	}
	
	@GetMapping("{homeworkId}/item/{itemId}")
	public Result<Item> getItemInHomework(@PathVariable Integer homeworkId,@PathVariable Integer itemId){
		Item item = itemService.getDetail(itemId);
		item.setAnswer(answerService.find(homeworkId,itemId));
		Homework homework = homeworkService.getDetail(homeworkId);
		MistakeBook mistakeBook = mistakeBookService.find(getCurrentStudent().getStudentId(),homework.getTask().getCourse().getCourseId());
		item.setCollected(mistakeBookService.isCollected(mistakeBook.getMistakeBookId(),itemId));
		return new Result<Item>().code(1).message("查询题目成功").data(item);
	}
	
	@GetMapping("items/{taskId}")
	public Result<List<Item>> findItems(@PathVariable Integer taskId){
		List<Item> items = itemService.findByTask(taskId);
		return new Result<List<Item>>().code(1).message("查询题目成功").data(items);
	}
	
	@PostMapping("collect/{courseId}/{itemId}")
	public Result<Object> collectItem(
			@PathVariable Integer courseId,
			@PathVariable Integer itemId){
		MistakeBook mistakeBook = mistakeBookService.find(getCurrentStudent().getStudentId(),courseId);
		mistakeBookService.collect(mistakeBook.getMistakeBookId(),itemId);
		return new Result<>().code(1).message("成功添加至错题本");
	}
	
	@DeleteMapping("abandon/{courseId}/{itemId}")
	public Result<Object> abandonItem(
			@PathVariable Integer courseId,
			@PathVariable Integer itemId){
		MistakeBook mistakeBook = mistakeBookService.find(getCurrentStudent().getStudentId(),courseId);
		mistakeBookService.abandon(mistakeBook.getMistakeBookId(),itemId);
		return new Result<>().code(1).message("从错题本移除题目成功");
	}
	
	@PostMapping("submit/{homeworkId}")
	public Result<Homework> submitHomework(
			@PathVariable Integer homeworkId,
			@RequestParam String objectiveJson,
			@RequestParam List<MultipartFile> manuscripts){
		Homework homework = homeworkService.getDetail(homeworkId);
		if(homework.getStatus() != Status.UNSUBMITTED) throw new FailException("本次作业已提交过，无法重复提交");
		if(objectiveJson.startsWith(Default.DOUBLE_QUOTE)) objectiveJson = objectiveJson.substring(1);
		if(objectiveJson.endsWith(Default.DOUBLE_QUOTE)) objectiveJson = objectiveJson.substring(0,objectiveJson.length()-1);
		objectiveJson = objectiveJson.replace(Default.BACKSLASH+Default.DOUBLE_QUOTE,Default.DOUBLE_QUOTE);
		try{
			Map<String,String> objectiveMap = objectMapper.readValue(objectiveJson,new TypeReference<Map<String,String>>(){});
			Map<String,String> subjectiveMap = UploadUtils.batchUpload(PreSuf.MANUSCRIPT_PREFIX,manuscripts);
			List<Item> items = homework.getTask().getItems();
			List<Answer> answers = new ArrayList<>();
			for(Item item:items){
				Integer itemId = item.getItemId();
				Answer answer = new Answer();
				answer.setHomework(homework);
				answer.setItem(item);
				String subjectiveKey = PreSuf.H_PREFIX+homeworkId+PreSuf.I_PREFIX+itemId+PreSuf.PNG_SUFFIX;
				if(subjectiveMap.containsKey(subjectiveKey)) answer.setManuscript(subjectiveMap.get(subjectiveKey));
				List<Question> questions = item.getQuestions();
				if(!CollectionUtils.isEmpty(questions)){
					List<Choice> choices = new ArrayList<>();
					for(Question question:questions){
						Integer questionId = question.getQuestionId();
						String objectiveKey = PreSuf.I_PREFIX+itemId+PreSuf.Q_PREFIX+questionId;
						Type type = question.getType();
						if(type == Type.SINGLE || type == Type.MULTIPLE){
							Choice choice = new Choice();
							choice.setQuestion(question);
							choice.setAnswer(answer);
							choice.setContent(objectiveMap.get(objectiveKey));
							choices.add(choice);
						}
					}
					answer.setChoices(choices);
					answers.add(answer);
				}
			}
			homework.setAnswers(answers);
			homework.setStatus(Status.UNCORRECTED);
			homework.setSubmitted(new Date());
			int wrong = homeworkService.submit(homework);
			return new Result<Homework>().code(1).message("作业提交成功").put(MapKey.WRONG,wrong);
		}catch (IOException e){
			e.printStackTrace();
			throw new FailException(PreSuf.IO_EXCEPTION_PREFIX+"客观题答案解析失败");
		}
	}
	
	@GetMapping("course")
	public Result<List<Course>> findCourses(){
		List<Course> courses = courseService.findByStudent(getCurrentStudent().getStudentId());
		return new Result<List<Course>>().code(1).message("查询课程成功").data(courses);
	}
	
	@GetMapping("unit/{unitId}")
	public Result<List<Unit>> findUnits(@PathVariable Integer unitId){
		Unit unit = unitService.getDetail(unitId);
		Unit parent;
		int level = 1;
		for(;(parent = unit.getParent()) != null;){
			level++;
			unit = unitService.getDetail(parent.getUnitId());
		}
		List<Unit> units = unitService.findByParent(unitId);
		return new Result<List<Unit>>().code(1).message("查询单元成功").data(units).put(MapKey.LEVEL,level);
	}
	
	@GetMapping("{courseId}/homework")
	public Result<List<Homework>> findHomeworksByCourse(
			@PathVariable Integer courseId,
			Integer formCode,
			Integer pageNum,
			Integer pageSize){
		Task task = new Task();
		task.setForm(Form.getByCode(formCode));
		task.setCourse(courseService.get(courseId));
		Homework homework = new Homework();
		homework.setStudent(getCurrentStudent());
		homework.setTask(task);
		PageHelper.startPage(pageNum,pageSize);
		List<Homework> homeworks = homeworkService.findCompliant(homework);
		PageInfo<Homework> pageInfo = new PageInfo<>(homeworks);
		return new Result<List<Homework>>().code(1).message("查询课程作业成功").data(pageInfo.getList())
				.put(MapKey.TOTAL,pageInfo.getTotal()).put(MapKey.MAX_PAGE_NUM,pageInfo.getPages())
				.put(MapKey.HAS_PREVIOUS,pageInfo.isHasPreviousPage()).put(MapKey.HAS_NEXT,pageInfo.isHasNextPage());
	}
	
	@GetMapping("mistakeBook")
	public Result<List<MistakeBook>> findMistakeBooks(){
		List<MistakeBook> mistakeBooks = mistakeBookService.findByStudent(getCurrentStudent().getStudentId());
		return new Result<List<MistakeBook>>().code(1).message("查询错题本成功").data(mistakeBooks);
	}
	
	@GetMapping("mistakeBook/{mistakeBookId}")
	public Result<MistakeBook> findMistakeBook(
			@PathVariable Integer mistakeBookId,
			Integer pageNum,
			Integer pageSize){
		MistakeBook mistakeBook = mistakeBookService.getDetail(mistakeBookId);
		PageHelper.startPage(pageNum,pageSize);
		List<Item> items = itemService.findByMistakeBook(mistakeBookId);
		PageInfo<Item> pageInfo = new PageInfo<>(items);
		mistakeBook.setItems(pageInfo.getList());
		return new Result<MistakeBook>().code(1).message("查询错题本数据成功").data(mistakeBook)
				.put(MapKey.TOTAL,pageInfo.getTotal())
				.put(MapKey.MAX_PAGE_NUM,pageInfo.getPages())
				.put(MapKey.HAS_PREVIOUS,pageInfo.isHasPreviousPage())
				.put(MapKey.HAS_NEXT,pageInfo.isHasNextPage());
	}
	
	@GetMapping("{mistakeBookId}/item")
	public Result<List<Item>> findItemsByMistakeBook(@PathVariable Integer mistakeBookId){
		List<Item> items = itemService.findByMistakeBook(mistakeBookId);
		return new Result<List<Item>>().code(1).message("查询错题本题目成功").data(items);
	}
	
	@PutMapping("clear/{mistakeBookId}/{itemId}")
	public Result<Object> clearRemind(
			@PathVariable Integer mistakeBookId,
			@PathVariable Integer itemId){
		mistakeBookService.clearRemind(mistakeBookId,itemId);
		return new Result<>().code(MapKey.SUCCESS).message("清除提醒成功");
	}
	
}