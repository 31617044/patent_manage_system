/**
 * 
 */
package com.web.project.controller;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.ibatis.annotations.Param;
import org.omg.CORBA.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import net.sf.json.JSONArray;

import com.web.project.model.maker.MakerCommonWorks;
import com.web.project.model.maker.MakerCooperation;
import com.web.project.model.maker.MakerProject;
import com.web.project.model.maker.MakerInfo;
import com.web.project.service.FieldService;
import com.web.project.service.makerService.MakerProjectService;
import com.web.project.vo.MakerProjectListVo;
import com.web.project.model.Field;
import com.web.project.model.Question;
import com.web.project.model.maker.MakerWorks;
import com.web.project.service.maker.MakerCommonWorksService;
import com.web.project.service.maker.MakerCooperationService;
import com.web.project.service.maker.MakerInfoService;
import com.web.project.service.maker.MakerWorksService;
import com.web.project.vo.MakerCommonWorksVo;
import com.web.project.vo.MakerCooperationVo;
import com.web.project.vo.MakerWorkVo;
import com.web.project.vo.QuestionVo;
import com.web.project.vo.MakerProjectVo;
import com.web.project.vo.MakerProjectWorkVo;
/**
 * @author 子晨
 *
 */
@RequestMapping("maker")
@Controller
public class MakerController {

	@Autowired
	MakerWorksService makerWorksService;
	@Autowired
	MakerProjectService makerProjectService;
	@Autowired
	MakerCooperationService makerCooperationService;
	@Autowired
	MakerCommonWorksService makerCommonWorksService;
	@Autowired
	HttpServletRequest request;
	@Autowired
	MakerInfoService makerInfoService;
	@Autowired
	FieldService fieldService;
	
	/**
	 * 获取创客信息并跳转信息维护界面
	 */
	@RequestMapping("personalInfo")
	public String getPersonalInfo(@RequestParam(value = "id") final int id,
			ModelMap model){
		MakerInfo makerInfo=makerInfoService.getMakerInfoById(id);
		ArrayList<Field> field=fieldService.getField();
		model.put("detail", makerInfo);
		model.put("fieldList", field);
		return "maker/personalinfo";
	}
	
	/**
	 * 修改创客个人信息
	 */
	@RequestMapping("personalInfoupdate")
	public String updatePersonalInfo(
			@RequestParam("makerid") int id,
			@RequestParam("team") String team,
			@RequestParam("contact") String contact,
			@RequestParam("phone") String phone,
			@RequestParam("email") String email,
			@RequestParam("address") String address,
			@RequestParam("field1") String field1,
			@RequestParam("field2") String field2,
			@RequestParam("teamIntro") String teamIntro,
			@RequestParam("achievement") String achievement
			)throws UnsupportedEncodingException{
		team = new String(team.getBytes("iso-8859-1"), "utf-8");
		contact = new String(contact.getBytes("iso-8859-1"), "utf-8");
		phone = new String(phone.getBytes("iso-8859-1"), "utf-8");
		email = new String(email.getBytes("iso-8859-1"), "utf-8");
		address = new String(address.getBytes("iso-8859-1"), "utf-8");
		field1 = new String(field1.getBytes("iso-8859-1"), "utf-8");
		field2 = new String(field2.getBytes("iso-8859-1"), "utf-8");
		teamIntro = new String(teamIntro.getBytes("iso-8859-1"), "utf-8");
		achievement = new String(achievement.getBytes("iso-8859-1"), "utf-8");

		MakerInfo makerInfo=new MakerInfo();
		makerInfo.setId(id);
		makerInfo.setTeam(team);
		makerInfo.setContact(contact);
		makerInfo.setPhone(phone);
		makerInfo.setEmail(email);
		makerInfo.setAddress(address);
		makerInfo.setField1(field1);
		makerInfo.setField2(field2);
		makerInfo.setTeamIntro(teamIntro);
		makerInfo.setAchievement(achievement);
		makerInfoService.updateMakerInfoById(makerInfo);
		return "maker/loging";
	}
	
	/**
	 * 获取创客项目列表
	 */
	@RequestMapping("makerProjectList")
	@ResponseBody
	public String getProjectList(
			@RequestParam(value = "pageNum") final int pageId,
			@RequestParam(value = "pageSize") final int pageSize) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Long current = System.currentTimeMillis();
		String currentTime=sdf.format(current);
		String [] currentList=currentTime.substring(0,10).split("-");
		String currentDate=currentList[0]+currentList[1]+currentList[2];
		int currentIdate=Integer.parseInt(currentDate);
		int num=0;
		
