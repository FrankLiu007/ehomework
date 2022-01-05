package com.zxrh.ehomework.controller;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.zxrh.ehomework.common.constant.Default;
import com.zxrh.ehomework.common.constant.MapKey;
import com.zxrh.ehomework.common.constant.PreSuf;
import com.zxrh.ehomework.common.pojo.FailException;
import com.zxrh.ehomework.common.pojo.Result;
import com.zxrh.ehomework.common.util.MD5Utils;
import com.zxrh.ehomework.common.util.UUIDUtils;
import com.zxrh.ehomework.common.util.UploadUtils;
import com.zxrh.ehomework.pojo.Answer;
import com.zxrh.ehomework.pojo.Course;
import com.zxrh.ehomework.pojo.Homework;
import com.zxrh.ehomework.pojo.Item;
import com.zxrh.ehomework.pojo.Material;
import com.zxrh.ehomework.pojo.Material.Carrier;
import com.zxrh.ehomework.pojo.Task.Form;
import com.zxrh.ehomework.pojo.Review;
import com.zxrh.ehomework.pojo.Student;
import com.zxrh.ehomework.pojo.Task;
import com.zxrh.ehomework.pojo.Teacher;
import com.zxrh.ehomework.pojo.Unit;
import com.zxrh.ehomework.service.AnalyseService;
import com.zxrh.ehomework.service.AnswerService;
import com.zxrh.ehomework.service.CourseService;
import com.zxrh.ehomework.service.HomeworkService;
import com.zxrh.ehomework.service.ItemService;
import com.zxrh.ehomework.service.MaterialService;
import com.zxrh.ehomework.service.RedisService;
import com.zxrh.ehomework.service.ReviewService;
import com.zxrh.ehomework.service.StudentService;
import com.zxrh.ehomework.service.TaskService;
import com.zxrh.ehomework.service.TeacherService;
import com.zxrh.ehomework.service.UnitService;

@RestController
@RequestMapping("teacher")
public class TeacherController{

	@Autowired
	private TeacherService teacherService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private AnswerService answerService;
	@Autowired
	private ReviewService reviewService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private ItemService itemService;
	@Autowired
	private HomeworkService homeworkService;
	@Autowired
	private UnitService unitService;
	@Autowired
	private AnalyseService analyseService;
	
	private Teacher getCurrentTeacher(){
		String token = (String)SecurityUtils.getSubject().getPrincipal();
		String auth = redisService.get(PreSuf.AUTH_KEY_PREFIX+token);
		if(!auth.startsWith(PreSuf.TEACHER_PREFIX)) throw new UnauthenticatedException("请使用教师账号登录");
		Teacher teacher = teacherService.get(Integer.valueOf(auth.substring(PreSuf.TEACHER_PREFIX.length())));
		if(teacher == null) throw new UnauthenticatedException("请先登录");
		return teacher;
	}
	
	@PostMapping("login")
	public Result<Teacher> login(String account,String password){
		Teacher teacher = teacherService.findByAccount(account);
		if(teacher == null || !MD5Utils.isCipher(teacher.getPassword(),password)) throw new FailException("用户名或密码错误");
		String auth = PreSuf.TEACHER_PREFIX + teacher.getTeacherId();
		String token = redisService.get(PreSuf.TOKEN_KEY_PREFIX + auth);
		if(StringUtils.hasText(token)){
			redisService.delete(PreSuf.TOKEN_KEY_PREFIX + auth);
			redisService.delete(PreSuf.AUTH_KEY_PREFIX + token);
		}
		token = UUIDUtils.generate();
		redisService.set(PreSuf.AUTH_KEY_PREFIX + token,auth);
		redisService.set(PreSuf.TOKEN_KEY_PREFIX + auth,token);
		return new Result<Teacher>().code(1).message("登陆成功").data(teacher).put(MapKey.TOKEN,token);
	}
	
	@PostMapping("modify")
	public Result<Object> modify(
			@NotBlank String name,
			@RequestParam(required=false) MultipartFile avatar){
		Teacher currentTeacher = getCurrentTeacher();
		currentTeacher.setName(name);
		currentTeacher.setAvatar(avatar == null?Default.TEACHER_AVATAR:UploadUtils.upload(PreSuf.AVATAR_PREFIX,avatar));
		teacherService.update(currentTeacher);
		return new Result<>().code(MapKey.SUCCESS).message("修改成功");
	}
	
