package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * AppVersionDO class<br/>
 *
 * @author hdonghong
 * @since 2019/01/16
 */
@TableName("`app_version`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AppVersionDO extends BaseDO {

    private static final long serialVersionUID = 6448445810954592958L;

    @TableId
    private Long versionId;

    private Integer newVersion;

    private String versionName;

    private String versionDesc;

    private Integer versionType;

    private String downloadUrl;

    /** '0存在；1删除' */
    @TableLogic
    private Integer isDel;
}
