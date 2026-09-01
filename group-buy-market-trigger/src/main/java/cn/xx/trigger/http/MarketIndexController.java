package cn.xx.trigger.http;

import cn.xx.api.IMarketIndexService;
import cn.xx.api.dto.GoodsMarketRequestDTO;
import cn.xx.api.dto.GoodsMarketResponseDTO;
import cn.xx.api.response.Response;
import cn.xx.domain.activity.model.entity.MarketProductEntity;
import cn.xx.domain.activity.model.entity.TrialBalanceEntity;
import cn.xx.domain.activity.model.entity.UserGroupBuyOrderDetailEntity;
import cn.xx.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.xx.domain.activity.model.valobj.TeamStatisticVO;
import cn.xx.domain.activity.service.IIndexGroupBuyMarketService;
import cn.xx.types.enums.ResponseCode;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author xiaoxin
 * @description 营销首页服务
 * @create 2026/9/1 19:50
 */

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/gbm/index/")
public class MarketIndexController implements IMarketIndexService {

    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;

    @RequestMapping(value = "query_group_buy_market_config", method = RequestMethod.POST)
    @Override
    public Response<GoodsMarketResponseDTO> queryGroupBuyMarketConfig(
            @RequestBody GoodsMarketRequestDTO requestDTO) {

        try {
            log.info("查询拼团营销配置开始:{} goodsId:{}",
                    requestDTO.getUserId(), requestDTO.getGoodsId());

            if (StringUtils.isBlank(requestDTO.getUserId())
                    || StringUtils.isBlank(requestDTO.getSource())
                    || StringUtils.isBlank(requestDTO.getChannel())
                    || StringUtils.isBlank(requestDTO.getGoodsId())) {
                return Response.<GoodsMarketResponseDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            //营销试算
            TrialBalanceEntity trialBalanceEntity =
                    indexGroupBuyMarketService.indexMarketTrial(MarketProductEntity.builder()
                            .userId(requestDTO.getUserId())
                            .source(requestDTO.getSource())
                            .channel(requestDTO.getChannel())
                            .goodsId(requestDTO.getGoodsId())
                            .build());

            GroupBuyActivityDiscountVO groupBuyActivityDiscountVO =
                    trialBalanceEntity.getGroupBuyActivityDiscountVO();
            Long activityId = groupBuyActivityDiscountVO.getActivityId();

            //查询用户正在参与的拼团
            List<UserGroupBuyOrderDetailEntity> userGroupBuyOrderDetailEntities =
                    indexGroupBuyMarketService.queryInProgressUserGroupBuyOrderDetailList(
                            activityId, requestDTO.getUserId(), 1, 2);

            //查询平台拼团统计
            TeamStatisticVO teamStatisticVO =
                    indexGroupBuyMarketService.queryTeamStatisticByActivityId(activityId);

            //组装商品信息
            GoodsMarketResponseDTO.Goods goods = GoodsMarketResponseDTO.Goods.builder()
                    .goodsId(trialBalanceEntity.getGoodsId())
                    .originalPrice(trialBalanceEntity.getOriginalPrice())
                    .deductionPrice(trialBalanceEntity.getDeductionPrice())
                    .payPrice(trialBalanceEntity.getPayPrice())
                    .build();

            //组装用户拼团列表
            List<GoodsMarketResponseDTO.Team> teams = new ArrayList<>();
            if (null != userGroupBuyOrderDetailEntities && !userGroupBuyOrderDetailEntities.isEmpty()) {
                for (UserGroupBuyOrderDetailEntity detail : userGroupBuyOrderDetailEntities) {
                    teams.add(GoodsMarketResponseDTO.Team.builder()
                            .userId(detail.getUserId())
                            .teamId(detail.getTeamId())
                            .activityId(detail.getActivityId())
                            .targetCount(detail.getTargetCount())
                            .completeCount(detail.getCompleteCount())
                            .lockCount(detail.getLockCount())
                            .validStartTime(detail.getValidStartTime())
                            .validEndTime(detail.getValidEndTime())
                            .validTimeCountdown(
                                    GoodsMarketResponseDTO.Team.differenceDateTime2Str(
                                            new Date(), detail.getValidEndTime()))
                            .outTradeNo(detail.getOutTradeNo())
                            .build());
                }
            }

            GoodsMarketResponseDTO.TeamStatistic teamStatistic =
                    GoodsMarketResponseDTO.TeamStatistic.builder()
                            .allTeamCount(teamStatisticVO.getAllTeamCount())
                            .allTeamCompleteCount(teamStatisticVO.getAllTeamCompleteCount())
                            .allTeamUserCount(teamStatisticVO.getAllTeamUserCount())
                            .build();

            Response<GoodsMarketResponseDTO> response =
                    Response.<GoodsMarketResponseDTO>builder()
                            .code(ResponseCode.SUCCESS.getCode())
                            .info(ResponseCode.SUCCESS.getInfo())
                            .data(GoodsMarketResponseDTO.builder()
                                    .goods(goods)
                                    .teamList(teams)
                                    .teamStatistic(teamStatistic)
                                    .build())
                            .build();

            log.info("查询拼团营销配置完成:{} goodsId:{} response:{}",
                    requestDTO.getUserId(), requestDTO.getGoodsId(),
                    JSON.toJSONString(response));
            return response;
        } catch (Exception e) {
            log.error("查询拼团营销配置失败:{} goodsId:{}",
                    requestDTO.getUserId(), requestDTO.getGoodsId(), e);
            return Response.<GoodsMarketResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }
}
