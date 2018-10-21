package com.hdh.lifeup.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.domain.UserInfoDO;
import com.hdh.lifeup.util.JsonUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import org.springframework.beans.BeanUtils;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

/**
 * UserInfoDTO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserInfoDTO extends BaseDTO<UserInfoDO> {

    private static final long serialVersionUID = 3900595581562692523L;

    private Long userId;

    /** '用户昵称' */
    private String nickname;

    /** '0女，1男，2保密' */
    private Integer userSex;

    /**'用户地区'  */
    private String userAddress;

    /** 用户头像 */
    private String userHead;

    /** '-1已删除，0未激活，1正常' */
    private Integer userStatus;

    /** '注册时间' */
    private Instant createTime;

    /** 当有绑定手机时，手机号 */
    private String phone;

    /** 绑定的类型 */
    private List<String> authTypes;

    private String pwdSalt;

    public static UserInfoDTO fromYbUser(JsonNode userInfoJson) {
        Preconditions.checkNotNull(userInfoJson, "userInfoJson can not be empty!");
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        userInfoDTO.setNickname(userInfoJson.get("yb_username").asText())
                .setUserSex("M".equals(userInfoJson.get("yb_sex").asText()) ? 1 : 0)
                .setUserHead(userInfoJson.get("yb_userhead").asText())
                .setUserAddress(userInfoJson.get("yb_schoolname").asText());
        return userInfoDTO;
    }

    @Override
    public UserInfoDO toDO(Class<UserInfoDO> doClass) {
        try {
            UserInfoDO userInfoDO = doClass.newInstance();
            BeanUtils.copyProperties(this, userInfoDO, "authTypes", "password");
            if (this.authTypes != null && this.authTypes.size() > 0) {
                userInfoDO.setAuthTypes(JsonUtil.toJson(this.authTypes));
            }
            return userInfoDO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <DTO extends BaseDTO> DTO from(UserInfoDO aDO) {
        UserInfoDTO userInfoDTO = new UserInfoDTO();
        BeanUtils.copyProperties(aDO, userInfoDTO, "authTypes");
        userInfoDTO.setAuthTypes(JsonUtil.jsonToList(aDO.getAuthTypes(), String.class));
        return (DTO) userInfoDTO;
    }
}
