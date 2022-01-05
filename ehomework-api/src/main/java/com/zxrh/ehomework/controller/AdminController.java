package com.zxrh.ehomework.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.ArrayUtils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zxrh.ehomework.common.constant.Default;
import com.zxrh.ehomework.common.constant.MapKey;
import com.zxrh.ehomework.common.constant.PreSuf;
import com.zxrh.ehomework.common.pojo.FailException;
import com.zxrh.ehomework.common.pojo.Result;
import com.zxrh.ehomework.common.util.FileUtils;
import com.zxrh.ehomework.common.util.MD5Utils;
import com.zxrh.ehomework.common.util.UUIDUtils;
import com.zxrh.ehomework.common.util.UploadUtils;
import com.zxrh.ehomework.pojo.Admin;
import com.zxrh.ehomework.pojo.Course;
import com.zxrh.ehomework.pojo.Item;
import com.zxrh.ehomework.pojo.Material;
import com.zxrh.ehomework.pojo.Material.Carrier;
import com.zxrh.ehomework.pojo.Student;
import com.zxrh.ehomework.pojo.Subject;
import com.zxrh.ehomework.pojo.Teacher;
import com.zxrh.ehomework.pojo.Unit;
import com.zxrh.ehomework.service.AdminService;
import com.zxrh.ehomework.service.CourseService;
import com.zxrh.ehomework.service.MaterialService;
import com.zxrh.ehomework.service.RedisService;
import com.zxrh.ehomework.service.StudentService;
import com.zxrh.ehomework.service.TeacherService;

@Validated
@Controller
@RequestMapping("admin")
public class AdminController{
	
	@Autowired
	private AdminService adminService;
	@Autowired
	private TeacherService teacherService;
	@Autowired
	private StudentService studentService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private MaterialService materialService;
	@Autowired
	private RedisService redisService;
	@Autowired
	private ObjectMapper objectMapper;

	private Admin getCurrentAdmin(){
		String token = (String)SecurityUtils.getSubject().getPrincipal();
		String auth = redisService.get(PreSuf.AUTH_KEY_PREFIX+token);
		if(!auth.startsWith(PreSuf.ADMIN_PREFIX)) throw new UnauthenticatedException("请使用管理员账号登录");
		Admin admin = adminService.get(Integer.valueOf(auth.substring(PreSuf.ADMIN_PREFIX.length())));
		if(admin == null) throw new UnauthenticatedException("请先登录");
		return admin;
	}
	
	@GetMapping("login")
	public String login(Model model){
		return "login";
	}
	
	@PostMapping("login")
	public String login(String account,String password,Model model) throws FailException{
		Admin admin = adminService.findByAccount(account);
		if(admin == null || !MD5Utils.isCipher(admin.getPassword(),password)){
			model.addAttribute("errMsg","用户名或密码错误");
			return "login";
		}
		String auth = PreSuf.ADMIN_PREFIX + admin.getId();
		String token = redisService.get(PreSuf.TOKEN_KEY_PREFIX + auth);
		if(StringUtils.hasText(token)){
			redisService.delete(PreSuf.TOKEN_KEY_PREFIX + auth);
			redisService.delete(PreSuf.AUTH_KEY_PREFIX + token);
		}
		token = UUIDUtils.generate();
		redisService.set(PreSuf.AUTH_KEY_PREFIX + token,auth);
		redisService.set(PreSuf.TOKEN_KEY_PREFIX + auth,token);
		model.addAttribute("admin",admin);
		model.addAttribute("token",token);
		model.addAttribute("menu","admin");
		return "index";
	}
	
	@GetMapping("index/{menu}")
	public String index(@PathVariable String menu,Model model){
		model.addAttribute("menu",menu);
		return "index";
	}
	
	@GetMapping("admin")
	@ResponseBody
	public Result<List<Admin>> getAdmins(){
		List<Admin> admins = adminService.getAll();
		return new Result<List<Admin>>().code(1).message("查询管理员成功").data(admins);
	}
	
