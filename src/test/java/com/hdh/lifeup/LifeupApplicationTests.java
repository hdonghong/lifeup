package com.hdh.lifeup;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdh.lifeup.dao.TeamMemberRecordMapper;
import com.hdh.lifeup.dao.UserInfoMapper;
import com.hdh.lifeup.model.domain.TeamMemberRecordDO;
import com.hdh.lifeup.model.domain.UserInfoDO;
import com.hdh.lifeup.model.dto.TeamMemberRecordDTO;
import com.hdh.lifeup.util.JsonUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.stream.Collectors;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LifeupApplicationTests {

	@Autowired
	private TeamMemberRecordMapper teamMemberRecordMapper;

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Test
	public void contextLoads() {
	}

	@Test
	public void updateImage() {
		QueryWrapper<TeamMemberRecordDO> queryWrapper = new QueryWrapper<TeamMemberRecordDO>()
				.isNotNull("activity_images");
		List<TeamMemberRecordDO> recordDOList = teamMemberRecordMapper.selectList(queryWrapper);
		recordDOList.forEach(record -> {
			TeamMemberRecordDTO recordDTO = TeamMemberRecordDTO.from(record, TeamMemberRecordDTO.class);
			List<String> activityImages = recordDTO.getActivityImages();
			activityImages = activityImages.stream().map(image -> image + "-webp").collect(Collectors.toList());
			TeamMemberRecordDO recordDO = new TeamMemberRecordDO();
			recordDO.setMemberRecordId(record.getMemberRecordId());
			recordDO.setActivityImages(JsonUtil.toJson(activityImages));
			teamMemberRecordMapper.updateById(recordDO);
		});
	}

	@Test
	public void updateHead() {
		QueryWrapper<UserInfoDO> queryWrapper = new QueryWrapper<UserInfoDO>()
				.isNotNull("user_head");
		List<UserInfoDO> userInfoDOList = userInfoMapper.selectList(queryWrapper);
		userInfoDOList.forEach(userInfoDO -> {
			if (!userInfoDO.getUserHead().contains("lifeup.hdonghong.top")) {
				return;
			}
			UserInfoDO userInfoDO1 = new UserInfoDO();
			userInfoDO1.setUserId(userInfoDO.getUserId());
			userInfoDO1.setUserHead(userInfoDO.getUserHead() + "-webp");
			userInfoMapper.updateById(userInfoDO1);
		});
	}
}
