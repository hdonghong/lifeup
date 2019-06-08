package com.hdh.lifeup.model.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * UserInfoDO class<br/>
 * 用户详细信息实体
 * @author hdonghong
 * @since 2018/08/13
 */
@TableName("`user_info`")
@Data
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
public class UserInfoDO extends BaseDO {

    private static final long serialVersionUID = -4074218333370866360L;

    /** '用户主键 */
    @TableId
    private Long userId;

    /** '用户昵称' */
    private String nickname;

    /** '0女，1男，2保密' */
    private Integer userSex;

    /** 特有的盐 */
    private String pwdSalt;

    /**'用户地区'  */
    private String userAddress;

    /** 头像 */
    private String userHead;

    /** '0封号，1正常' */
    private Integer userStatus;

    /** '创建时间' */
    private LocalDateTime createTime;

    private String authTypes;

    /** 通用字段 0存在；1被删除 */
    @TableLogic
    private Integer isDel;
}
