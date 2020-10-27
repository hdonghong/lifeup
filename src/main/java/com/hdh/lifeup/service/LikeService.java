package com.hdh.lifeup.service;

import com.hdh.lifeup.redis.KeyPrefix;

/**
 * LikeService interface<br/>
 *
 * @author hdonghong
 * @since 2019/05/25
 */
public interface LikeService {

    int doLike(Long userId, Long memberRecordId);

    int undoLike(Long userId, Long memberRecordId);

    int getUserLikeCount(Long userId);

    int getUserExchangedLikeCount(Long userId);

    int getRecordLikeCount(Long memberRecordId);

    <T> int isLike(KeyPrefix<T> keyPrefix, long sourceId, long userId);

    int exchangeLike(Long userId, Integer count);

    int doLikeTeam(Long userId, Long teamId);

    int undoLikeTeam(Long userId, Long teamId);

    int getTeamLikeCount(Long teamId);

    int doLikeGoods(Long userId, Long goodsId);

    int undoLikeGoods(Long userId, Long goodsId);

    int getGoodsLikeCount(Long goodsId);
}
