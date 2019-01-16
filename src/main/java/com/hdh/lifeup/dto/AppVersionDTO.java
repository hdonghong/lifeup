package com.hdh.lifeup.dto;

import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.domain.AppVersionDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * AppVersionDTO class<br/>
 *
 * @author hdonghong
 * @since 2019/01/16
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class AppVersionDTO extends BaseDTO<AppVersionDO> {

    private static final long serialVersionUID = -2424390811933753913L;

    private Long versionId;

    private Integer newVersion;

    private String versionName;

    private String versionDesc;

    private String downloadUrl;

}
