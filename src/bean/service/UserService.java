package bean.service;

import bean.pojo.User;

public interface UserService {
	/**
	 * 通过用户编号获取用户
	 * @param user_id
	 * @return pojo.User
	 */
	public User getUserById(String user_id);
	/**
	 * 提交用户昵称、用户密码增加用户,返回用户id
	 * @param user_nickname 用户昵称
	 * @param user_password 用户密码
	 * @return 新增用户id
	 */
	public int insertUser(String user_nickname, String user_password);
	/**
	 * 修改用户昵称
	 * @param user_id 用户编号
	 * @param user_nickname 用户昵称
	 */
	public void updateNicknameByUserId(String user_id, String user_nickname);
	/**
	 * 修改用户性别
	 * @param user_id 用户编号
	 * @param user_gender 用户性别：0-女，1-男
	 */
	public void updateGenderByUserId(String user_id, String user_gender);
	/**
	 * 修改用户密码
	 * @param user_id 用户编号
	 * @param user_password 用户密码
	 */
	public void updatePasswordByUserId(String user_id, String user_password);
	
	/**
	 * 修改头像
	 * @param user_id 用户编号
	 * @param user_image 图片路径
	 */
	public void updateImageByUserId(String user_id, String user_image);
	
	/**
	 * 更新用户login_time
	 * @param user_id 用户id
	 */
	public void updateLoginTimeByUserId(String user_id);
	
	/**
	 * 删除长时间未登录的用户
	 * @param login_time 指定的登录时间
	 */
	public void deleteUser(String login_time);
}
