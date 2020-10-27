package com.hdh.lifeup.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hdh.lifeup.dao.RedeemCodeMapper;
import com.hdh.lifeup.exception.GlobalException;
import com.hdh.lifeup.model.domain.RedeemCodeDO;
import com.hdh.lifeup.model.enums.CodeMsgEnum;
import com.hdh.lifeup.model.enums.RedeemCodeEnum;
import com.hdh.lifeup.service.RedeemCodeService;
import com.hdh.lifeup.util.TokenUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.Random;

/**
 * RedeemCodeServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2020/04/06
 */
@Service
public class RedeemCodeServiceImpl implements RedeemCodeService {

    @Resource
    private RedeemCodeMapper redeemCodeMapper;

    @Override
    public boolean redeem(Long userId, String code) {
        if (userId == null || StringUtils.isEmpty(code)) {
            return false;
        }
        // 已经兑换过的，不能再兑换
        QueryWrapper<RedeemCodeDO> queryWrapper = new QueryWrapper<RedeemCodeDO>()
                .eq("redeem_code", code)
                .eq("status", RedeemCodeEnum.REDEEMED.getStatus());
        Integer count = redeemCodeMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new GlobalException(CodeMsgEnum.CODE_HAS_BEEN_REDEEMED);
        }
        queryWrapper = new QueryWrapper<RedeemCodeDO>()
                .eq("user_id", userId)
                .eq("status", RedeemCodeEnum.REDEEMED.getStatus());
        count = redeemCodeMapper.selectCount(queryWrapper);
        if (count > 0) {
            throw new GlobalException(CodeMsgEnum.USER_HAS_REDEEMED);
        }
        // 兑换码兑换失败3次

        RedeemCodeDO redeemCodeDO = new RedeemCodeDO();
        redeemCodeDO.setUserId(userId);
        redeemCodeDO.setStatus(RedeemCodeEnum.REDEEMED.getStatus());
        Integer result = redeemCodeMapper.update(redeemCodeDO,
                new QueryWrapper<RedeemCodeDO>().eq("redeem_code", code));
        return result != 0;
    }

    @Override
    public boolean createCodes(int limit) {
        if (limit > 1000) {
            return false;
        }
        Random random = new Random();
        while (limit-- > 0) {
            RedeemCodeDO redeemCodeDO = new RedeemCodeDO();
            String str = TokenUtil.get().substring(0, 16 + random.nextInt(8)).toUpperCase();
            if (str.endsWith("-")) {
                str = str.substring(0, str.length() - 2);
            }
            redeemCodeDO.setRedeemCode("LIFEUP@" + str);
            redeemCodeMapper.insert(redeemCodeDO);
        }
        return true;
    }
}
