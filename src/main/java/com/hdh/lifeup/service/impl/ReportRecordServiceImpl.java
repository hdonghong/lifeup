package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Preconditions;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.dao.MarketGoodsMapper;
import com.hdh.lifeup.dao.TeamMemberRecordMapper;
import com.hdh.lifeup.dao.TeamTaskMapper;
import com.hdh.lifeup.model.constant.BizTypeConst;
import com.hdh.lifeup.model.domain.ReportRecordDO;
import com.hdh.lifeup.model.dto.ReportRecordDTO;
import com.hdh.lifeup.dao.ReportRecordMapper;
import com.hdh.lifeup.model.enums.CodeLevelEnum;
import com.hdh.lifeup.model.vo.ReportResultVO;
import com.hdh.lifeup.service.ReportRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDateTime;

/**
 * ReportRecordServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/10/21
 */
@Service
public class ReportRecordServiceImpl implements ReportRecordService {

    @Autowired
    private ReportRecordMapper reportRecordMapper;
    @Autowired
    private TeamTaskMapper teamTaskMapper;
    @Autowired
    private TeamMemberRecordMapper teamMemberRecordMapper;
    @Autowired
    private MarketGoodsMapper marketGoodsMapper;

    /**
     * 用户举报人数阈值
     */
    private static final Integer USER_REPORT_LIMIT_COUNT = 10;

    @Override
    public ReportResultVO insert(ReportRecordDTO reportRecordDTO) {
        Preconditions.checkNotNull(reportRecordDTO, "【新增举报记录】reportRecordDTO不能为空");
        reportRecordDTO.setReportUserId(UserContext.get().getUserId());
        reportRecordMapper.insert(reportRecordDTO.toDO(ReportRecordDO.class));
        boolean publishResult = doPublishment(reportRecordDTO);
        ReportResultVO reportResultVO = new ReportResultVO().setIsDel(publishResult);
        return reportResultVO;
    }

    /**
     * 执行惩罚措施
     */
    private boolean doPublishment(ReportRecordDTO reportRecordDTO) {
        // 检查对同一项的用户举报人数在7天内是否超过阈值
        QueryWrapper<ReportRecordDO> countWrapper = new QueryWrapper<ReportRecordDO>()
            .select("distinct report_user_id")
            .eq("item_id", reportRecordDTO.getItemId())
            .gt("create_time", LocalDateTime.now().minusDays(7L));
        Integer reportCount = reportRecordMapper.selectCount(countWrapper);
        Integer userType = UserContext.get().getUserType();
        if (reportCount < USER_REPORT_LIMIT_COUNT &&
            !CodeLevelEnum.ADMIN.getLevel().equals(userType)) {
            return false;
        }

        String reportItem = reportRecordDTO.getReportItem();
        // 团队
        if (BizTypeConst.TEAM_TASK.equals(reportItem)) {
            teamTaskMapper.deleteById(reportRecordDTO.getItemId());
            return true;
        }

        // 动态
        if (BizTypeConst.TEAM_MEMBER_RECORD.equals(reportItem)) {
            teamMemberRecordMapper.deleteById(reportRecordDTO.getItemId());
            return true;
        }

        // 商品
        if (BizTypeConst.MARKET_GOODS.equals(reportItem)) {
            marketGoodsMapper.deleteById(reportRecordDTO.getItemId());
            return true;
        }

        return false;
    }
}
