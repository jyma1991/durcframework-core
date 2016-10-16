欢迎使用durcframework-core
===================
> **日常更新**
> - 2015-9-7,1.0.15版本中去掉了jackson依赖,需要在自己的项目中添加.
> - 2015-6-30,新增QBC类,支持QBC查询
> - 2015-5-15,新建了一个QQ群:328180219欢迎加群探讨 :)
> - 2015-4-17更新,添加不规则翻页查询.[点击查看](http://git.oschina.net/durcframework/durcframework-core/wikis/%E4%BD%BF%E7%94%A8%E4%B8%8D%E8%A7%84%E5%88%99%E7%BF%BB%E9%A1%B5%E6%9F%A5%E8%AF%A2)

项目介绍
-------------------

> **durcframework-core**

> - durcframework-core是一个基于SpringMVC + Mybatis的框架。其设计目的在于提高开发效率，避免做重复的工作。尤其是在做管理后台时，能减少许多代码量。

> **框架用到的技术点**

> 1. 采用泛型设计，对数据库的增删改查做了适度的封装。只需少量代码就能完成一个模块的CRUD操作。
> 2. 使用注解来生成查询条件，mybatis文件不需要额外配置，减少了mybatis的代码量。
> 3. 使用Java代码动态生成查询条件，可以根据不同场景，不同业务来组装查询条件。
> 4. 可以配合前台做Ajax开发，传输JSON格式数据，也可以使用传统SpringMVC到jsp页面。
> 5. 后台自动验证功能，支持JSR-303。

> **使用本框架可以完成的事：**

> 1. 少量代码完成对一张表的增删改查。
> 2. 数据导出
> 3. 数据校验

> **使用本框架完成的项目：**

> - [autoCode](https://git.oschina.net/durcframework/autoCode),一款代码生成器;
> - [rms](https://git.oschina.net/durcframework/rms),一个权限管理系统;
> - [durcframework-open](https://git.oschina.net/durcframework/durcframework-open),一个开放平台(未完善)

框架对应[demo](https://git.oschina.net/durcframework/durcframework-core.demo)

----------
简单列子
----------
Controller完成对学生表的增删改查

controller类
```
// 继承CrudController,表示该Controller具有增删改查功能
@Controller
public class StudentCrudController extends CrudController<Student, StudentService> {
	
	// 模拟表单提交
	// http://localhost/durcframeworkTest/addStudent.do?name=Lucy&stuNo=No00001971&address=USA&mobile=13345678951&birthday=1987-07-06
	@RequestMapping("/addStudent.do")
	public @ResponseBody MessageResult addStudent(Student student) {
		this.save(student);
		System.out.println("添加后的主键ID:"+ student.getId());
		return success();
	}
	
	// http://localhost/durcframeworkTest/listStudent.do
	@RequestMapping("/listStudent.do")
	public @ResponseBody GridResult listStudent(SearchStudentEntity searchStudentEntity) {
		return this.query(searchStudentEntity);
	}
	
	// 模拟表单提交,修改姓名为Lily
	// http://localhost/durcframeworkTest/updateStudent.do?id=39&name=Lily&stuNo=36251111
	@RequestMapping("/updateStudent.do")
	public @ResponseBody MessageResult updateStudent(Student student) {
		return this.update(student);
	}
	
	// 传一个id值即可,根据主键删除
	// http://localhost/durcframeworkTest/delStudent.do?id=39
	@RequestMapping("/delStudent.do")
	public @ResponseBody MessageResult delStudent(Student student) {
		// 通过主键查询某一条记录
		System.out.println(this.getService().get(student.getId()));
		return this.delete(student);
	}
	
	
}
```

Service类
```
// 只需简单继承无需其它代码
@Service
public class StudentService extends CrudService<Student, StudentDao> {}
```

Dao层
```

// 只需简单继承无需其它代码
public interface StudentDao extends BaseDao<Student> {}
```

----------
QBC查询
```
// 查询姓名为Jim,并且id是20和25的学生
// 查询结果以name字段升序

// SELECT * FROM student t WHERE name = 'Jim' AND id IN ( 20,25 ) ORDER BY
// name ASC
QBC qbc = QBC.create(StudentDao.class);
List list = qbc
		.eq("name", "Jim")
		.in("id", Arrays.asList(20, 25))
		.sort("name")
		.listAll();
		
//-----------------------------------------
// 查询姓名为Jim,并且id是20和25的学生,并且部门ID为16
// 查询结果以name字段升序

// SELECT * FROM student t INNER JOIN department t2 ON
// t.DEPARTMENT=t2.ID WHERE name = 'Jim' AND t2.ID = 16 AND t.id IN ( 20,25 )
// ORDER BY name ASC
QBC qbc = QBC.create(StudentDao.class);
List list = qbc.innerJoin("department", "t2", "DEPARTMENT", "ID")
		.eq("name", "Jim")
		.in("t.id", Arrays.asList(20, 25))
		.eq("t2.ID", 16)
		.sort("name")
		.listAll();
```