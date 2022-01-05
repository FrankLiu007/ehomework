$(()=>{
	const CODE_SUCCESS = 1,CODE_MISSING_TOKEN = 2,CODE_EXPIRED_TOKEN = 3
	const ADMIN = "admin",TEACHER = "teacher",STUDENT = "student",COURSE = "course",MATERIAL = "material";
	const CARRIER = {BOOK:{code:1,name:"书"},PAPER:{code:2,name:"试卷"}};
	const SUBJECT = {CHINESE:{code:1,name:"语文"},MATHEMATICS:{code:2,name:"数学"},MATHS_ARTS:{code:3,name:"文数"},MATHS_SCIENCE:{code:4,name:"理数"},ENGLISH:{code:5,name:"英语"},
			INTEGRATION_ARTS:{code:6,name:"文综"},POLITICS:{code:7,name:"政治"},HISTORY:{code:8,name:"历史"},GEOGRAPHY:{code:9,name:"地理"},
			INTEGRATION_SCIENCE:{code:10,name:"理综"},PHYSICS:{code:11,name:"物理"},CHEMISTRY:{code:12,name:"化学"},BIOLOGY:{code:13,name:"生物"}};
	let isFunction = varible=>typeof varible == "function"
	let isJQuery = varible=>varible instanceof $
	let initSearchSelect = (selector,options)=>{
		let placeholder = options.placeholder||"请输入信息以筛选";
		let search = options.search
		let load = options.load
		if(isJQuery($(selector))) $(selector).each((index,element)=>{
			$(element).addClass("search-select")
			let searchInput = $("<input/>")
			searchInput.addClass("search")
			searchInput.attr("type","text")
			searchInput.attr("placeholder",placeholder)
			let selectUl = $("<ul></ul>")
			selectUl.addClass("select").addClass("hide")
			let valueInput = $("<input/>")
			valueInput.attr("type","hidden")
			if(isFunction(search) && isFunction(load)){
				searchInput.focus(event=>{
					search(searchInput.val(),data=>load(data,searchInput,selectUl,valueInput))
					selectUl.removeClass("hide")
				})
				searchInput.blur(event=>selectUl.addClass("hide"))
				searchInput.bind("input propertychange",()=>search(searchInput.val(),data=>load(data,searchInput,selectUl,valueInput)))
			}
			$(selector).append(searchInput).append(selectUl).append(valueInput)
		})
	}
	let handleResult = (result,success)=>{
		switch(result.code){
			case CODE_SUCCESS:
				if(isFunction(success)) success(result)
				break
			case CODE_MISSING_TOKEN:
			case CODE_EXPIRED_TOKEN:
				window.location.href = "/admin/login"
				break
			default:
				break
		}
	}
	let loadTeacher = (teachers,searchInput,selectUl,valueInput)=>{
		selectUl.empty()
		if(teachers) teachers.forEach(teacher=>{
			let li = $("<li></li>")
			li.html(teacher.name)
			li.mousedown(event=>{
				searchInput.val(teacher.name)
				valueInput.val(teacher.teacherId)
			})
			selectUl.append(li)
		})
	}
	let searchTeacher = (keyword,loadData)=>{
		$.ajax({
			url: "/admin/teacher/search",
			beforeSend: xhr=>xhr.setRequestHeader(TOKEN_KEY,$.cookie(TOKEN)),
			data: {
				keyword: keyword
			},
			dateType: "json",
			success: (result,status,xhr)=>handleResult(result,result=>loadData(result.data))
		})
	}
	switch(menu){
		case ADMIN:
			listAdmin();
			$("#create-admin").click(event=>{
				let account = $("#account-admin").val();
				if(validate(account)&&validate(account.trim())){
					account = account.trim();
					$("#account-admin").removeClass("warn");
				}else{
					$("#account-admin").addClass("warn");
					return;
				}
				let password = $("#password-admin").val();
				if(validate(password)&&validate(password.trim())){
					password = password.trim();
					$("#password-admin").removeClass("warn");
				}else{
					$("#password-admin").addClass("warn");
					return;
				}
				$.ajax({
					url: "/admin/admin",
					type: "post",
					beforeSend: xhr=>xhr.setRequestHeader(TOKEN_KEY,$.cookie(TOKEN)),
					data: {
						account: account,
						password: password
					},
					dateType: "json",
					success: (result,status,xhr)=>{
						switch(result.code){
							case 1:
								listAdmin();
								$("#account-admin").val("");
								$("#password-admin").val("");
								break;
							case 2:
							case 3:
								window.location.href = "/admin/login";
								break;
							default:
								break;
						}
					}
				});
			});
			break;
		case TEACHER:
			loadSubject();
			listTeacher();
			$("#avatar-teacher-div").click(event=>$("#avatar-teacher").click());
			$("#avatar-teacher").change(function(event){
				if(this.files[0]){
					$("#avatar-button").addClass("hide");
					$("#avatar-img").attr("src",URL.createObjectURL(this.files[0]));
					$("#avatar-img").removeClass("hide");
				}else{
					$("#avatar-img").addClass("hide");
					$("#avatar-button").removeClass("hide");
				}
			});
			$("#create-teacher").click(event=>{
				let account = $("#account-teacher").val();
				if(validate(account)&&validate(account.trim())){
					account = account.trim();
					$("#account-teacher").removeClass("warn");
				}else{
					$("#account-teacher").addClass("warn");
					return;
				}
				let password = $("#password-teacher").val();
				if(validate(password)&&validate(password.trim())){
					password = password.trim();
					$("#password-teacher").removeClass("warn");
				}else{
					$("#password-teacher").addClass("warn");
					return;
				}
				let name = $("#name-teacher").val();
				if(validate(name)&&validate(name.trim())){
					name = name.trim();
					$("#name-teacher").removeClass("warn");
				}else{
					$("#name-teacher").addClass("warn");
					return;
				}
				let subjectCode = $("#subject-teacher").val();
				if(validate(subjectCode)){
					$("#subject-teacher").removeClass("warn");
				}else{
					$("#subject-teacher").addClass("warn");
					return;
				}
				let phone = $("#phone-teacher").val();
				if(/^1\d{10}$/.test(phone)){
					$("#phone-teacher").removeClass("warn");
				}else{
					$("#phone-teacher").addClass("warn");
					return;
				}
				let data = new FormData();
				data.append("account",account);
				data.append("password",password);
				data.append("name",name);
				data.append("subjectCode",subjectCode);
				data.append("avatar",$("#avatar-teacher")[0].files[0]);
				data.append("phone",phone);
				$.ajax({
					url: "/admin/teacher",
					type: "post",
					beforeSend: xhr=>xhr.setRequestHeader(TOKEN_KEY,$.cookie(TOKEN)),
					data: data,
					contentType: false,
					processData: false,
					dataType: "json",
					success: (result,status,xhr)=>{
						switch(result.code){
							case 1:
								listTeacher();
								$("#account-teacher").val("");
								$("#password-teacher").val("");
								$("#name-teacher").val("");
								$("#subject-teacher option:first").prop("selected",true);
								$("#avatar-teacher").val("");
								$("#phone-teacher").val("");
								break;
							case 2:
							case 3:
								window.location.href = "/admin/login";
								break;
							default:
								break;
						}
					}
				})
			});
			break;
		case STUDENT:
			listStudent();
			$("#create-student").click(event=>{
				let account = $("#account-student").val();
				if(validate(account)&&validate(account.trim())){
					account = account.trim();
					$("#account-student").removeClass("warn");
				}else{
					$("#account-student").addClass("warn");
					return;
				}
				let password = $("#password-student").val();
				if(validate(password)&&validate(password.trim())){
					password = password.trim();
					$("#password-student").removeClass("warn");
				}else{
					$("#password-student").addClass("warn");
					return;
				}
				let name = $("#name-student").val();
				if(validate(name)&&validate(name.trim())){
					name = name.trim();
					$("#name-student").removeClass("warn");
				}else{
					$("#name-student").addClass("warn");
					return;
				}
				$.ajax({
					url: "/admin/student",
					type: "post",
					beforeSend: xhr=>xhr.setRequestHeader(TOKEN_KEY,$.cookie(TOKEN)),
					data: {
						account: account,
						password: password,
						name: name
					},
					dataType: "json",
					success: (result,status,xhr)=>{
						switch(result.code){
							case 1:
								listStudent();
								$("#account-student").val("");
								$("#password-student").val("");
								$("#name-student").val("");
								break;
							case 2:
							case 3:
								window.location.href = "/admin/login";
								break;
							default:
								break;
						}
					}
				})
			});
			break;
		case COURSE:
			initSearchSelect("#search-select-teacher",{
				search: searchTeacher,
				load: loadTeacher
			})
			listCourse();
			break;
		case MATERIAL:
			loadCarrier();
			loadSubject();
			listMaterial();
			$("#questions-div").click(event=>$("#questions").click());
			$("#answers-div").click(event=>$("#answers").click());
			$("#questions").change(function(event){
				let questions = this.files[0];
				if(questions){
					$("#questions-name").html(questions.name);
					$("#questions-button").addClass("hide");
					$("#questions-name").removeClass("hide");
				}else{
					$("#questions-name").html("");
					$("#questions-name").addClass("hide");
					$("#questions-button").removeClass("hide");
				}
			});
			$("#preview").click(event=>{
				let subjectCode = $("#subject-material").val();
				if(validate(subjectCode)){
					$("#subject-material").removeClass("warn");
				}else{
					$("#subject-material").addClass("warn");
					return;
				}
				let carrierCode = $("#carrier-material").val();
				if(validate(carrierCode)){
					$("#carrier-material").removeClass("warn");
				}else{
					$("#carrier-material").addClass("warn");
					return;
				}
				let questions = $("#questions")[0].files[0];
				if(questions&&/\.docx$/.test(questions.name)){
					$("#questions-div").children().removeClass("warn");
				}else{
					$("#questions-div").children().addClass("warn");
					return;
				}
				let name = $("#name-material").val();
				let data = new FormData();
				data.append("questions",questions);
				data.append("subjectCode",subjectCode);
				data.append("name",name);
				switch(carrierCode){
					case "1":
						break;
					case "2":
						$("#loading-modal").removeClass("hide");
						$.ajax({
							url: "/admin/paper",
							type: "post",
							beforeSend: xhr=>xhr.setRequestHeader(TOKEN_KEY,$.cookie(TOKEN)),
							data: data,
							contentType: false,
							processData: false,
							dataType: "json",
							success: (result,status,xhr)=>{
								$("#loading-modal").addClass("hide");
								switch(result.code){
									case 1:
										$("#preview-modal").removeClass("hide");
										let name = result.name;
										let items = result.data;
										$("#page").empty();
										$("#page").append($("<div class='name'>"+name+"</div>"));
										if(validate(items)) items.forEach(item=>{
											if(validate(item)){
												let itemDiv = $("<div class='item'></div>");
												$("#page").append(itemDiv);
												let title = item.title;
												if(validate(title)) itemDiv.append("<div class='title'>"+title+"</div>");
												let questions = item.questions;
												if(validate(questions)) questions.forEach(question=>{
													if(validate(question)){
														let questionDiv = $("<div class='question'></div>");
														itemDiv.append(questionDiv);
														let stem = question.stem;
														if(validate(stem)){
															questionDiv.append("<div class='number'>"+question.number+"</div>");
															questionDiv.append("<div class='stem'>"+stem+"</div>");
														}else{
															questionDiv.append("<div class='number'>"+question.number+"</div>");
														}
														let type = question.type;
														let options = question.options;
														if(["SINGLE","MULTIPLE"].includes(type) && validate(options)){
															options.forEach(option=>{
																if(validate(option)){
																	questionDiv.append($("<div class='option'></div>").append($("<span class='label'>"+option.label+"</span>")).append($("<span>"+option.content+"</span>")));
																}
															});
														}
													}
												})
												itemDiv.append($("<div class='expand'>解析答案<span class='iconfont'></span></div>"))
												let reference = item.reference;
												if(validate(reference)) itemDiv.append($("<div class='reference'>"+reference+"</div>"))
												$(".item").click(function(event){
													$(this).siblings().removeClass("selected")
													$(this).addClass("selected")
												})
											}
										});
										MathJax.typesetPromise()
										$("#negative").unbind("click")
										$("#positive").unbind("click")
										let uuid = result.uuid
										$("#negative").click(event=>
											$.ajax({
												url: "/admin/paper/"+uuid,
												type: "post",
												beforeSend: (xhr,settings)=>xhr.setRequestHeader(TOKEN_KEY,$.cookie(TOKEN)),
												data: {confirm:false},
												success: (result,status,xhr)=>{
													switch(result.code){
														case 1:
															break
														case 2:
														case 3:
															window.location.href = "/admin/login"
															break
														default:
															break
													}
												},
												complete: (xhr,status)=>$("#preview-modal").addClass("hide")
											})
										)
										$("#positive").click(event=>
											$.ajax({
												url: "/admin/paper/"+uuid,
												type: "post",
												beforeSend: (xhr,settings)=>xhr.setRequestHeader(TOKEN_KEY,$.cookie(TOKEN)),
												data: {confirm:true},
												success: (result,status,xhr)=>{
													switch(result.code){
														case 1:
															break
														case 2:
														case 3:
															window.location.href = "/admin/login"
															break
														default:
															break
													}
												},
												complete: (xhr,status)=>$("#preview-modal").addClass("hide")
											})
										)
										break;
									case 2:
									case 3:
										window.location.href = "/admin/login";
										break;
									default:
										break;
								}
							},
							error: (xhr,status,error)=>$("#loading-modal").addClass("hide")
						})
						break;
					default:
						break;
				}
			});
			break;
		default:
			break;
	}
	function listAdmin(){
		$("#f-admin").siblings(".tr").remove();
		$.ajax({
			url: "/admin/admin",
			beforeSend: xhr=>xhr.setRequestHeader(TOKEN_KEY,$.cookie(TOKEN)),
			dataType: "json",
			success: (result,status,xhr)=>{
				switch(result.code){
					case 1:
						let admins = result.data;
						if(admins) admins.forEach(admin=>{
							let button = $("<button>删除</button>");
							button.click(event=>{
								$.ajax({
									url: "/admin/admin/"+admin.id,
									type: "delete",
									beforeSend: xhr=>xhr.setRequestHeader(TOKEN_KEY,$.cookie(TOKEN)),
									dataType: "json",
									success: (result,status,xhr)=>{
										switch(result.code){
											case 1:
												listAdmin();
												break;
											case 2:
											case 3:
												window.location.href = "/admin/login";
												break;
											default:
												break;
										}
									}
								})
							});
							$("#f-admin").before($("<li class='tr'></li>")
									.append($("<div class='td'>"+admin.id+"</div>"))
									.append($("<div class='td'>"+admin.account+"</div>"))
									.append($("<div class='td'>******</div>"))
									.append($("<div class='td'></div>").append(button)));
						})
						break;
					case 2:
					case 3:
						window.location.href = "/admin/login";
						break;
					default:
						break;
				}
			}
		})
	}
	function loadSubject(){
		for(let key in SUBJECT){
			let value = SUBJECT[key];
			$("#subject-teacher").append("<option value='"+value.code+"'>"+value.name+"</option>");
			$("#subject-material").append("<option value='"+value.code+"'>"+value.name+"</option>");
		}
	}
	function listTeacher(){
		$("#f-teacher").siblings(".tr").remove();
		$.ajax({
			url: "/admin/teacher",
			beforeSend: xhr=>xhr.setRequestHeader(TOKEN_KEY,$.cookie(TOKEN)),
			dataType: "json",
			success: (result,status,xhr)=>{
				switch(result.code){
					case 1:
						let teachers = result.data;
						if(teachers) teachers.forEach(teacher=>$("#f-teacher").before($("<li class='tr'></li>")
							.append($("<div class='td'>"+teacher.teacherId+"</div>"))
							.append($("<div class='td'>"+teacher.account+"</div>"))
							.append($("<div class='td'>******</div>"))
							.append($("<div class='td'>"+teacher.name+"</div>"))
							.append($("<div class='td'>"+SUBJECT[teacher.subject].name+"</div>"))
							.append($("<div class='td'><img src='"+teacher.avatar+"'/></div>"))
							.append($("<div class='td'>"+teacher.phone+"</div>"))
							.append($("<div class='td'><button>删除</button></div>"))));
						break;
					case 2:
					case 3:
						window.location.href = "/admin/login";
						break;
					default:
						break;
				}
			}
		})
	}
	function listStudent(){
		$("#f-student").siblings(".tr").remove();
		$.ajax({
			url: "/admin/student",
			beforeSend: xhr=>xhr.setRequestHeader(TOKEN_KEY,$.cookie(TOKEN)),
			dataType: "json",
			success: (result,status,xhr)=>{
				switch(result.code){
					case 1:
						let students = result.data;
						if(students) students.forEach(student=>$("#f-student").before($("<li class='tr'></li>")
							.append($("<div class='td'>"+student.studentId+"</div>"))
							.append($("<div class='td'>"+student.account+"</div>"))
							.append($("<div class='td'>******</div>"))
							.append($("<div class='td'>"+student.name+"</div>"))
							.append($("<div class='td'><button>删除</button></div>"))));
						break;
					case 2:
					case 3:
						window.location.href = "/admin/login";
						break;
					default:
						break;
				}
			}
		})
	}
	function listCourse(){
		$("#f-course").siblings(".tr").remove();
		$.ajax({
			url: "/admin/course",
			beforeSend: xhr=>xhr.setRequestHeader(TOKEN_KEY,$.cookie(TOKEN)),
			dataType: "json",
			success: (result,status,xhr)=>{
				switch(result.code){
					case 1:
						let courses = result.data;
						if(courses) courses.forEach(course=>$("#f-course").before($("<li class='tr'></li>")
							.append($("<div class='td'>"+course.courseId+"</div>"))
							.append($("<div class='td'>"+course.name+"</div>"))
							.append($("<div class='td'>"+course.teacher.name+"</div>"))
							.append($("<div class='td'>"+course.studentNumber+"</div>"))
							.append($("<div class='td'><button>删除</button></div>"))));
						break;
					case 2:
					case 3:
						window.location.href = "/admin/login";
						break;
					default:
						break;
				}
			}
		})
	}
	function listMaterial(){
		$("#f-material").siblings(".tr").remove();
		$.ajax({
			url: "/admin/material",
			beforeSend: xhr=>xhr.setRequestHeader(TOKEN_KEY,$.cookie(TOKEN)),
			dataType: "json",
			success: (result,status,xhr)=>{
				switch(result.code){
					case 1:
						let materials = result.data;
						if(materials) materials.forEach(material=>$("#f-material").before($("<li class='tr'></li>")
							.append($("<div class='td'>"+material.materialId+"</div>"))
							.append($("<div class='td xl'>"+material.name+"</div>"))
							.append($("<div class='td'>"+SUBJECT[material.subject].name+"</div>"))
							.append($("<div class='td'>"+CARRIER[material.carrier].name+"</div>"))
							.append($("<div class='td l'>2019/11/21 15:02</div>"))
							.append($("<div class='td'><button>删除</button></div>"))));
						break;
					case 2:
					case 3:
						window.location.href = "/admin/login";
						break;
					default:
						break;
				}
			}
		})
	}
	function loadCarrier(){
		for(let key in CARRIER){
			let value = CARRIER[key];
			$("#carrier-material").append($("<option value='"+value.code+"'>"+value.name+"</option>"));
		}
	}
	function validate(value){
		return value&&value!=null&&value!="";
	}
})