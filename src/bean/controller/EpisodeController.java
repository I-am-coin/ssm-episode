package bean.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.ibatis.annotations.Param;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import bean.pojo.Episode;
import bean.service.EpisodeService;

@Controller@RequestMapping("/episode")
public class EpisodeController {
	@Resource(name="episodeServiceImpl")
	private EpisodeService episodeService;
	
	/**
	 * 获取所有段子信息
	 * @return 段子信息
	 */
	@ResponseBody
	@RequestMapping(value="/getEpisodes_ajax", produces="application/json;charset=UTF-8")
	public Map<String, Object> getEpisodes_ajax(@RequestParam("page_num") int page_num) {// 通过ID获取用户
		Map<String, Object> data = new HashMap<String, Object>();
		PageHelper.startPage(page_num, 10, true);
		List<Episode> list_episodes = episodeService.getEpisodes();
		PageInfo<Episode> page_episodes = new PageInfo<Episode>(list_episodes);
		data.put("data", page_episodes);
		return data;
	}
	
	/**
	 * 获取指定id的段子信息
	 * @param episode_id 段子id
	 * @return 段子信息
	 */
	@RequestMapping("/getEpisodeById")
	public ModelAndView getEpisodeById(@Param("episode_id") String episode_id) {// 通过ID获取用户
		ModelAndView model = new ModelAndView("/content");
		
		model.addObject("episode", episodeService.getEpisodeById(episode_id));
		return model;
	}
	
	/**
	 * 通过用户ID获取收藏段子信息，并分页
	 * @param page_num 页号
	 * @param user_id 用户ID
	 * @return 分页后的段子信息
	 */
	@ResponseBody
	@RequestMapping(value="/getEpisodeByUserId_ajax", produces="application/json;charset=UTF-8")
	public Map<String, Object> getEpisodeByUserId_ajax(@RequestParam("page_num") int page_num, @RequestParam("user_id") String user_id) {// 通过ID获取用户
		Map<String, Object> data = new HashMap<String, Object>();
		PageHelper.startPage(page_num, 10, true);
		List<Episode> list_episodes = episodeService.getEpisodeByUserId(user_id);
		PageInfo<Episode> page_episodes = new PageInfo<Episode>(list_episodes);
		data.put("page_episodes", page_episodes);
		
		return data;
	}
	
	/**
	 * 点赞段子操作异步处理
	 * @param user_id 用户id
	 * @param episode_id 段子id
	 * @return 是否操作成功 1-是，0-否
	 */
	@ResponseBody
	@RequestMapping(value="/addEpisodeGood_ajax", produces="application/json;charset=UTF-8")
	public Map<String, Object> addEpisodeGood_ajax(@RequestParam("user_id") String user_id, @RequestParam("episode_id") String episode_id) {
		Map<String, Object> data = new HashMap<String, Object>();
		int flag = episodeService.addEpisodeGood(user_id, episode_id);
		data.put("flag", flag);
		
		return data;
	}
	
	/**
	 * 获取点赞段子及收藏数据
	 * @param episode_id 段子编号
	 * @param user_id 用户编号
	 * @return 是否有数据
	 */
	@ResponseBody
	@RequestMapping(value="/getGoodEpisodeAndCollect_ajax", produces="application/json;charset=UTF-8")
	public Map<String, Object> getGoodEpisodeAndCollect_ajax(@RequestParam("episode_id") String episode_id, @RequestParam("user_id") String user_id) {
		Map<String, Object> data = new HashMap<String, Object>();
		int good = episodeService.getGoodEpisode(episode_id, user_id);
		int collect = episodeService.getCollect(episode_id, user_id);
		
		data.put("good", good);
		data.put("collect", collect);
		
		return data;
	}
	
	/**
	 * 增加指定用户id和段子id的收藏信息
	 * @param user_id 用户编号
	 * @param episode_id 段子编号
	 */
	@ResponseBody
	@RequestMapping(value="/addCollectEpisode_ajax", produces="application/json;charset=UTF-8")
	public void addCollectEpisode_ajax(@RequestParam("user_id") String user_id, @RequestParam("episode_id") String episode_id) {
		episodeService.insertCollectEpisode(user_id, episode_id);
	}
	
