package com.hdh.lifeup.dto;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.google.common.base.Preconditions;
import com.hdh.lifeup.base.BaseDO;
import com.hdh.lifeup.base.BaseDTO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * PageDTO class<br/>
 *
 * @author hdonghong
 * @since 2018/08/18
 */
@Getter
@Builder
@ToString
@ApiModel("分页查询的DTO类，暂时是这样，还未处理重复的情况！")
public class PageDTO<T> implements Serializable {
    private static final long serialVersionUID = -6321188008468846904L;

    @ApiModelProperty("当前页码，从1开始，查询必传")
    private Long currentPage;

    @ApiModelProperty("当前页展示的数据量，大于0，查询必传")
    private Long size;

    @ApiModelProperty("总页码，由后端返回")
    private Long totalPage;

    @ApiModelProperty("查询结果，有后端返回")
    private List<T> list;

    public static <DTO extends BaseDTO, DO extends BaseDO> PageDTO<DTO> create(IPage<DO> iPage, Class<DTO> dtoClass) {
        Preconditions.checkNotNull(iPage, "iPage不能为空");
        List<DO> doList = iPage.getRecords();
        List<DTO> dtoList = doList.stream()
                                  .map(aDo -> BaseDTO.from(aDo, dtoClass))
                                  .collect(Collectors.toList());

        return PageDTO.<DTO>builder()
                      .currentPage(iPage.getCurrent())
                      .totalPage((long) Math.ceil((iPage.getTotal() * 1.0) / iPage.getSize()))
                      .list(dtoList)
                      .build();
    }
}
