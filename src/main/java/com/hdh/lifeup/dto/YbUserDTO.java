package com.hdh.lifeup.dto;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.base.Preconditions;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.enums.CodeMsgEnum;
import com.hdh.lifeup.exception.GlobalException;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * YbUserDTO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/21
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@Slf4j
public class YbUserDTO extends BaseDTO {

    private static final long serialVersionUID = 3429422782742257336L;

    /** 易班用户id */
    private Long ybUserId;

    /** 用户名 */
    private String ybUsername;

    /** 用户昵称 */
    private String ybUsernick;

    /** 性别 */
    private String ybSex;

    /** 持有网薪 */
    private Integer ybMoney;

    /** 经验值 */
    private String ybExp;

    /** 用户头像 */
    private String ybUserhead;

    /** 所在学校id */
    private String ybSchoolid;

    /** 所在学校名称 */
    private String ybSchoolname;

    public static YbUserDTO from(JsonNode userInfoJson) {
        Preconditions.checkNotNull(userInfoJson, "userInfoJson can not be empty!");
        YbUserDTO ybUserDTO = new YbUserDTO();
        ybUserDTO.setYbUserId(Long.parseLong(userInfoJson.get("yb_userid").asText()))
                 .setYbUsername( userInfoJson.get("yb_username").asText())
                 .setYbUsernick(userInfoJson.get("yb_usernick").asText())
                 .setYbSex(userInfoJson.get("yb_sex").asText())
                 .setYbMoney(Integer.parseInt(userInfoJson.get("yb_money").asText()))
                 .setYbExp(userInfoJson.get("yb_exp").asText())
                 .setYbUserhead(userInfoJson.get("yb_userhead").asText())
                 .setYbSchoolid(userInfoJson.get("yb_schoolid").asText())
                 .setYbSchoolname(userInfoJson.get("yb_schoolname").asText());
        return ybUserDTO;
    }
}
