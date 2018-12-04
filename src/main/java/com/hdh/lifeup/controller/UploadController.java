package com.hdh.lifeup.controller;

import com.google.common.collect.Lists;
import com.hdh.lifeup.auth.ApiLimiting;
import com.hdh.lifeup.config.QiniuConfig;
import com.hdh.lifeup.util.Result;
import com.hdh.lifeup.util.UploadUtil;
import com.hdh.lifeup.vo.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * UploadController class<br/>
 *
 * @author hdonghong
 * @since 2018/10/19
 */
@Api(description = "上传模块")
@RestController
@RequestMapping("/upload")
@Slf4j
public class UploadController {

    private QiniuConfig qiniuConfig;

    @Autowired
    public UploadController(QiniuConfig qiniuConfig) {
        this.qiniuConfig = qiniuConfig;
    }


    @ApiLimiting
    @ApiOperation(value = "传图片", notes = "imageCategory中，如果传动态就是activity")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "authenticity-token", required = true, paramType = "header", dataType = "String"),
    })
    @PostMapping("/image/{imageCategory}")
    public ResultVO<List<String>> uploadImage(
            @RequestParam("imageFiles") List<MultipartFile> imageFiles,
            @PathVariable("imageCategory") String imageCategory) {
        List<String> imagePaths = Lists.newArrayList();
        if (imageFiles.size() > 0) {
            log.info("开始上传{}张图片", imageFiles.size());
            for (MultipartFile image : imageFiles) {
                imagePaths.add(UploadUtil.uploadImage(image, QiniuConfig.getImageURI(imageCategory), qiniuConfig));
            }
            log.info("上传图片成功");
        }
        return Result.success(imagePaths);
    }
}
