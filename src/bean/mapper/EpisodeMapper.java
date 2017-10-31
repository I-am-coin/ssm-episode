package bean.mapper;

import java.util.List;

import bean.pojo.Episode;

public interface EpisodeMapper {
	//获取所有段子信息
	public List<Episode> getEpisodes();
	//通过段子编号获取单个段子信息
	public Episode getEpisodeById(String episode_id);
	//通过用户编号获取个人收藏的段子
	public List<Episode> getEpisodeByUserId(String user_id);
	
	//通过段子编号增加段子点赞数
	public void addEpisodeGoodById(String episode_id);
	//将用户id和段子id关联到段子点赞表
	public void insertEpisode_Good(String user_id, String episode_id);
	
	//通过段子编号和用户编号查询点赞段子表中是否有数据
	public int getGoodEpisode(String episode_id, String user_id);
	//通过段子编号和用户编号查询收藏表中是否有数据
	public int getCollect(String episode_id, String user_id);
	
	//添加收藏段子
	public void insertCollectEpisode(String user_id, String episode_id);
	//取消收藏段子
	public void deleteCollectEpisode(String user_id, String episode_id);
	
	//删除指定时间前的段子
	public void deleteEpisode(String add_time);
	//添加段子
	public void insertEpisode(String episode_content);
}
