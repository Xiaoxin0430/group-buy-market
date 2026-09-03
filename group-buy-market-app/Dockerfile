# 基础镜像
FROM registry.cn-hangzhou.aliyuncs.com/xfg-studio/group-buy-market-app:1.3

# 作者
LABEL org.opencontainers.image.authors="xiaofuge"

# 配置
ENV PARAMS=""

# 时区
ENV TZ=PRC
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 添加应用
ADD target/group-buy-market-app.jar /group-buy-market-app.jar

ENTRYPOINT ["sh","-c","java -jar $JAVA_OPTS /group-buy-market-app.jar $PARAMS"]
