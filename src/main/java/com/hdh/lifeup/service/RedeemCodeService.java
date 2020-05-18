package com.hdh.lifeup.service;

/**
 * RedeemCodeService interface<br/>
 *
 * @author hdonghong
 * @since 2020/04/06
 */
public interface RedeemCodeService {

    /**
     * 兑换
     * @param userId
     * @param code
     * @return
     */
    boolean redeem(Long userId, String code);

    boolean createCodes(int limit);
}
