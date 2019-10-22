package com.hdh.lifeup.service;

import com.hdh.lifeup.model.dto.FeedbackDTO;

/**
 * FeedbackService interface<br/>
 * 用户反馈
 * @author hdonghong
 * @since 2019/10/17
 */
public interface FeedbackService {

    FeedbackDTO insert(FeedbackDTO feedbackDTO);
}
