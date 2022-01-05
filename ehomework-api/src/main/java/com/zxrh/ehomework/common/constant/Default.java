package com.zxrh.ehomework.common.constant;

import java.io.File;

public interface Default{

	String SUPER_ADMIN = "admin";
	
	String TEACHER_AVATAR = "https://ehomework.oss-cn-hangzhou.aliyuncs.com/avatar/teacher_default.png";
	
	String EMPTY = "";
	String HYPHEN = "-";
	String COMMA = ",";
	String PERIOD = ".";
	String SLASH = "/";
	String BACKSLASH = "\\";
	String DOUBLE_QUOTE = "\"";
	
	String PAPER_PATH = "paper" + File.separator;
	String DOCX_PATH = "docx" + File.separator;
	String QUESTIONS_PATH = "questions.docx";
	String ANSWERS_PATH = "answers.docx";
	String IMG_PATH = "img" + File.separator;
	String MATERIAL_PATH = "material.json";
	String ITEMS_PATH = "items.json";
	
	String PYTHON = "python3";
	String PYTHON_SCRIPT = "docx2json.py";

	Integer CODE_SUCCESS = 1;

	
	
}