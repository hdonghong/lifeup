package com.hdh.lifeup.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.Instant;

/**
 * UserDetailVO class<br/>
 *
 * @author hdonghong
 * @since 2018/09/21
 */
@ApiModel("用户详情VO类")
@Data
@Accessors(chain = true)
public class UserDetailVO {

    private Long userId;

    /** '用户昵称' */
    @ApiModelProperty("用户昵称")
    private String nickname;

    /** '0女，1男，2保密' */
    @ApiModelProperty("** '0女，1男，2保密' ")
    private Integer userSex;

    /**'用户地区'  */
    @ApiModelProperty("'用户地区'")
    private String userAddress;

    /** 用户头像 */
    @ApiModelProperty("用户头像")
    private String userHead;

    /** '-1已删除，0未激活，1正常' */
    @ApiModelProperty("'-1已删除，0未激活，1正常' ")
    private Integer userStatus;

    /** '注册时间' */
    @ApiModelProperty("注册时间")
    private Instant createTime;

    /** 当有绑定手机时，手机号 */
    @ApiModelProperty("当有绑定手机时，手机号 ")
    private String phone;

    @ApiModelProperty("团队数量")
    private Integer teamAmount;

    @ApiModelProperty("我关注的数量")
    private Long followingAmount;

    @ApiModelProperty("关注者数量")
    private Long followerAmount;

    @ApiModelProperty("-1自己；0未关注；1已关注；互相关注")
    private Integer isFollow;

    public UserDetailVO() {
        this.teamAmount = 0;
        this.followingAmount = 0L;
        this.followerAmount = 0L;
    }
}
