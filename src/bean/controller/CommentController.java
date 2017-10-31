package bean.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import bean.pojo.Comment;
import bean.pojo.User;
import bean.service.CommentService;
import bean.service.UserService;

@Controller@RequestMapping("/comment")
public class CommentController {
	@Resource(name="commentServiceImpl")
	private CommentService commentService;
	@Resource(name="userServiceImpl")
	private UserService userService;
	
	/**
	 * 实现评论分页数据的异步获取
	 * @param episode_id 段子编号
	 * @param page_num 页号
	 * @return 评论分页数据和对应用户数据
	 */
	@ResponseBody
	@RequestMapping(value="/getCommentsAndUsersByEpisodeId_ajax", produces="application/json;charset=UTF-8")
	public Map<String, Object> getCommentsAndUsersByEpisodeId_ajax(@RequestParam("episode_id") String episode_id, @RequestParam("page_num") int page_num) {
		Map<String, Object> data = new HashMap<String, Object>();
		PageHelper.startPage(page_num, 10);//开始分页
		List<Comment> comments = commentService.getCommentsByEpisodeId(episode_id); 
		PageInfo<Comment> page_comments = new PageInfo<Comment>(comments);
		List<User> users = new ArrayList<User>();
		
		for (int i=0; i<page_comments.getList().size(); i++) {//遍历获取用户数据
			users.add(userService.getUserById(page_comments.getList().get(i).getUser_id()));
		}
		data.put("comments", page_comments);
		data.put("users", users);
		
		return data;
	}
	
	/**
	 * 点赞评论
	 * @param comment_id 评论编号
	 * @param user_id 用户编号
	 */
	@ResponseBody
	@RequestMapping(value="/addGoodComment_ajax", produces="application/json;charset=UTF-8")
	public void addGoodComment_ajax(@RequestParam("comment_id")String comment_id, @RequestParam("user_id")String user_id){
		commentService.addGoodComment(comment_id, user_id);
	}
	
	/**
	 * 通过评论编号和用户编号查询是否已经点赞
	 * @param comment_ids 评论编号集合
	 * @param user_id 用户编号
	 * @return 是否点赞
	 */
	@ResponseBody
	@RequestMapping(value="/getGoodComment_ajax", produces="application/json;charset=UTF-8")
	public Map<String, Object> getGoodComment_ajax(@RequestParam("comment_ids") String comment_ids, @RequestParam("user_id") String user_id) {
		Map<String, Object> data = new HashMap<String, Object>();
		List<Integer> good = new ArrayList<>();
		String[] ids = comment_ids.split(",");
		
		for (int i=0; i<ids.length && ids[i] != ""; i++) {
			good.add(commentService.getGoodComment(ids[i], user_id));
		}
		data.put("good", good);
		return data;
	}
	
	/**
	 * 新增评论
	 * @param comment对象
	 */
	@ResponseBody
	@RequestMapping(value="/addComment_ajax", produces="application/json;charset=UTF-8")
	public Map<String, Object> addComment_ajax(@RequestParam("comment_content")String comment_content, @RequestParam("user_id")String user_id, @RequestParam("episode_id")String episode_id){
		Map<String, Object> data = new HashMap<String, Object>();
		 String comment_id = commentService.insertComment(comment_content, user_id, episode_id);
		 
		 data.put("comment_id", comment_id);
		 return data;
	}
	
	/**
	 * 删除评论
	 * @param comment_id 评论编号
	 */
	@ResponseBody
	@RequestMapping(value="/removeComment_ajax", produces="application/json;charset=UTF-8")
	public void removeComment_ajax(@RequestParam("comment_id")String comment_id){
		commentService.deleteComment(comment_id);
	}
}
