package com.hdh.lifeup.service.impl;

import com.hdh.lifeup.dao.FeedbackMapper;
import com.hdh.lifeup.model.domain.FeedbackDO;
import com.hdh.lifeup.model.dto.FeedbackDTO;
import com.hdh.lifeup.service.FeedbackService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * FeedbackServiceImpl class<br/>
 *
 * @author hdonghong
 * @since 2019/10/17
 */
@Slf4j
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackMapper feedbackMapper;

    @Override
    public FeedbackDTO insert(FeedbackDTO feedbackDTO) {
        feedbackMapper.insert(feedbackDTO.toDO(FeedbackDO.class));
        return feedbackDTO;
    }
}
