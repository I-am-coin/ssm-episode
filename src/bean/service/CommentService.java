package bean.service;

import java.util.List;

import bean.pojo.Comment;


public interface CommentService {
	/**
	 * 通过段子编号获取相应的评论数据
	 * @param episode_id 段子编号
	 * @return 评论信息
	 */
	public List<Comment> getCommentsByEpisodeId(String episode_id);
	
	/**
	 * 增加点赞评论数
	 * @param comment_id 评论编号
	 * @param user_id 用户编号
	 */
	public void addGoodComment(String comment_id, String user_id);
	
	/**
	 * 获取点赞评论数据
	 * @param comment_id 评论编号
	 * @param user_id 用户编号
	 * @return 是否查询到数据， 1-是，0-否
	 */
	public int getGoodComment(String comment_id, String user_id);
	
	/**
	 * 新增评论
	 * @param Comment对象
	 * @return 新增评论id
	 */
	public String insertComment(String comment_content, String user_id, String episode_id);
	
	/**
	 * 删除评论
	 * @param user_id 用户编号
	 * @param episode_id 段字编号
	 */
	public void deleteComment(String comment_id);
}
