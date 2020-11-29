package com.hdh.lifeup.model.query;

import com.hdh.lifeup.model.constant.CommonConst;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * PageQuery class<br/>
 * 分页查询基类，涉及业务查询的分页查询可继承此类
 * @author hdonghong
 * @since 2020/10/25
 */
@ApiModel("分页查询模型")
@Data
public class PageQuery {

    @ApiModelProperty("当前页码，从1开始，查询必传")
    private Long currentPage;

    @ApiModelProperty("当前页展示的数据量，大于0，查询必传")
    private Long size;

    @ApiModelProperty("通用排序字段，使用前需确认对应值是否支持；0时间倒序；1活跃度优先；2点赞量优先")
    private Integer rank;

    @ApiModelProperty("来源，1国内；2海外")
    private Integer createSource;

    @ApiModelProperty("过滤器：only_the_user只保留指定用户；")
    private String filter;

    @ApiModelProperty("过滤器值为only_the_user时才有效")
    private Long userId;

    @ApiModelProperty("关键词搜索")
    private String keywords;

    public Long getCurrentPage() {
        return currentPage == null || createSource < 1 ? 1 : currentPage;
    }

    /**
     * size取值[1, 100]
     * @return
     */
    public Long getSize() {
        return size == null || size < 1 || size > 100 ? 20 : size;
    }

    public Integer getCreateSource() {
        return createSource == null ? CommonConst.CreateSource.CHINA : createSource;
    }
}
