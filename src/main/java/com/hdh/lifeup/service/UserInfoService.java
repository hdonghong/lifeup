package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.PageDTO;
import com.hdh.lifeup.model.dto.UserInfoDTO;
import com.hdh.lifeup.model.vo.UserDetailVO;
import com.hdh.lifeup.model.vo.UserListVO;
import lombok.NonNull;

/**
 * UserInfoService interface<br/>
 *
 * @author hdonghong
 * @since 2018/08/14
 */
public interface UserInfoService {

    UserInfoDTO getOne(@NonNull Long userId);

    UserInfoDTO insert(@NonNull UserInfoDTO userInfoDTO);

    UserInfoDTO update(@NonNull UserInfoDTO userInfoDTO);

    /**
     * 缓存取，缓存存，通过token获取user
     * @param authenticityToken 鉴权Token
     * @return 用户信息
     */
    UserInfoDTO getByToken(String authenticityToken);

    /**
     * 获取用户详情
     * @param userId 用户id
     * @return 用户详情
     */
    UserDetailVO getDetailById(Long userId);

    /**
     * 关注用户
     * @param userId 用户id
     */
    void follow(Long userId);

    /**
     * 取消关注
     * @param userId 用户id
     */
    void deleteFollowing(Long userId);

    /**
     * 获取我关注的
     * @param userId 用户id
     * @param pageDTO 分页条件
     * @return 用户列表
     */
    PageDTO<UserListVO> getFollowings(Long userId, PageDTO pageDTO);

    /**
     * 获取关注我的
     * @param userId 用户id
     * @param pageDTO 分页条件
     * @return 用户列表
     */
    PageDTO<UserListVO> getFollowers(Long userId, PageDTO pageDTO);

    /**
     * 获取指定用户id的朋友圈
     * @param userId 用户id
     * @param pageDTO 分页条件
     * @param scope 动态显示范围
     * @return 动态列表
     */
//    PageDTO<RecordDTO> getMoments(Long userId, PageDTO pageDTO, int scope);

    /**
     * 获取关注者的属性排行榜
     * @param userId 用户id
     * @param pageDTO 分页条件
     * @return 用户列表
     */
    PageDTO<UserListVO> getFollowingsRank(Long userId, PageDTO pageDTO);
}
