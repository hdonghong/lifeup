package com.hdh.lifeup.util;

import com.hdh.lifeup.config.QiniuConfig;
import com.hdh.lifeup.enums.CodeMsgEnum;
import com.hdh.lifeup.exception.GlobalException;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author		hdonghong
 * @since		2018/10/16
 */
public class UploadUtil {

	public static String uploadImage(MultipartFile imageFile, String imageUri, QiniuConfig qiniuConfig){
		if (imageFile == null) {
			throw new GlobalException(CodeMsgEnum.SERVER_ERROR);
		}
		// 构造一个带Zone对象的配置类
		Configuration cfg = new Configuration(Zone.zone2());
		UploadManager uploadManager = new UploadManager(cfg);
		// 上传的凭证参数
		String accessKey = qiniuConfig.getAccessKey();
		String secretKey = qiniuConfig.getSecretKey();
		String bucket = qiniuConfig.getBucket();
		
		// 默认不指定key的情况下，以文件内容的hash值作为文件名
//		ByteArrayInputStream inputStream = new ByteArrayInputStream(b);
		Auth auth = Auth.create(accessKey, secretKey);
		String upToken = auth.uploadToken(bucket);
		
		// 设置保存在bucket上的图片文件名
		String originalFilename = imageFile.getOriginalFilename();
		String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
		String imageName = imageUri + UUID.randomUUID().toString() + suffix;
		try {
			uploadManager.put(imageFile.getInputStream(), imageName, upToken, null, null);
		} catch (Exception e) {
			throw new GlobalException(CodeMsgEnum.SERVER_ERROR);
		}

		return qiniuConfig.getCdnPath() + imageName;
		
	}

	public static void main(String[] args) {
		String s = "asdf.jpg";
		System.out.println(s.substring(s.lastIndexOf(".")));
	}
}
