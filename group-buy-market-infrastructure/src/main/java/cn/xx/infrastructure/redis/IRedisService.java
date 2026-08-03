package cn.xx.infrastructure.redis;

import org.redisson.api.RBitSet;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author xiaoxin
 * @description
 * @create 2026/7/28 20:39
 */


public interface IRedisService {

    RBitSet getBitSet(String key);

    // 用户 ID 转成 Bitmap 下标
    default int getIndexFromUserId(String userId) {
        try {
            //创建一个MD5计算器
            MessageDigest md = MessageDigest.getInstance("MD5");

            //将userid转换为字节数组并计算md5值
            byte[] hashBytes = md.digest(userId.getBytes(StandardCharsets.UTF_8));

            //将结果转换为一个正整数
            BigInteger bigInt = new BigInteger(1, hashBytes);

            return bigInt
                    .mod(BigInteger.valueOf(Integer.MAX_VALUE))
                    .intValue();
        }catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5 algorithm not found", e);
        }



    }


}
