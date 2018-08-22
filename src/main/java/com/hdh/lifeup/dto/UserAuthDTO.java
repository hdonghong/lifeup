package com.hdh.lifeup.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.constant.AuthTypeConst;
import com.hdh.lifeup.domain.UserAuthDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;

/**
 * UserAuthDTO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/22
 */
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserAuthDTO extends BaseDTO<UserAuthDO> {

    private static final long serialVersionUID = -4251687015640921311L;

    private Long authId;

    /** 关联userInfo */
    private Long userId;

    /** 授权验证的类型，比如qq、yb、phone */
    @NotEmpty(message = "授权登陆的类型不能为空")
    private String authType;

    /** '第三方应用或者本站平台的唯一标识，比如手机号，微信openid' */
    @NotEmpty(message = "授权登陆的唯一标识不能为空")
    private String authIdentifier;

    /** 'authType是手机号的话对应的是平台用户的密码' */
    private String accessToken;

    public static UserAuthDTO fromYbUser(JsonNode userInfoJson) {
        Preconditions.checkNotNull(userInfoJson, "userInfoJson can not be empty!");
        UserAuthDTO userAuthDTO = new UserAuthDTO();
        userAuthDTO.setAuthIdentifier(userInfoJson.get("yb_userid").asText())
                .setAuthType(AuthTypeConst.YB);

        return userAuthDTO;
    }
}
