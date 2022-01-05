package com.zxrh.ehomework.common.constant;

public interface MapKey{

	String CODE = "code";
	
	int SUCCESS = 1;
	int MISSING_TOKEN = 2;
	int INVALID_TOKEN = 3;
	int INVALID_METHOD = 4;
	int INVALID_PARAMETER = 5;
	int NOT_PERMITTED = 6;
	int FAIL = 7;
	
	String MESSAGE = "message";
	
	String DATA = "data";
	
	String TOTAL = "total";
	
	String ALL = "all";

    String UNSUBMITTED = "unsubmitted";

    String UNCORRECTED = "uncorrected";

    String CORRECTED = "corrected";
	
	String LEVEL = "level";
	
	String TOKEN = "token";
	
	String AUTHENTICATION = "Authentication";

	String UUID = "uuid";

	String NAME = "name";

	String MAX_PAGE_NUM = "maxPageNum";

	String HAS_PREVIOUS = "hasPrevious";

	String HAS_NEXT = "hasNext";

	String WRONG = "wrong";

	String UNSUBMITTED_REMIND = "unsubmittedRemind";

	String SUBMITTED_REMIND = "submittedRemind";

	String HOMEWORK_REMIND = "homeworkRemind";

	String TEST_REMIND = "testRemind";

	String MISTAKE_REMIND = "mistakeRemind";

}