	@GetMapping("teacher")
	@ResponseBody
	public Result<List<Teacher>> getTeachers(){
		List<Teacher> teachers = teacherService.getAll();
		return new Result<List<Teacher>>().code(1).message("查询教师成功").data(teachers);
	}
	
	@GetMapping("teacher/search")
	@ResponseBody
	public Result<List<Teacher>> searchTeachers(String keyword){
		List<Teacher> teachers = teacherService.search(keyword);
		return new Result<List<Teacher>>().code(1).message("搜索教师成功").data(teachers);
	}
	
	@GetMapping("student")
	@ResponseBody
	public Result<List<Student>> getStudents(){
		List<Student> students = studentService.getAll(null);
		return new Result<List<Student>>().code(1).message("查询学生成功").data(students);
	}
	
	@GetMapping("course")
	@ResponseBody
	public Result<List<Course>> getCourses(){
		List<Course> courses = courseService.getAll();
		return new Result<List<Course>>().code(1).message("查询课程成功").data(courses);
	}
	
	@GetMapping("material")
	@ResponseBody
	public Result<List<Material>> getMaterials(){
		List<Material> materials = materialService.getAll(null);
		return new Result<List<Material>>().code(1).message("查询资料成功").data(materials);
	}
	
	@PostMapping("admin")
	@ResponseBody
	public Result<Object> createAdmin(
			@NotBlank String account,
			@NotBlank String password){
		Admin currentAdmin = getCurrentAdmin();
		if(!currentAdmin.isSuper()) throw new UnauthorizedException("只有超级管理员有权创建管理员账号");
		Admin admin = adminService.findByAccount(account);
		if(admin != null) throw new FailException("该账号已存在");
		admin = new Admin();
		admin.setAccount(account);
		admin.setPassword(MD5Utils.encrypt(password));
		adminService.create(admin);
		return new Result<>().code(1).message("创建管理员成功");
	}
	
	@PostMapping("teacher")
	@ResponseBody
	public Result<Object> createTeacher(
			@NotBlank String account,
			@NotBlank String password,
			@NotBlank String name,
			@NotNull Integer subjectCode,
			@RequestParam(required=false) MultipartFile avatar,
			@NotBlank String phone) throws FailException{
		getCurrentAdmin();
		Teacher teacher = teacherService.findByAccount(account);
		if(teacher != null) throw new FailException("该账号已存在");
		teacher = new Teacher();
		teacher.setAccount(account);
		teacher.setPassword(MD5Utils.encrypt(password));
		teacher.setName(name);
		teacher.setSubject(Subject.getByCode(subjectCode));
		teacher.setAvatar(avatar == null?Default.TEACHER_AVATAR:UploadUtils.upload(PreSuf.AVATAR_PREFIX,avatar));
		teacher.setPhone(phone);
		teacherService.create(teacher);
		return new Result<>().code(1).message("创建教师账号成功");
	}
	
	@PostMapping("student")
	@ResponseBody
	public Result<Object> createStudent(
			@NotBlank String account,
			@NotBlank String password,
			@NotBlank String name){
		getCurrentAdmin();
		Student student = studentService.findByAccount(account);
		if(student != null) throw new FailException("该账号已存在");
		student = new Student();
		student.setAccount(account);
		student.setPassword(MD5Utils.encrypt(password));
		student.setName(name);
		studentService.create(student);
		return new Result<>().code(1).message("创建学生账号成功").data(null);
	}
	
	@PostMapping("course")
	@ResponseBody
	public Result<Course> createCourse(
			String name,
			Integer teacherId,
			@RequestParam List<Integer> studentIds){
		Course course = new Course();
		course.setName(name);
		Teacher teacher = teacherService.get(teacherId);
		course.setTeacher(teacher);
		if(studentIds != null && !studentIds.isEmpty()){
			List<Student> students = studentService.getAll(studentIds);
			course.setStudents(students);
		}
		courseService.create(course);
		return new Result<Course>().code(1).message("创建课程成功");
	}
	
