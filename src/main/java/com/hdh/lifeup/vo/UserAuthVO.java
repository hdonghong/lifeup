package com.hdh.lifeup.vo;

import com.hdh.lifeup.constant.AuthTypeConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * UserAuthVO class<br/>
 *
 * @author hdonghong
 * @since 2018/09/01
 */
@Data
@Accessors(chain = true)
@ApiModel("注册/登录VO")
public class UserAuthVO implements Serializable {

    private static final long serialVersionUID = -8361866541896304535L;

    /** '用户昵称' */
    @NotEmpty
    private String nickname;

    /** '0女，1男，2保密' */
    @ApiModelProperty(value = "0女，1男，2保密")
    private Integer userSex;

    /**'用户地区'  */
    private String userAddress;

    /** 用户头像 */
    private String userHead;

    /** 绑定的类型 */
    @ApiModelProperty(value = "验证的类型，目前限定为qq、phone、google", example = "qq")
    @Pattern(regexp = AuthTypeConst.PHONE + "|" + AuthTypeConst.QQ + "|" + AuthTypeConst.GOOGLE, message = "必须是系统支持的验证类型")
    @NotEmpty
    private String authType;

    @ApiModelProperty(name = "accessToken", value = "预留字段，目前只用于传递32位的密码", example = "sddjkgkclsi2k4h1kx9aksd33k1lsk8d")
    @Length(min = 32, max = 32, message = "必须是32位的字符串")
    private String accessToken;

    @ApiModelProperty(value = "第三方授权时给的唯一标识，比如qq的openid", example = "sadasdasasfsdfasd")
    private String authIdentifier;

}
