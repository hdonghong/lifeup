package com.hdh.lifeup.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;

/**
 * MobVO class<br/>
 *
 * @author hdonghong
 * @since 2018/09/20
 */
@ApiModel("Mob提供的短信验证VO类")
@Data
@Accessors(chain = true)
public class MobVO {

    private String appKey;

    @NotEmpty
    @Length(min = 11, max = 11, message = "必须是11位的手机号码")
    private String phone;

    @NotEmpty
    private String zone;

    @NotEmpty
    private String code;
}
