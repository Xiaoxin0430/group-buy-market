package cn.xx.trigger.http;

import cn.xx.api.IMarketTradeService;
import cn.xx.api.dto.LockMarketPayOrderRequestDTO;
import cn.xx.api.dto.LockMarketPayOrderResponseDTO;
import cn.xx.api.dto.SettlementMarketPayOrderRequestDTO;
import cn.xx.api.dto.SettlementMarketPayOrderResponseDTO;
import cn.xx.api.response.Response;
import cn.xx.domain.activity.model.entity.MarketProductEntity;
import cn.xx.domain.activity.model.entity.TrialBalanceEntity;
import cn.xx.domain.activity.model.valobj.GroupBuyActivityDiscountVO;
import cn.xx.domain.activity.service.IIndexGroupBuyMarketService;
import cn.xx.domain.trade.model.entity.MarketPayOrderEntity;
import cn.xx.domain.trade.model.entity.PayActivityEntity;
import cn.xx.domain.trade.model.entity.PayDiscountEntity;
import cn.xx.domain.trade.model.entity.UserEntity;
import cn.xx.domain.trade.model.entity.TradePaySettlementEntity;
import cn.xx.domain.trade.model.entity.TradePaySuccessEntity;
import cn.xx.domain.trade.model.valobj.GroupBuyProgressVO;
import cn.xx.domain.trade.service.ITradeLockOrderService;
import cn.xx.domain.trade.service.ITradeSettlementOrderService;
import cn.xx.types.enums.ResponseCode;
import cn.xx.types.exception.AppException;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author xiaoxin
 * @description 营销交易服务
 * @create 2026/8/5 17:08
 */

@Slf4j
@RestController()
@CrossOrigin("*")
@RequestMapping("/api/v1/gbm/trade/")
public class MarketTradeController implements IMarketTradeService {

    //营销试算服务
    @Resource
    private IIndexGroupBuyMarketService indexGroupBuyMarketService;

    //交易服务
    @Resource
    private ITradeLockOrderService tradeOrderService;

    @Resource
    private ITradeSettlementOrderService tradeSettlementOrderService;

