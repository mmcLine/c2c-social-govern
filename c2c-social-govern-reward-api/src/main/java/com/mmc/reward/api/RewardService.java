package com.mmc.reward.api;

import java.util.List;

public interface RewardService {

    void giveReward(List<Long> reviewerIds);
}
