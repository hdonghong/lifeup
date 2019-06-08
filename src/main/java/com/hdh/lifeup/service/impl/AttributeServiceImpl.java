package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.base.BaseDTO;
import com.hdh.lifeup.model.domain.AttributeDO;
import com.hdh.lifeup.model.dto.AttributeDTO;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.dao.AttributeMapper;
import com.hdh.lifeup.service.AttributeService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * AttributeServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2018/08/25
 */
@Slf4j
@Service
public class AttributeServiceImpl implements AttributeService {

    private AttributeMapper attributeMapper;

    @Autowired
    public AttributeServiceImpl(AttributeMapper attributeMapper) {
        this.attributeMapper = attributeMapper;
    }



    @Override
    public AttributeDTO insert(@NonNull AttributeDTO attributeDTO) {
        attributeMapper.insert(attributeDTO.toDO(AttributeDO.class));
        return attributeDTO;
    }

    @Override
    public AttributeDTO update(@NonNull AttributeDTO attributeDTO) {
        Long userId = UserContext.get().getUserId();
        AttributeDO attributeDO = attributeMapper.selectOne(
                new QueryWrapper<AttributeDO>().eq("user_id", userId)
        );
        if (attributeDO == null ) {
            log.error("【更新人物属性】人物属性不存在，也可能是不存在的用户，user = [{}]", UserContext.get());
            throw new GlobalException(CodeMsgEnum.ATTRIBUTE_NOT_EXIST);
        }
        BeanUtils.copyProperties(attributeDTO, attributeDO, "userId");
        Integer result = attributeMapper.update(
                attributeDO,
                new QueryWrapper<AttributeDO>().eq("user_id", userId)
        );
        if (!Objects.equals(1, result)) {
            log.error("【更新人物属性】数据库更新操作失败，user = [{}], attributeDO = [{}]", UserContext.get(), attributeDO);
            throw new GlobalException(CodeMsgEnum.DATABASE_EXCEPTION);
        }
        return attributeDTO;
    }

    @Override
    public AttributeDTO getByUserId(@NonNull Long userId) {
        AttributeDO attributeDO = attributeMapper.selectOne(
                new QueryWrapper<AttributeDO>().eq("user_id", userId)
        );
        if (attributeDO == null ) {
            log.error("【userId获取人物属性】人物属性不存在，也可能是不存在的用户，userId = [{}]", userId);
            throw new GlobalException(CodeMsgEnum.ATTRIBUTE_NOT_EXIST);
        }
        return BaseDTO.from(attributeDO, AttributeDTO.class);
    }
}
