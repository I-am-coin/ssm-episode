package bean.mapper;

import bean.pojo.User;

public interface UserMapper {
	//通过用户编号获取用户
	public User getUserById(String user_id);
	//提交用户昵称、用户密码增加用户
	public void insertUser(String user_id, String user_nickname, String user_password);
	//获取预新增用户id
	public int getNewUserId();
	//修改用户昵称
	public void updateNicknameByUserId(String user_id, String user_nickname);
	//修改用户性别
	public void updateGenderByUserId(String user_id, String user_gender);
	//修改用户密码
	public void updatePasswordByUserId(String user_id, String user_password);
	//修改头像
	public void updateImageByUserId(String user_id, String user_image);
	//更新用户login_time
	public void updateLoginTimeByUserId(String user_id);
	//删除长时间未登录的用户
	public void deleteUser(String login_time);
}
