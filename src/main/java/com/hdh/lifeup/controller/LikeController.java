package com.hdh.lifeup.controller;

import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.auth.UserContext;
import com.hdh.lifeup.model.vo.LikeVO;
import com.hdh.lifeup.model.vo.ResultVO;
import com.hdh.lifeup.service.LikeService;
import com.hdh.lifeup.util.Result;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * LikeController class<br/>
 *
 * @author hdonghong
 * @since 2019/05/25
 */
@Api(description = "点赞模块")
@RestController
@RequestMapping("/likes")
public class LikeController {

    @Autowired
    private LikeService likeService;

    @ApiLimiting(maxAccess = 10)
    @PostMapping("/activities/{memberRecordId}")
    public ResultVO likeActivity(@PathVariable Long memberRecordId) {
        int count = likeService.doLike(UserContext.get().getUserId(), memberRecordId);
        LikeVO likeVO = new LikeVO().setLikeCount(count);
        return Result.success(likeVO);
    }


    @ApiLimiting(maxAccess = 10)
    @DeleteMapping("/activities/{memberRecordId}")
    public ResultVO undoLikeActivity(@PathVariable Long memberRecordId) {
        int count = likeService.undoLike(UserContext.get().getUserId(), memberRecordId);
        LikeVO likeVO = new LikeVO().setLikeCount(count);
        return Result.success(likeVO);
    }
}