	/**
	 * 删除指定用户id和段子id的收藏信息
	 * @param user_id 用户编号
	 * @param episode_id 段子编号
	 */
	@ResponseBody
	@RequestMapping(value="/removeCollectEpisode_ajax", produces="application/json;charset=UTF-8")
	public void removeCollectEpisode_ajax(@RequestParam("user_id") String user_id, @RequestParam("episode_id") String episode_id) {
		episodeService.deleteCollectEpisode(user_id, episode_id);
	}
	
	/**
	 * 时间任务，指定时间（每天0:10）重复执行
	 * 删除7天以前的段子
	 */
	@Scheduled(cron="0 10 0 * * ?")
	public void deleteEpisode() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) - 7);//当前时间减去7天
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String time = sdf.format(calendar.getTime());
		System.out.println("删除段子");
		episodeService.deleteEpisode(time);
		System.out.println("执行完成");
	}
	
	/**
	 * 时间任务，指定时间（每天0:20）重复执行
	 * 执行Python脚本爬取段子保存到文件
	 */
	@Scheduled(cron="0 20 0 * * ?")
	public void execSpider () {
		System.out.println("执行Python脚本...");
		//服务器上的绝对地址
		String serverPath = this.getClass().getClassLoader().getResource("").getPath().substring(1);
		String pythonFilePath = "bean/python/spider_main.py";
		System.out.println("python脚本地址:"+serverPath+pythonFilePath);
		String[] cmd = new String[2];
		cmd[0] = "python";
		cmd[1] = serverPath + pythonFilePath;
		Runtime rt = Runtime.getRuntime();
		Process pr;
		BufferedReader br1 = null;
		BufferedReader br2 = null;
		try {
			//cmd执行Python脚本
			pr = rt.exec(cmd);
			//Charset.forName("GBK")解决中文乱码问题
			br1 = new BufferedReader(new InputStreamReader(pr.getInputStream(), Charset.forName("GBK")));
			//输出正常执行信息
			String line = "";
			while((line = br1.readLine()) != null) {
			System.out.println(line);
			}
			//错误信息流
			br2 = new BufferedReader(new InputStreamReader(pr.getErrorStream(), Charset.forName("GBK")));
			//输出错误执行信息
			String error = "";
			while((error = br2.readLine()) != null) {
			System.out.println(error);
			}
		} catch (IOException e) {
			System.out.println("执行Python脚本出错");
			e.printStackTrace();
		} finally {//关闭数据流
			try {
				if (br1 != null) {
					br1.close();
				}
				if (br2 != null) {
					br2.close();
				}
			} catch (IOException e) {
					System.out.println("关闭数据流出错");
					e.printStackTrace();
			}
		}
		System.out.println("脚本执行完成！");
	}
	
	/**
	 * 时间任务，指定时间（每天0:30）重复执行
	 * 读取文件保存到数据库
	 */
	@Scheduled(cron="0 30 0 * * ?")
	public void addEpisode() {
		//获取当天日期
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String fileName = "E:\\Python\\Data\\episode\\" + sdf.format(new Date()) + ".dat";//文件名
		BufferedReader br = null;
		System.out.println("读取文件...("+fileName+")");
		try {
			//获取输入流，Charset.forName("gbk")解决中文乱码问题
			br = new BufferedReader(new InputStreamReader(new FileInputStream(new File(fileName)), Charset.forName("gbk")));
			//读取数据，存入数据库
			System.out.println("保存数据中...");
			String line = "";
			while ((line=br.readLine()) != null) {
				episodeService.insertEpisode(line);
			}
			System.out.println("保存完成！");
		} catch (FileNotFoundException e) {
			System.out.println("文件打开异常");
			e.printStackTrace();
		} catch (IOException e) {
			System.out.println("文件读取异常");
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					System.out.println("文件关闭异常");
					e.printStackTrace();
				}
			}
		}
	}
}