	@GetMapping("course")
	public Result<List<Course>> findCourses(){
		List<Course> courses = courseService.findByTeacher(getCurrentTeacher().getTeacherId());
		return new Result<List<Course>>().code(1).message("查询课程成功").data(courses);
	}
	
	@GetMapping("{courseId}/student")
	public Result<List<Student>> findStudents(@PathVariable Integer courseId){
		List<Student> students = studentService.findByCourse(courseId);
		return new Result<List<Student>>().code(1).message("查询课程学生成功").data(students);
	}
	
	@GetMapping("{courseId}/task")
	public Result<List<Task>> findTasks(@PathVariable Integer courseId,Integer formCode){
		Task task = new Task();
		task.setCourse(courseService.get(courseId));
		task.setForm(Form.getByCode(formCode));
		List<Task> tasks = taskService.findCompliant(task);
		return new Result<List<Task>>().code(1).message("查询课程作业成功").data(tasks);
	}
	
	@GetMapping("{taskId}/review/item")
	public Result<List<Answer>> reviewByItem(@PathVariable Integer taskId){
		List<Answer> answers = answerService.reviewByItem(taskId);
		return new Result<List<Answer>>().code(1).message("逐题批阅查询成功").data(answers);
	}
	
	@GetMapping("{taskId}/item/{itemId}")
	public Result<List<Answer>> reviewEachItem(@PathVariable Integer taskId,@PathVariable Integer itemId){
		List<Answer> answers = answerService.reviewItem(taskId,itemId);
		return new Result<List<Answer>>().code(1).message("查询单体批改成功").data(answers);
	}
	
	@GetMapping("{taskId}/review/homework")
	public Result<List<Homework>> reviewByHomework(@PathVariable Integer taskId){
		List<Homework> homeworks = homeworkService.reviewByHomework(taskId);
		return new Result<List<Homework>>().code(1).message("逐份批阅查询成功").data(homeworks);
	}
	
	@GetMapping("{taskId}/homework/{homeworkId}")
	public Result<List<Answer>> reviewEachHomework(@PathVariable Integer taskId,@PathVariable Integer homeworkId){
		List<Answer> answers = answerService.reviewHomework(taskId,homeworkId);
		return new Result<List<Answer>>().code(1).message("查询单份批改成功").data(answers);
	}
	
	@GetMapping("answer/{answerId}")
	public Result<Answer> getAnswer(@PathVariable Integer answerId){
		Answer answer = answerService.getDetail(answerId);
		return new Result<Answer>().code(1).message("查询学生答案成功").data(answer);
	}
	
	// 批改作业
	@PostMapping("review/{answerId}")
	public Result<Object> review(
			@PathVariable Integer answerId,
			@RequestParam(required=false) MultipartFile postil,
			String comment,
			@RequestParam(required=false) MultipartFile audio,
			Integer audioDuration,
			@NotNull Float score) throws FailException{
		Review review = reviewService.findByAnswer(answerId);
		if(review == null){
			review = new Review();
			review.setPostil(UploadUtils.upload(PreSuf.MANUSCRIPT_PREFIX,postil));
			review.setComment(comment);
			review.setAudio(UploadUtils.upload(PreSuf.AUDIO_PREFIX,audio));
			review.setAudioDuration(audioDuration);
			review.setScore(score);
			review.setAnswerId(answerId);
			reviewService.insert(review);
		}else{
			String oldPostil = review.getPostil();
			String oldAudio = review.getAudio();
			review.setPostil(UploadUtils.upload(PreSuf.MANUSCRIPT_PREFIX,postil));
			review.setComment(comment);
			review.setAudio(UploadUtils.upload(PreSuf.AUDIO_PREFIX,audio));
			review.setScore(score);
			reviewService.update(review);
			UploadUtils.delete(oldPostil);
			UploadUtils.delete(oldAudio);
		}
		return new Result<>().code(1).message("批阅成功");
	}
	
	@GetMapping("{taskId}/unreviewedItems")
	public Result<List<Item>> findUnreviewedItems(@PathVariable Integer taskId){
		List<Item> unreviewedItems = itemService.findUnreviewedByTask(taskId);
		return new Result<List<Item>>().code(1).message("查询未阅题目成功").data(unreviewedItems);
	}
	
	@GetMapping("task/{taskId}")
	public Result<Task> getDetailedTask(@PathVariable Integer taskId){
		Task task = taskService.getDetail(taskId);
		return new Result<Task>().code(MapKey.SUCCESS).message("查询成功").data(task);
	}
	