	@PostMapping("paper")
	@ResponseBody
	public Result<List<Item>> previewPaper(
			@RequestParam MultipartFile questions,
			@NotNull Integer subjectCode,
			String name){
		String uuid = UUIDUtils.generate();
		String rootPath = Default.PAPER_PATH + uuid + File.separator;
		File docxFile = new File(rootPath + Default.DOCX_PATH);
		if(docxFile.exists()) FileUtils.delete(docxFile);
		docxFile.mkdirs();
		File questionsFile = new File(docxFile,Default.QUESTIONS_PATH);
		if(questionsFile.exists()) FileUtils.delete(questionsFile);
		File answersFile = new File(docxFile,Default.ANSWERS_PATH);
		if(answersFile.exists()) FileUtils.delete(answersFile);
		byte[] buffer = new byte[1024*8];
		BufferedInputStream questionsBis = null,answersBis = null;
		BufferedOutputStream questionsBos = null,answersBos = null;
		try{
			questionsBis = new BufferedInputStream(questions.getInputStream());
			questionsBos = new BufferedOutputStream(new FileOutputStream(questionsFile));
			for(int l;(l=questionsBis.read(buffer))!=-1;questionsBos.write(buffer,0,l)){}
		}catch(IOException e){
			throw new FailException(PreSuf.IO_EXCEPTION_PREFIX+"文档保存异常");
		}finally{
			try{
				if(questionsBis != null) questionsBis.close();
				if(questionsBos != null){
					questionsBos.flush();
					questionsBos.close();
				}
				if(answersBis != null) answersBis.close();
				if(answersBos != null){
					answersBos.flush();
					answersBos.close();
				}
			}catch(IOException e){
				throw new FailException(PreSuf.IO_EXCEPTION_PREFIX+"文件流关闭异常");
			}
		}
		File materialFile = new File(rootPath + Default.MATERIAL_PATH);
		if(materialFile.exists()) FileUtils.delete(materialFile);
		Material material = new Material();
		Subject subject = Subject.getByCode(subjectCode);
		if(!StringUtils.hasText(name)) name = FileUtils.getName(questions.getOriginalFilename());
		material.setName(name);
		material.setSubject(subject);
		material.setCarrier(Carrier.PAPER);
		try{
			objectMapper.writeValue(materialFile,material);
		}catch(IOException e){
			throw new FailException(PreSuf.IO_EXCEPTION_PREFIX+"生成信息文件失败");
		}
		System.err.println(Arrays.asList(Default.PYTHON,Default.PYTHON_SCRIPT,"-working_dir",rootPath,"-subject",subject.getName(),"-question_docx",Default.DOCX_PATH+Default.QUESTIONS_PATH,"-answer_docx",Default.DOCX_PATH+Default.ANSWERS_PATH,"-img_dir",Default.IMG_PATH,"-http_head",PreSuf.IMG_PREFIX,"-out_json",Default.ITEMS_PATH));
		try{
			Process process = Runtime.getRuntime().exec(new String[]{Default.PYTHON,Default.PYTHON_SCRIPT,"-working_dir",rootPath,"-subject",subject.getName(),"-question_docx",Default.DOCX_PATH+Default.QUESTIONS_PATH,"-img_dir",Default.IMG_PATH,"-http_head",PreSuf.IMG_PREFIX,"-out_json",Default.ITEMS_PATH});
			new Thread(()->{
				try{
					BufferedInputStream ibis = new BufferedInputStream(process.getInputStream());
					BufferedInputStream ebis = new BufferedInputStream(process.getErrorStream());
					StringBuilder isb = new StringBuilder(),esb = new StringBuilder();
					for(int l;(l=ibis.read(buffer))!=-1;isb.append(new String(buffer,0,l,"gbk"))){}
					for(int l;(l=ebis.read(buffer))!=-1;esb.append(new String(buffer,0,l,"gbk"))){}
					System.out.println(isb);
					System.err.println(esb);
				}catch(IOException ignore){}
			}).start();
			process.waitFor();
		}catch(Exception e){
			throw new FailException("生成json文件失败");
		}
		File itemsFile = new File(rootPath + Default.ITEMS_PATH);
		if(!itemsFile.isFile()) throw new FailException("生成json文件失败");
		UploadUtils.uploadDirectory(PreSuf.ITEM_PREFIX,rootPath + Default.IMG_PATH);
		try{
			return new Result<List<Item>>().code(1).message("预览试卷").data(objectMapper.readValue(itemsFile,new TypeReference<List<Item>>(){})).put(MapKey.NAME,name).put(MapKey.UUID,uuid);
		}catch(IOException e){
			throw new FailException(PreSuf.IO_EXCEPTION_PREFIX+"读取json文件失败");
		}
	}
	