		ArrayList<MakerProject> makerProjectList = makerProjectService.getProjectList();
		ArrayList<MakerProject> availableList = new ArrayList<MakerProject>();
		ArrayList<MakerProjectListVo> viewList = new ArrayList<MakerProjectListVo>();
		
		for (int i = 0; i < makerProjectList.size(); i++) {
			MakerProject makerProject = makerProjectList.get(i);
			String startTime=makerProject.getStartTime();
			String [] startList=startTime.substring(0,10).split("-");
			String startDate=startList[0]+startList[1]+startList[2];
			int startIdate=Integer.parseInt(startDate);
			String endTime=makerProject.getEndTime();
			String [] endList=endTime.substring(0,10).split("-");
			String endDate=endList[0]+endList[1]+endList[2];
			int endIdate=Integer.parseInt(endDate);			
			if(startIdate<=currentIdate&&endIdate>=currentIdate){
				availableList.add(makerProject);
				num++;
			}
		}
		int start = (pageId - 1) * pageSize;
		int end = Math.min(availableList.size(), start + pageSize);
		for (int i = start; i < end; i++) {
			MakerProject makerProject = availableList.get(i);
			String startTime=makerProject.getStartTime();
			String [] startList=startTime.substring(0,10).split("-");
			String startDate=startList[0]+startList[1]+startList[2];
			int startIdate=Integer.parseInt(startDate);
			String endTime=makerProject.getEndTime();
			String [] endList=endTime.substring(0,10).split("-");
			String endDate=endList[0]+endList[1]+endList[2];
			int endIdate=Integer.parseInt(endDate);			
			if(startIdate<=currentIdate&&endIdate>=currentIdate){
				MakerProjectListVo view = new MakerProjectListVo();
				view.setId(makerProject.getId());
				view.setTitle(makerProject.getTitle());
				view.setStartTime(startTime.split(" ")[0]);
				view.setEndTime(endTime.split(" ")[0]);
				viewList.add(view);
			}
		}
		HashMap hashMap = new HashMap();
		hashMap.put("total", num);
		hashMap.put("rows", viewList);
		String result1 = JSONArray.fromObject(hashMap).toString();
		String result = result1.substring(1, result1.length() - 1);
		return result;
	}
	
	/**
	 * 获取创客合作列表
	 */
	@RequestMapping("makerCooperationList")
	@ResponseBody
	public String getCooperationList(
			@RequestParam(value = "pageNum") final int pageId,
			@RequestParam(value = "pageSize") final int pageSize,
			@RequestParam(value = "pageSort") final String pageSort,
			@RequestParam(value = "pageOrder") final String pageOrder) {
		
		ArrayList<MakerCooperation> makerCooperationList = makerCooperationService.getCooperationList(pageSort, pageOrder);
		ArrayList<MakerCooperationVo> viewList = new ArrayList<MakerCooperationVo>();
		int start = (pageId - 1) * pageSize;
		int end = Math.min(makerCooperationList.size(), start + pageSize);		
		for (int i = start; i < end; i++) {
			MakerCooperation makerCooperation = makerCooperationList.get(i);
			MakerCooperationVo makerCooperationVo=new MakerCooperationVo();
			makerCooperationVo.transfer(makerCooperation);
			viewList.add(makerCooperationVo);
		}
		HashMap hashMap = new HashMap();
		hashMap.put("total", makerCooperationList.size());
		hashMap.put("rows", viewList);
		String result1 = JSONArray.fromObject(hashMap).toString();
		String result = result1.substring(1, result1.length() - 1);
		return result;
	}
	
	/**
	 * 获取创客合作详细信息
	 */
	@RequestMapping("makerCooperationDetail")
	public String getCooperationDetail(@RequestParam(value = "id") final int id,ModelMap model){
		MakerCooperation makerCooperation=makerCooperationService.getCooperationById(id);
		MakerCooperationVo makerCooperationVo=new MakerCooperationVo();
		makerCooperationVo.transfer(makerCooperation);
		model.put("detail", makerCooperationVo);
		return "maker/cooperationview";
	}
	
	/**
	 * 获取创客个人合作详细信息
	 */
	@RequestMapping("myCooperationDetail")
	public String getMyCooperationDetail(@RequestParam(value = "id") final int id,ModelMap model){
		MakerCooperation makerCooperation=makerCooperationService.getCooperationById(id);
		MakerCooperationVo makerCooperationVo=new MakerCooperationVo();
		makerCooperationVo.transfer(makerCooperation);
		model.put("detail", makerCooperationVo);
		return "maker/mycooperationview";
	}
	/**
	 * 获取创客个人合作列表
	 */
	@RequestMapping("myCooperationList")
	@ResponseBody
	public String getMyCooperationList(
			@RequestParam(value = "pageNum") final int pageId,
			@RequestParam(value = "pageSize") final int pageSize,
			@RequestParam(value = "userId") final int id) {		
		ArrayList<MakerCooperation> makerCooperationList = makerCooperationService.getCooperationListByUser(id);
		ArrayList<MakerCooperationVo> viewList = new ArrayList<MakerCooperationVo>();
		int start = (pageId - 1) * pageSize;
		int end = Math.min(makerCooperationList.size(), start + pageSize);
		
		for (int i = start; i < end; i++) {
			MakerCooperation makerCooperation = makerCooperationList.get(i);
			MakerCooperationVo makerCooperationVo=new MakerCooperationVo();
			makerCooperationVo.transfer(makerCooperation);
			viewList.add(makerCooperationVo);
		}
		HashMap hashMap = new HashMap();
		hashMap.put("total", makerCooperationList.size());
		hashMap.put("rows", viewList);
		String result1 = JSONArray.fromObject(hashMap).toString();
		String result = result1.substring(1, result1.length() - 1);
		return result;
	}
	
	/**
	 * 修改创客合作需求
	 */
	@RequestMapping("updateMyCooperation")
	public String updateMyCooperation(
			@RequestParam("cid") int cid,
			@RequestParam("contact") String contact,
			@RequestParam("phone") String phone,
	        @RequestParam("teamIntro") String teamIntro,
	        @RequestParam("description") String description
			)throws UnsupportedEncodingException{
		contact = new String(contact.getBytes("iso-8859-1"), "utf-8");
		phone = new String(phone.getBytes("iso-8859-1"), "utf-8");
		teamIntro = new String(teamIntro.getBytes("iso-8859-1"), "utf-8");
		description = new String(description.getBytes("iso-8859-1"), "utf-8");
		MakerCooperation makerCooperation=new MakerCooperation();
		makerCooperation.setId(cid);
		makerCooperation.setContact(contact);
		makerCooperation.setPhone(phone);
		makerCooperation.setTeamIntro(teamIntro);
		makerCooperation.setDetail(description);
		makerCooperationService.updateCooperationById(makerCooperation);
		return "maker/mycooperationoverview";
	}
	
	/**
	 * 进入合作添加界面
	 */
	@RequestMapping("inputMyCooperation")
	public String inputMyCooperation(
			@RequestParam("userid") int id,
			ModelMap model
			){
		MakerInfo makerInfo=makerInfoService.getMakerInfoById(id);
		ArrayList<Field> field=fieldService.getField();
		model.put("detail", makerInfo);
		model.put("fieldList", field);
		return "maker/cooperation";
	}
	
	/**
	 * 添加创客合作需求
	 */
	@RequestMapping("addMyCooperation")
	public String addMyCooperation(
			@RequestParam("userid") int id,
			@RequestParam("title") String title,
			@RequestParam("field") String field,
			@RequestParam("contact") String contact,
			@RequestParam("phone") String phone,
			@RequestParam("teamIntro") String teamIntro,
			@RequestParam("description") String description			
			)throws UnsupportedEncodingException{
		contact = new String(contact.getBytes("iso-8859-1"), "utf-8");
		phone = new String(phone.getBytes("iso-8859-1"), "utf-8");
		teamIntro = new String(teamIntro.getBytes("iso-8859-1"), "utf-8");
		description = new String(description.getBytes("iso-8859-1"), "utf-8");
		title = new String(title.getBytes("iso-8859-1"), "utf-8");
		field = new String(field.getBytes("iso-8859-1"), "utf-8");
		MakerCooperation makerCooperation=new MakerCooperation();
		MakerInfo makerInfo = makerInfoService.getMakerInfoById(id);
		makerCooperation.setContact(contact);
		makerCooperation.setDetail(description);
		makerCooperation.setField(field);
		makerCooperation.setMakerId(id);
		makerCooperation.setPhone(phone);
		makerCooperation.setTeam(makerInfo.getTeam());
		makerCooperation.setTeamIntro(teamIntro);
		makerCooperation.setTitle(title);
		Long time = System.currentTimeMillis()/1000;
		makerCooperation.setSubTime(time);
		makerCooperationService.insertCooperation(makerCooperation);
		return "maker/mycooperationoverview";
	}
	/**
	 * 删除创客个人合作列表
	 */
	@RequestMapping("deleteMyCooperation")
	@ResponseBody
	public void getMyCooperationList(
			@RequestParam(value = "remove") final int[] id) {
		ArrayList<Integer> remove=new ArrayList<Integer>();
		for(int i=0;i<id.length;i++){
			remove.add(id[i]);
		}
		makerCooperationService.deleteCooperationById(remove);
	}
	/**
	 * 创客项目详细信息
	 */
	@RequestMapping("makerProjectDetail")
	public String getProjectDetail(@RequestParam(value="projectId")final int id,ModelMap model){
		MakerProject makerProject = makerProjectService.getProjectById(id);
		MakerProjectVo makerProjectVo = new MakerProjectVo();
		makerProjectVo.transfer(makerProject);
		model.put("detail", makerProjectVo);
		return "maker/mproject";
	}
	
	/**
	 * 获取创客个人原创作品列表
	 */
	@RequestMapping("myCommonWorksList")
	@ResponseBody
	public String getMakerCommonWorkListByUser(@RequestParam(value = "pageNum") final int pageId,
			@RequestParam(value = "userId") final int id,
			@RequestParam(value = "pageSize") final int pageSize,ModelMap model){
		ArrayList<MakerCommonWorks> makerWorks = makerCommonWorksService.getMakerCommonWorksListByUser(id);
		int start = (pageId - 1) * pageSize;
		int end = Math.min(makerWorks.size(), start + pageSize);
		ArrayList<MakerCommonWorksVo> makerWorkVos = new ArrayList<MakerCommonWorksVo>();
		for (int i = start; i < end; i++) {
			MakerCommonWorksVo makerWorkVo = new MakerCommonWorksVo();
			makerWorkVo.transfer(makerWorks.get(i));
			makerWorkVos.add(makerWorkVo);
		}
		HashMap hashMap = new HashMap();
		hashMap.put("total", makerWorks.size());
		hashMap.put("rows", makerWorkVos);
		String result1 = JSONArray.fromObject(hashMap).toString();
		String result = result1.substring(1, result1.length() - 1);
		return result;
	}	
	
	/**
	 * 获取创客个人项目作品列表
	 */
	@RequestMapping("myProjectWorksList")
	@ResponseBody
	public String getMakerWorkListByUser(@RequestParam(value = "pageNum") final int pageId,
			@RequestParam(value = "userId") final int id,
			@RequestParam(value = "pageSize") final int pageSize,ModelMap model){
		ArrayList<MakerWorks> makerWorks = makerWorksService.getMakerWorksListByUser(id);
		int start = (pageId - 1) * pageSize;
		int end = Math.min(makerWorks.size(), start + pageSize);
		ArrayList<MakerProjectWorkVo> makerWorkVos = new ArrayList<MakerProjectWorkVo>();
		for (int i = start; i < end; i++) {
			MakerProjectWorkVo makerWorkVo = new MakerProjectWorkVo();
			makerWorkVo.transfer(makerWorks.get(i));
			MakerProject makerProject=makerProjectService.getProjectById(makerWorkVo.getMakerProjectID());
			makerWorkVo.setProject(makerProject.getTitle());
			makerWorkVos.add(makerWorkVo);
		}
		HashMap hashMap = new HashMap();
		hashMap.put("total", makerWorks.size());
		hashMap.put("rows", makerWorkVos);
		String result1 = JSONArray.fromObject(hashMap).toString();
		String result = result1.substring(1, result1.length() - 1);
		return result;
	}	
	
	/**
	 * 获取创客个人项目作品信息
	 */
	@RequestMapping("myProjectWorkDetail")
	public String getMyProjectWorkDetail(@RequestParam(value = "id") final int id,
			ModelMap model){
		MakerWorks makerWork = makerWorksService.getMakerWorksDetailById(id);
		MakerProjectWorkVo makerWorkVo = new MakerProjectWorkVo();
		makerWorkVo.transfer(makerWork);
		MakerProject makerProject=makerProjectService.getProjectById(makerWorkVo.getMakerProjectID());
		makerWorkVo.setProject(makerProject.getTitle());
		model.put("detail", makerWorkVo);
		return "maker/mprojectworkview";
	}
	
	/**
	 * 获取创客个人原创作品信息
	 */
	@RequestMapping("myCommonWorkDetail")
	public String getMyCommonWorkDetail(@RequestParam(value = "id") final int id,
			ModelMap model){
		MakerCommonWorks makerCommonWorks=makerCommonWorksService.getMakerCommonWorksDetailById(id);
		MakerCommonWorksVo makerCommonWorksVo=new MakerCommonWorksVo();
		makerCommonWorksVo.transfer(makerCommonWorks);
		model.put("detail", makerCommonWorksVo);
		return "maker/mworkview";
	}
	
	/**
	 * 创客项目作品列表
	 */
	@RequestMapping("workList")
	@ResponseBody
	public String getMakerWorkList(@RequestParam(value = "pageNum") final int pageId,
			@RequestParam(value = "pageSize") final int pageSize,ModelMap model){
		ArrayList<MakerWorks> makerWorks = makerWorksService.getMakerWorksList();
		int start = (pageId - 1) * pageSize;
		int end = Math.min(makerWorks.size(), start + pageSize);
		ArrayList<MakerWorkVo> makerWorkVos = new ArrayList<MakerWorkVo>();
		for (int i = start; i < end; i++) {
			MakerWorkVo makerWorkVo = new MakerWorkVo();
			makerWorkVo.transfer(makerWorks.get(i));
			makerWorkVos.add(makerWorkVo);
		}
		HashMap hashMap = new HashMap();
		hashMap.put("total", makerWorks.size());
		hashMap.put("rows", makerWorkVos);
		String result1 = JSONArray.fromObject(hashMap).toString();
		String result = result1.substring(1, result1.length() - 1);
		return result;
	}	
	
	/**
	 * 跳转到项目作品列表
	 */
	@RequestMapping("toMakerWorkList")
	public String toWorkList(){
		return "expert/pmakeroverview";
	}
	
	/**
	 * 创客项目作品详细信息
	 */
	@RequestMapping("makerWorkDetail")
	public String getWorkDetail(@RequestParam(value="id")final int id,ModelMap model){
		MakerWorks makerWork = makerWorksService.getMakerWorksDetailById(id);
		MakerWorkVo makerWorkVo = new MakerWorkVo();
		makerWorkVo.transfer(makerWork);
		model.put("detail", makerWorkVo);
		return "expert/pmakerreview";
	}
	
	/**
	 * 专家评价创客项目
	 * @throws UnsupportedEncodingException 
	 */
	@RequestMapping("evaluate")
	public String evaluate(@RequestParam(value="presention")String evaluation,
			@RequestParam(value="advice")String problems,
			@RequestParam(value="sort")String flag,
			@RequestParam(value="id")int id,ModelMap model) throws UnsupportedEncodingException{
        problems = new String(problems.getBytes("iso-8859-1"), "utf-8");
        evaluation = new String(evaluation.getBytes("iso-8859-1"), "utf-8");
		Long time = System.currentTimeMillis()/1000;
		if(flag.equals("save"))
		   makerWorksService.updateMakerWorkInfo(problems, evaluation, time, false, id);
		else {
			makerWorksService.updateMakerWorkInfo(problems, evaluation, time, true, id);
		}
		return "expert/pmakeroverview";
    }
	
	/**
	 * 创客新闻选择
	 */
	@RequestMapping("loginNewsList")
	public String loginNewsList(
			@RequestParam(value="type")int type,ModelMap model){
		
		if(type==1)
		{
			request.setAttribute("type", "国家科技政策");
		}
		if(type==2)
		{
			request.setAttribute("type", "政府培训通知");
		}
		if(type==3)
		{
			request.setAttribute("type", "银行资金援助项目");
		}
		if(type==4)
		{
			request.setAttribute("type", "系统公告");
		}
		return "maker/newsoverview";
    }
}