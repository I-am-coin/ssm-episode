package bean.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bean.mapper.UserMapper;
import bean.pojo.User;
import bean.service.UserService;

@Service@Transactional
public class UserServiceImpl implements UserService {
	@Resource(name="userMapper")
	private UserMapper userMapper;
	
	@Override
	public User getUserById(String user_id) {
		return userMapper.getUserById(user_id);
	}

	@Override
	public int insertUser(String user_nickname, String user_password) {
		int user_id = userMapper.getNewUserId();
		userMapper.insertUser(user_id+"", user_nickname, user_password);
		return user_id;
	}

	@Override
	public void updateNicknameByUserId(String user_id, String user_nickname) {
		userMapper.updateNicknameByUserId(user_id, user_nickname);
	}

	@Override
	public void updateGenderByUserId(String user_id, String user_gender) {
		userMapper.updateGenderByUserId(user_id, user_gender);
	}

	@Override
	public void updatePasswordByUserId(String user_id, String user_password) {
		userMapper.updatePasswordByUserId(user_id, user_password);
	}

	@Override
	public void updateImageByUserId(String user_id, String user_image) {
		userMapper.updateImageByUserId(user_id, user_image);
	}

	@Override
	public void updateLoginTimeByUserId(String user_id) {
		userMapper.updateLoginTimeByUserId(user_id);
	}

	@Override
	public void deleteUser(String login_time) {
		userMapper.deleteUser(login_time);
	}

}
