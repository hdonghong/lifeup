package com.hdh.lifeup.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * UserListVO class<br/>
 * 用户列表VO类
 * @author hdonghong
 * @since 2018/09/16
 */
@ApiModel("用户列表VO类")
@Data
@Accessors(chain = true)
public class UserListVO {

    /** '用户昵称' */
    private String nickname;

    /**'用户地区'  */
    private String userAddress;

    /** 用户头像 */
    private String userHead;

    private Long userId;

    private LocalDateTime createTime;

    @ApiModelProperty("-1自己；0未关注；1已关注；互相关注")
    private Integer isFollow;

    @ApiModelProperty("属性排行版的属性")
    private Integer point;

    private Integer rank;

}
