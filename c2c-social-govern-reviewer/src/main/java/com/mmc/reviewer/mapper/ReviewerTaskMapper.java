package com.mmc.reviewer.mapper;

import com.mmc.reviewer.api.domain.ReviewerTask;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ReviewerTaskMapper {

    void insert(ReviewerTask reviewerTask);

    void update(ReviewerTask reviewerTask);
}
