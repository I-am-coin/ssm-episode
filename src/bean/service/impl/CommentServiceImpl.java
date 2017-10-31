package bean.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bean.mapper.CommentMapper;
import bean.pojo.Comment;
import bean.service.CommentService;

@Service@Transactional
public class CommentServiceImpl implements CommentService {
	@Resource(name="commentMapper")
	private CommentMapper commentMapper;
	
	@Override
	public List<Comment> getCommentsByEpisodeId(String episode_id) {
		return commentMapper.getCommentsByEpisodeId(episode_id);
	}

	@Override
	public void addGoodComment(String comment_id, String user_id) {
		commentMapper.addGoodCommentById(comment_id);
		commentMapper.insertGoodComment(comment_id, user_id);
	}

	@Override
	public int getGoodComment(String comment_id, String user_id) {
		return commentMapper.getGoodComment(comment_id, user_id);
	}

	@Override
	public String insertComment(String comment_content, String user_id, String episode_id) {
		commentMapper.insertComment(comment_content, user_id, episode_id);
		return commentMapper.getNewCommentId();
	}

	@Override
	public void deleteComment(String comment_id) {
		commentMapper.deleteComment(comment_id);
	}
}
