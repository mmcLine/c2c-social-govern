package com.mmc.reward.mapper;

import com.mmc.reward.domain.RewardCoin;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface RewardCoinMapper {

    void insert(RewardCoin rewardCoin);
}
