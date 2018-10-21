package com.hdh.lifeup.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * ReportRecordDO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/25
 */
@TableName("`report_record`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class ReportRecordDO extends BaseDO {

    private static final long serialVersionUID = -2424390811933753913L;

    @TableId
    private Long reportId;

    private Long reportTypeId;

    /** 举报人的id */
    private Long reportUserId;

    /** 举报项，比如team、user、activity等等 */
    private String reportItem;

    /** 对应的举报项的id */
    private Long itemId;

    /** 被举报的人的id */
    private Long criminalUserId;

    /** '0存在；1删除' */
    @TableLogic
    private Integer isDel;
}