    //锁单服务
    @RequestMapping(
            value = "lock_market_pay_order",
            method = RequestMethod.POST)
    @Override
    public Response<LockMarketPayOrderResponseDTO> lockMarketPayOrder(
            @RequestBody LockMarketPayOrderRequestDTO lockMarketPayOrderRequestDTO) {
        try {
            // 取出请求参数
            String userId =
                    lockMarketPayOrderRequestDTO.getUserId();
            String source =
                    lockMarketPayOrderRequestDTO.getSource();
            String channel =
                    lockMarketPayOrderRequestDTO.getChannel();
            String goodsId =
                    lockMarketPayOrderRequestDTO.getGoodsId();
            Long activityId =
                    lockMarketPayOrderRequestDTO.getActivityId();
            String outTradeNo =
                    lockMarketPayOrderRequestDTO.getOutTradeNo();
            String teamId =
                    lockMarketPayOrderRequestDTO.getTeamId();
            String notifyUrl =
                    lockMarketPayOrderRequestDTO.getNotifyUrl();

            log.info("营销交易锁单:{} LockMarketPayOrderRequestDTO:{}",
                    userId,
                    JSON.toJSONString(lockMarketPayOrderRequestDTO)
            );

            //参数检验
            if (StringUtils.isBlank(userId)
                    || StringUtils.isBlank(source)
                    || StringUtils.isBlank(channel)
                    || StringUtils.isBlank(goodsId)
                    || null == activityId
                    || StringUtils.isBlank(notifyUrl)
            ) {

                return Response
                        .<LockMarketPayOrderResponseDTO>builder()
                        .code(
                                ResponseCode.ILLEGAL_PARAMETER.getCode()
                        )
                        .info(
                                ResponseCode.ILLEGAL_PARAMETER.getInfo()
                        )
                        .build();
            }

            // 查询 outTradeNo 是否已经存在交易记录
            MarketPayOrderEntity marketPayOrderEntity =
                    tradeOrderService.queryNoPayMarketPayOrderByOutTradeNo(
                            userId,
                            outTradeNo
                    );
            //若存在就返回该订单
            if (null != marketPayOrderEntity) {
                LockMarketPayOrderResponseDTO lockMarketPayOrderResponseDTO =
                        LockMarketPayOrderResponseDTO
                                .builder()
                                .orderId(
                                        marketPayOrderEntity.getOrderId()
                                )
                                .deductionPrice(
                                        marketPayOrderEntity.getDeductionPrice()
                                )
                                .tradeOrderStatus(
                                        marketPayOrderEntity.getTradeOrderStatusEnumVO().getCode()
                                )
                                .build();

                log.info("交易锁单记录(存在):{} " + "marketPayOrderEntity:{}",
                        userId,
                        JSON.toJSONString(marketPayOrderEntity)
                );

                return Response
                        .<LockMarketPayOrderResponseDTO>builder()
                        .code(ResponseCode.SUCCESS.getCode())
                        .info(ResponseCode.SUCCESS.getInfo())
                        .data(lockMarketPayOrderResponseDTO)
                        .build();
            }

            // 判断拼团锁单是否完成了目标
            if (StringUtils.isNotBlank(teamId)) {
                GroupBuyProgressVO groupBuyProgressVO = tradeOrderService.queryGroupBuyProgress(teamId);

                if (null != groupBuyProgressVO &&
                        Objects.equals(groupBuyProgressVO.getTargetCount(), groupBuyProgressVO.getLockCount()
                        )) {
                    log.info("交易锁单拦截-拼单目标已达成:{} {}", userId, teamId);

                    return Response
                            .<LockMarketPayOrderResponseDTO>builder()
                            .code(ResponseCode.E0006.getCode())
                            .info(ResponseCode.E0006.getInfo())
                            .build();
                }
            }

            // 无之前的订单且队伍没满员，进行营销优惠试算
            TrialBalanceEntity trialBalanceEntity =
                    indexGroupBuyMarketService.indexMarketTrial(
                            MarketProductEntity
                                    .builder()
                                    .userId(userId)
                                    .source(source)
                                    .channel(channel)
                                    .goodsId(goodsId)
                                    .activityId(activityId)
                                    .build()
                    );

            // 人群限定
            if (!trialBalanceEntity.getIsVisible()
                    || !trialBalanceEntity.getIsEnable()) {

                return Response.<LockMarketPayOrderResponseDTO>builder()
                        .code(ResponseCode.E0007.getCode())
                        .info(ResponseCode.E0007.getInfo())
                        .build();
            }

            //取出活动配置
            GroupBuyActivityDiscountVO groupBuyActivityDiscountVO =
                    trialBalanceEntity.getGroupBuyActivityDiscountVO();

            //锁单
            marketPayOrderEntity =
                    tradeOrderService.lockMarketPayOrder(
                            UserEntity.builder()
                                    .userId(userId)
                                    .build(),

                            PayActivityEntity.builder()
                                    .teamId(teamId)
                                    .activityId(activityId)
                                    .activityName(
                                            groupBuyActivityDiscountVO
                                                    .getActivityName()
                                    )
                                    .startTime(
                                            groupBuyActivityDiscountVO
                                                    .getStartTime()
                                    )
                                    .endTime(
                                            groupBuyActivityDiscountVO
                                                    .getEndTime()
                                    )
                                    .validTime(
                                            groupBuyActivityDiscountVO.getValidTime()
                                    )
                                    .targetCount(
                                            groupBuyActivityDiscountVO
                                                    .getTarget()
                                    )
                                    .build(),

                            PayDiscountEntity.builder()
                                    .source(source)
                                    .channel(channel)
                                    .goodsId(goodsId)
                                    .goodsName(
                                            trialBalanceEntity
                                                    .getGoodsName()
                                    )
                                    .originalPrice(
                                            trialBalanceEntity
                                                    .getOriginalPrice()
                                    )
                                    .deductionPrice(
                                            trialBalanceEntity
                                                    .getDeductionPrice()
                                    )
                                    .payPrice(
                                            trialBalanceEntity.getPayPrice()
                                    )
                                    .outTradeNo(outTradeNo)
                                    .notifyUrl(notifyUrl)
                                    .build()
                    );

            log.info(
                    "交易锁单记录(新):{} marketPayOrderEntity:{}",
                    userId,
                    JSON.toJSONString(marketPayOrderEntity)
            );

            // 返回结果
            return Response
                    .<LockMarketPayOrderResponseDTO>builder()
                    .code(ResponseCode.SUCCESS.getCode())
                    .info(ResponseCode.SUCCESS.getInfo())
                    .data(
                            LockMarketPayOrderResponseDTO
                                    .builder()
                                    .orderId(
                                            marketPayOrderEntity
                                                    .getOrderId()
                                    )
                                    .deductionPrice(
                                            marketPayOrderEntity
                                                    .getDeductionPrice()
                                    )
                                    .tradeOrderStatus(
                                            marketPayOrderEntity
                                                    .getTradeOrderStatusEnumVO()
                                                    .getCode()
                                    )
                                    .build()
                    )
                    .build();

        } catch (AppException e) {
            log.error(
                    "营销交易锁单业务异常:{} " +
                            "LockMarketPayOrderRequestDTO:{}",
                    lockMarketPayOrderRequestDTO.getUserId(),
                    JSON.toJSONString(
                            lockMarketPayOrderRequestDTO
                    ),
                    e
            );

            return Response
                    .<LockMarketPayOrderResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();

        } catch (Exception e) {
            log.error(
                    "营销交易锁单服务失败:{} " +
                            "LockMarketPayOrderRequestDTO:{}",
                    lockMarketPayOrderRequestDTO.getUserId(),
                    JSON.toJSONString(
                            lockMarketPayOrderRequestDTO
                    ),
                    e
            );

            return Response
                    .<LockMarketPayOrderResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

    @RequestMapping(
            value = "settlement_market_pay_order",
            method = RequestMethod.POST)
    @Override
    public Response<SettlementMarketPayOrderResponseDTO> settlementMarketPayOrder(
            @RequestBody SettlementMarketPayOrderRequestDTO requestDTO) {
        try {
            log.info("营销交易组队结算开始:{} outTradeNo:{}",
                    requestDTO.getUserId(), requestDTO.getOutTradeNo());

            if (StringUtils.isBlank(requestDTO.getUserId())
                    || StringUtils.isBlank(requestDTO.getSource())
                    || StringUtils.isBlank(requestDTO.getChannel())
                    || StringUtils.isBlank(requestDTO.getOutTradeNo())
                    || null == requestDTO.getOutTradeTime()) {
                return Response.<SettlementMarketPayOrderResponseDTO>builder()
                        .code(ResponseCode.ILLEGAL_PARAMETER.getCode())
                        .info(ResponseCode.ILLEGAL_PARAMETER.getInfo())
                        .build();
            }

            TradePaySettlementEntity tradePaySettlementEntity =
                    tradeSettlementOrderService.settlementMarketPayOrder(
                            TradePaySuccessEntity.builder()
                                    .source(requestDTO.getSource())
                                    .channel(requestDTO.getChannel())
                                    .userId(requestDTO.getUserId())
                                    .outTradeNo(requestDTO.getOutTradeNo())
                                    .outTradeTime(requestDTO.getOutTradeTime())
                                    .build());

            SettlementMarketPayOrderResponseDTO responseDTO =
                    SettlementMarketPayOrderResponseDTO.builder()
                            .userId(tradePaySettlementEntity.getUserId())
                            .teamId(tradePaySettlementEntity.getTeamId())
                            .activityId(tradePaySettlementEntity.getActivityId())
                            .outTradeNo(tradePaySettlementEntity.getOutTradeNo())
                            .build();

            Response<SettlementMarketPayOrderResponseDTO> response =
                    Response.<SettlementMarketPayOrderResponseDTO>builder()
                            .code(ResponseCode.SUCCESS.getCode())
                            .info(ResponseCode.SUCCESS.getInfo())
                            .data(responseDTO)
                            .build();

            log.info("营销交易组队结算完成:{} outTradeNo:{} response:{}",
                    requestDTO.getUserId(), requestDTO.getOutTradeNo(),
                    JSON.toJSONString(response));
            return response;
        } catch (AppException e) {
            log.error("营销交易组队结算异常:{} request:{}",
                    requestDTO.getUserId(), JSON.toJSONString(requestDTO), e);
            return Response.<SettlementMarketPayOrderResponseDTO>builder()
                    .code(e.getCode())
                    .info(e.getInfo())
                    .build();
        } catch (Exception e) {
            log.error("营销交易组队结算失败:{} request:{}",
                    requestDTO.getUserId(), JSON.toJSONString(requestDTO), e);
            return Response.<SettlementMarketPayOrderResponseDTO>builder()
                    .code(ResponseCode.UN_ERROR.getCode())
                    .info(ResponseCode.UN_ERROR.getInfo())
                    .build();
        }
    }

}
