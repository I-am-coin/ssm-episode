package bean.service.impl;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import bean.mapper.EpisodeMapper;
import bean.pojo.Episode;
import bean.service.EpisodeService;

@Service@Transactional
public class EpisodeServiceImpl implements EpisodeService {
	@Resource(name="episodeMapper")
	private EpisodeMapper episodeMapper;
	
	@Override
	public List<Episode> getEpisodes() {
		return episodeMapper.getEpisodes();
	}

	@Override
	public Episode getEpisodeById(String episode_id) {
		return episodeMapper.getEpisodeById(episode_id);
	}
	
	@Override
	public List<Episode> getEpisodeByUserId(String user_id) {
		return episodeMapper.getEpisodeByUserId(user_id);
	}

	@Override
	public int addEpisodeGood(String user_id, String episode_id) {
		int flag = 0;
		
		try {
			episodeMapper.addEpisodeGoodById(episode_id);//对段子表episode_good字段值+1
			episodeMapper.insertEpisode_Good(user_id, episode_id);//对段子点赞表新增行
			flag = 1;
		}
		catch (Exception e) {
			System.out.println("点赞发生异常ServiceImpl");
			e.printStackTrace();
		}
		return flag;
	}

	@Override
	public int getGoodEpisode(String episode_id, String user_id) {
		return episodeMapper.getGoodEpisode(episode_id, user_id);
	}

	@Override
	public int getCollect(String episode_id, String user_id) {
		return episodeMapper.getCollect(episode_id, user_id);
	}

	@Override
	public void insertCollectEpisode(String user_id, String episode_id) {
		episodeMapper.insertCollectEpisode(user_id, episode_id);
	}

	@Override
	public void deleteCollectEpisode(String user_id, String episode_id) {
		episodeMapper.deleteCollectEpisode(user_id, episode_id);
	}

	@Override
	public void deleteEpisode(String add_time) {
		episodeMapper.deleteEpisode(add_time);
	}

	@Override
	public void insertEpisode(String episode_content) {
		episodeMapper.insertEpisode(episode_content);
	}
}
