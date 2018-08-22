package com.hdh.lifeup.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * UserAuthDO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/22
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("`user_auth`")
public class UserAuthDO extends BaseDO {
    private static final long serialVersionUID = 129185751196807386L;

    @TableId
    private Long authId;

    /** 关联userInfo */
    private Long userId;

    /** 授权验证的类型，比如qq、yb、phone */
    private String authType;

    /** '第三方应用或者本站平台的唯一标识，比如手机号，微信openid' */
    private String authIdentifier;

    /** 'authType是手机号的话对应的是平台用户的密码' */
    private String accessToken;
}
