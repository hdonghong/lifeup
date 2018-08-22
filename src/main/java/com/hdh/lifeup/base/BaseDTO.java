package com.hdh.lifeup.base;

import com.google.common.base.Preconditions;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

/**
 * BaseDTO class<br/>
 * 通用的DTO父类，提供默认DTO与DO的优雅转换，方法命名仿自JDK8日期API
 * @author hdonghong
 * @since 2018/08/18
 */
public abstract class BaseDTO<DO extends BaseDO> implements Serializable {

    private static final long serialVersionUID = 7900474580809524159L;

    /**
     * 将DTO类转换成指定DO类型对象
     * @param doClass 指定DO类型
     * @return DO对象
     */
    public DO toDO(Class<DO> doClass) {
        Preconditions.checkNotNull(doClass, "DO class can not be null");
        try {
            DO aDO = doClass.newInstance();
            BeanUtils.copyProperties(this, aDO);
            return aDO;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 复制传入的DO对象属性到指定的DTO类型对象
     * @param aDO DO对象
     * @param dtoClass DTO类型
     * @param <DTO> DTO泛型类型
     * @return DTO对象
     */
    public static <DTO extends BaseDTO, DO extends BaseDO> DTO from(DO aDO, Class<DTO> dtoClass) {
        Preconditions.checkNotNull(dtoClass, "DTO class can not be null");
        try {
            DTO aDTO = dtoClass.newInstance();
            return (DTO) aDTO.from(aDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将传入的DO对象属性拷贝到当前DTO对象，允许子类重写
     * @param aDO 传入的DO对象
     * @param <DTO> 当前DTO对象类型
     * @return 当前DTO对象
     */
    @SuppressWarnings("unchecked")
    public <DTO extends BaseDTO> DTO from(DO aDO) {
        Preconditions.checkNotNull(aDO, "DO object can not be null");
        BeanUtils.copyProperties(aDO, this);
        return (DTO) this;
    }

}
