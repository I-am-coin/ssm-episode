package bean.service;

import java.util.List;

import bean.pojo.Episode;

public interface EpisodeService {
	/**
	 * 获取所有段子信息
	 * @return List<Episode> 所有段子信息
	 */
	public List<Episode> getEpisodes();
	/**
	 * 通过段子编号获取单个段子信息
	 * @param episode_id 段子id
	 * @return Episode 单个段子信息
	 */
	public Episode getEpisodeById(String episode_id);
	
	/**
	 * 通过用户编号获取个人收藏的段子
	 * @param user_id 用户编号
	 * @return 段子信息
	 */
	public List<Episode> getEpisodeByUserId(String user_id);
	
	/**
	 * 通过用户id和段子id处理点赞
	 * @param user_id 用户编号
	 * @param episode_id 段子编号
	 * @return 是否操作成功 1-是，0-否
	 */
	public int addEpisodeGood(String user_id, String episode_id);
	
	/**
	 * 通过段子编号和用户编号查询点赞段子表中是否有数据
	 * @param episode_id 段子编号
	 * @param user_id 用户编号
	 * @return 是否有数据，1-是，0-否
	 */
	public int getGoodEpisode(String episode_id, String user_id);
	
	/**
	 * 通过段子编号和用户编号查询收藏表中是否有数据
	 * @param episode_id 段子编号
	 * @param user_id 用户编号
	 * @return 是否有数据，1-是，0-否
	 */
	public int getCollect(String episode_id, String user_id);
	
	/**
	 * 增加指定用户id和段子id的收藏信息
	 * @param user_id 用户编号
	 * @param episode_id 段子编号
	 */
	public void insertCollectEpisode(String user_id, String episode_id);
	
	/**
	 * 删除指定用户id和段子id的收藏信息
	 * @param user_id 用户编号
	 * @param episode_id 段子编号
	 */
	public void deleteCollectEpisode(String user_id, String episode_id);
	
	/**
	 * 删除指定时间前的段子
	 * @param add_time
	 */
	public void deleteEpisode(String add_time);
	
	/**
	 * 添加段子
	 * @param episode_content 段子内容
	 */
	public void insertEpisode(String episode_content);
}
