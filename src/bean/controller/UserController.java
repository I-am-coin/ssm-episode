package bean.controller;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import bean.pojo.User;
import bean.service.UserService;
import bean.utils.ImageCutUtil;

@Controller@RequestMapping("/user")
public class UserController {
	@Resource(name="userServiceImpl")
	private UserService userService;
	
	/**
	 * 用户登录处理
	 * @param user_id 用户编号
	 * @param user_password 用户密码
	 * @param page 登录页面
	 * @return 用户登录信息
	 */
	@RequestMapping("/userLogin")
	public ModelAndView userLogin(@RequestParam("username") String user_id, @RequestParam("password") String user_password, @RequestParam("page") String page) {// 通过ID获取用户
		ModelAndView model = new ModelAndView("/"+page);
		int flag = 0;
		User user = userService.getUserById(user_id);
		
		if (user!=null && user.getUser_password().equals(user_password)) {
			flag = 1;//登陆成功
			userService.updateLoginTimeByUserId(user_id);//更新用户最近一次登录时间
		}
		model.addObject("flag", flag);
		model.addObject("user", user);
		
		return model;
	}
	
	/**
	 * 异步尝试登录
	 * @param user_id
	 * @param user_password
	 * @return 是否成功信息flag
	 */
	@ResponseBody
	@RequestMapping(value="/userLogin_ajax", produces="application/json;charset=UTF-8")
	public Map<String, Object> userLogin_ajax(@RequestParam("username") String user_id, @RequestParam("password") String user_password) {// 通过ID获取用户
		Map<String, Object> data = new HashMap<String, Object>();
		int flag = 0;
		User user = userService.getUserById(user_id);
		
		if (user!=null && user.getUser_password().equals(user_password)) {
			flag = 1;//登陆成功
		}
		data.put("flag", flag);
		
		return data;
	}
	
	/**
	 * 通过用户昵称、用户密码注册用户，并返回用户id
	 * @param user_nickname 用户昵称
	 * @param user_password 用户密码
	 * @return 用户id
	 */
	@RequestMapping("/userRegister")
	public ModelAndView userRegister(@RequestParam("nickname") String user_nickname, @RequestParam("psw") String user_password) {// 通过ID获取用户
		ModelAndView model = new ModelAndView("/index");
		int user_id = userService.insertUser(user_nickname, user_password);
		model.addObject("user_id", user_id);
		System.out.println("Controller-user_id:"+user_id);
		return model;
	}
	
	/**
	 * 修改用户信息
	 * @param user_id 用户编号
	 * @param user_info 用户更新信息内容
	 * @param type 更改信息类型：1-昵称、2-性别、3-密码
	 * @return Map<String, Object> flag 是否成功：0-失败， 1-成功
	 */
	@ResponseBody
	@RequestMapping(value="/updateUserInfo_ajax", produces="application/json;charset=UTF-8")
	public Map<String, Object> updateUserInfo_ajax(@RequestParam("user_id") String user_id, @RequestParam("user_info") String user_info, @RequestParam("type") String type) {
		Map<String, Object> data = new HashMap<String, Object>();
		int flag = 0;
		try {
			if (type.equals("1")) {//修改昵称
				userService.updateNicknameByUserId(user_id, user_info);
			}
			else if (type.equals("2")) {//修改性别
				userService.updateGenderByUserId(user_id, user_info);
			}
			else if (type.equals("3")) {//修改密码
				userService.updatePasswordByUserId(user_id, user_info);
			}
			flag = 1;
		}
		catch (Exception e) {
			System.out.println("用户信息修改异常");
			e.printStackTrace();
		}
		
		data.put("flag", flag);
		return data;
	}
	
	/**
	 * 图片上传，剪切头像
	 * @param x 头像左上角x坐标
	 * @param y 头像左上角y坐标
	 * @param file 头像文件
	 * @param width 头像宽度
	 * @param height 头像高度
	 * @param user_id 用户编号
	 * @param request
	 * @return 成功
	 */
	@RequestMapping("/uploadImage")
	public ModelAndView uploadImage(@RequestParam(value="x1") String x, @RequestParam(value="y1") String y,
			@RequestParam(value="file") CommonsMultipartFile file,
			@RequestParam(value="w") String width, @RequestParam(value="h") String height,
			@RequestParam(value="user_id") String user_id, HttpServletRequest request) {
		//预防类型不匹配，造成400异常
		int x1 = (int)(Float.parseFloat(x));
		int y1 = (int)(Float.parseFloat(y));
		int w1 = (int)(Float.parseFloat(width));
		int h1 = (int)(Float.parseFloat(height));
		
		String type = "."+file.getContentType().substring(6, file.getContentType().length());
		String fileName = UUID.randomUUID().toString().replace("-", "")+type;//唯一文件名
		String path = "images/";//保存相对路径
		FileOutputStream fos = null;
		InputStream is = null;
		int flag = 0;

		try {
			String realPath = request.getServletContext().getRealPath(path+fileName);// 保存的绝对路径
			System.out.println(realPath);
			fos = new FileOutputStream(realPath, true); // 文件输出流
			is = file.getInputStream(); // 输入流
			int b = 0;
			
			while ((b = is.read()) != -1) { // 执行保存,保存初始图片
				fos.write(b);
			}
			if (ImageCutUtil.cutImage(realPath, realPath, x1, y1, w1, h1)) {//保存剪切后图片数据
				// 保存路径到数据库
				userService.updateImageByUserId(user_id, fileName);
				flag = 1;
			}
			System.out.println(realPath);
		} catch (Exception e) {
			System.out.println("图片上传失败");
			e.printStackTrace();
		} finally {
			try {
				if (fos != null) {
					fos.close();
				}
				if (is != null) {
					is.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		ModelAndView model = new ModelAndView("/personal_center");
		model.addObject("user_image",fileName);
		model.addObject("isUpload", flag);
		return model;
	}
	
	/**
	 * 时间任务，指定时间（每天0点）重复执行
	 * 删除3年未登录的用户
	 */
	@Scheduled(cron="0 0 0 * * ?")
	public void deleteUser() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		int time = Integer.parseInt(sdf.format(new Date()));//以int格式获取当前时间
		time -= 30000;//减去3年
		System.out.println("删除用户");
		userService.deleteUser(time+"");
		System.out.println("执行完成");
	}
}
