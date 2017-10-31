package bean.mapper;

import java.util.List;

import bean.pojo.Comment;


public interface CommentMapper {
	//获取指定段子编号的所有评论
	public List<Comment> getCommentsByEpisodeId(String episode_id);
	//通过评论id增加评论点赞数
	public void addGoodCommentById(String comment_id);
	//向点赞评论表插入一条数据
	public void insertGoodComment(String comment_id, String user_id);
	//获取点赞评论数据
	public int getGoodComment(String comment_id, String user_id);
	//新增评论
	public void insertComment(String comment_content, String user_id, String episode_id);
	//获取最新添加评论编号
	public String getNewCommentId();
	//删除评论
	public void deleteComment(String comment_id);
}
