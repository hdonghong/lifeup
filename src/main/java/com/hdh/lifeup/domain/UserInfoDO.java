package com.hdh.lifeup.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import com.hdh.lifeup.base.BaseDO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.time.Instant;

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
    private String nickName;

    /** '联系方式' */
    private String userPhone;

    /** '用户密码' */
    private String userPassword;

    /** '0女，1男，2保密' */
    private Integer userSex;

    /**'用户地区'  */
    private String userAddress;

    /** '-1封号，0未激活，1正常' */
    private Integer userStatus;

    /** '创建时间' */
    private Instant createTime;

    /** 通用字段 0存在；1被删除 */
    @TableLogic
    private Integer isDel;
}
