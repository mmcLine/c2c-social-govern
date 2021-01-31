package com.mmc.reward.service;

import com.mmc.reward.api.RewardService;
import com.mmc.reward.domain.RewardCoin;
import com.mmc.reward.mapper.RewardCoinMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Service(
        version = "1.0.0",
        interfaceClass = RewardService.class,
        cluster = "failfast",
        loadbalance = "roundrobin"
)
public class RewardServiceImpl implements RewardService {

    @Autowired
    private RewardCoinMapper rewardCoinMapper;

    @Override
    public void giveReward(List<Long> reviewerIds) {
        for (Long reviewerId:reviewerIds){
            RewardCoin rewardCoin=new RewardCoin();
            rewardCoin.setReviewerId(reviewerId);
            rewardCoin.setCoins(10);
            rewardCoinMapper.insert(rewardCoin);
        }
    }
}