	@GetMapping("material")
	public Result<List<Material>> findMaterials(Integer carrierCode){
		Material material = new Material();
		material.setCarrier(Carrier.getByCode(carrierCode));
		material.setSubject(getCurrentTeacher().getSubject());
		List<Material> materials = materialService.findEligible(material);
		return new Result<List<Material>>().code(1).message("查询资料成功").data(materials);
	}
	
	@PostMapping("task")
	public Result<Task> assign(
			@RequestParam List<Integer> courseIds,
			Integer formCode,
			String title,
			@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date birthline,
			@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date deadline,
			String remark,
			@RequestParam List<Integer> itemIds){
		Task task = new Task();
		task.setForm(Form.getByCode(formCode));
		task.setTitle(title);
		task.setBirthline(birthline);
		task.setDeadline(deadline);
		task.setRemark(remark);
		task.setItems(itemService.getAll(itemIds));
		for(Integer courseId:courseIds){
			task.setCourse(courseService.getDetail(courseId));
			taskService.create(task);
		}
		return new Result<Task>().code(1).message("布置作业成功");
	}
	
	@PostMapping("test")
	public Result<Object> assignTest(
			@RequestParam List<Integer> courseIds,
			@NotBlank String title,
			@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date birthline,
			@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss") Date deadline,
			String remark,
			Integer unitId){
		Unit unit = unitService.getDetail(unitId);
		if(unit == null) throw new FailException("所选内容不存在");
		if(unit.getMaterial().getCarrier() != Carrier.PAPER) throw new FailException("只能选择试卷进行考试");
		Task task = new Task();
		task.setForm(Form.TEST);
		task.setTitle(title);
		task.setBirthline(birthline);
		task.setDeadline(deadline);
		task.setRemark(remark);
		task.setItems(unit.getItems());
		if(CollectionUtils.isEmpty(courseIds)) throw new FailException("需要为考试指定课程");
		for(Integer courseId:courseIds){
			task.setCourse(courseService.getDetail(courseId));
			taskService.create(task);
		}
		return new Result<>().code(MapKey.SUCCESS).message("布置考试成功");
	}
	
	@GetMapping("{taskId}/submitted")
	public Result<List<Homework>> findSubmittedHomeworks(@PathVariable Integer taskId){
		List<Homework> homeworks = homeworkService.findSubmittedByTask(taskId);
		return new Result<List<Homework>>().code(1).message("查询已提交作业成功").data(homeworks);
	}
	
	@GetMapping("homework/{homeworkId}")
	public Result<Homework> getHomework(@PathVariable Integer homeworkId){
		Homework homework = homeworkService.getDetail(homeworkId);
		return new Result<Homework>().code(1).message("查询作业详情成功").data(homework);
	}
	
	@GetMapping("{unitId}/unit")
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
	
	@GetMapping("{unitId}/item")
	public Result<List<Item>> findItems(@PathVariable Integer unitId){
		List<Item> items = itemService.findByUnit(unitId);
		return new Result<List<Item>>().code(1).message("查询题目成功").data(items);
	}
	
	//数据统计
	@GetMapping("analyse/{taskId}/item")
	public Result<List<Item>> analyseByItem(@PathVariable Integer taskId){
		List<Item> items = itemService.findByTask(taskId);
		if(!CollectionUtils.isEmpty(items)) items.forEach(item->item.setScore(analyseService.getAverageScore(taskId,item.getItemId())));
		return new Result<List<Item>>().code(1).message("查询单体分析成功").data(items);
	}
	
	@GetMapping("analyse/{taskId}/{itemId}")
	public Result<Item> analyseEachItem(
			@PathVariable Integer taskId,
			@PathVariable Integer itemId){
		Item item = itemService.getDetail(itemId);
		item.getQuestions().forEach(question->question.setChoices(analyseService.getGroupedChoices(taskId,question.getQuestionId())));
		return new Result<Item>().code(1).message("查询单体分析详情成功").data(item);
	}
	
	@GetMapping("detail/{taskId}/{itemId}")
	public Result<List<Answer>> detailed(
			@PathVariable Integer taskId,
			@PathVariable Integer itemId){
		List<Answer> answers = answerService.findByTask(taskId,itemId);
		return new Result<List<Answer>>().code(1).message("查询单体分析详情成功").data(answers);
	}
	
	@GetMapping("analyse/{taskId}/homework")
	public Result<List<Homework>> analyseByHomework(@PathVariable Integer taskId){
		List<Homework> homeworks = homeworkService.findByTask(taskId);
		return new Result<List<Homework>>().code(1).message("查询学生考情成功").data(homeworks);
	}
	
}