package com.hdh.lifeup.dto;

import com.hdh.lifeup.base.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.Instant;

/**
 * UserInfoDTO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class UserInfoDTO extends BaseDTO {

    private static final long serialVersionUID = 3900595581562692523L;

    private Long userId;

    /** '用户昵称' */
    private String nickName;

    /** '联系方式' */
    private String userPhone;

    /** '用户密码' */
    private String userPassword;

    /** '0女，1男，2保密' */
    private Integer userSex;

    /**'用户地区'  */
    private String userAddress;

    /** '-1已删除，0未激活，1正常' */
    private Integer userStatus;

    /** '创建时间' */
    private Instant createTime;

}