	@PostMapping("paper/{uuid}")
	@ResponseBody
	public Result<Object> confirmPaper(
			@PathVariable String uuid,
			@NotNull Boolean confirm){
		String rootPath = Default.PAPER_PATH + uuid + File.separator;
		File rootFile = new File(rootPath);
		if(!rootFile.isDirectory()) throw new FailException("指定目录不存在");
		if(confirm){
			File materialFile = new File(rootFile,Default.MATERIAL_PATH);
			if(!materialFile.isFile()) throw new FailException("信息文件不存在");
			File itemsFile = new File(rootFile,Default.ITEMS_PATH);
			if(!itemsFile.isFile()) throw new FailException("json文件不存在");
			try{
				Material material = objectMapper.readValue(materialFile,Material.class);
				List<Item> items = objectMapper.readValue(itemsFile,new TypeReference<List<Item>>(){});
				Unit root = new Unit();
				root.setItems(items);
				material.setRoot(root);
				material.setCreated(new Date());
				materialService.create(material);
			}catch(IOException e){
				e.printStackTrace();
				throw new FailException(PreSuf.IO_EXCEPTION_PREFIX+"读取json文件失败");
			}
		}else{
			File imgFile = new File(rootPath + Default.IMG_PATH);
			if(imgFile.isDirectory()){
				File[] files = imgFile.listFiles();
				if(!ArrayUtils.isEmpty(files)){
					List<String> filenames = new ArrayList<>();
					for(File file:files){
						if(file.isFile()) filenames.add(file.getName());
					}
					UploadUtils.batchDelete(PreSuf.ITEM_PREFIX,filenames);
				}
			}
		}
		FileUtils.delete(rootFile);
		return new Result<>().code(1).message(confirm?"添加试卷成功":"已取消");
	}
	
	@Scheduled(cron="0 0 4 * * *")
	protected void recyclePapar(){
		System.err.println("执行了");
		File paperFile = new File(Default.PAPER_PATH);
		if(paperFile.isDirectory()){
			File[] rootFiles = paperFile.listFiles();
			if(!ArrayUtils.isEmpty(rootFiles)){
				for(File rootFile:rootFiles){
					File imgFile = new File(rootFile,Default.IMG_PATH);
					if(imgFile.isDirectory()){
						File[] files = imgFile.listFiles();
						if(!ArrayUtils.isEmpty(files)){
							List<String> filenames = new ArrayList<>();
							for(File file:files){
								if(file.isFile()) filenames.add(file.getName());
							}
							UploadUtils.batchDelete(PreSuf.ITEM_PREFIX,filenames);
						}
					}
					FileUtils.delete(rootFile);
				}
			}
		}
	}
	
	@DeleteMapping("admin/{id}")
	@ResponseBody
	public Result<Object> deleteAdmin(@PathVariable Integer id){
		Admin currentAdmin = getCurrentAdmin();
		if(!currentAdmin.isSuper()) throw new UnauthorizedException("只有超级管理员有权删除管理员账号");
		Admin admin = adminService.get(id);
		if(admin == null) throw new FailException("该账号不存在");
		if(admin.isSuper()) throw new FailException("超级管理员无法删除");
		adminService.delete(id);
		return new Result<>().code(1).message("删除管理员成功");
	}
	
	@PostMapping("test")
	public void read(@RequestParam MultipartFile file){
		try{
			List<Item> items = objectMapper.readValue(file.getInputStream(),new TypeReference<List<Item>>(){});
			System.err.println(items);
		}catch (IOException e){
			e.printStackTrace();
		}
	}
	
}