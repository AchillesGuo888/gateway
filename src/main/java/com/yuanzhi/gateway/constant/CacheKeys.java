package com.yuanzhi.gateway.constant;

/**
 * @ClassName: CacheKeys
 * @author: zhangding
 * @date: 2020/2/24  4:59 下午
 */
public class CacheKeys {
    /**
     * 单位秒，app token 30天
     */
    public static final int APP_TOKEN_EXPIRE_TIME = 30 * 24 * 60 * 60;
    /**
     * 单位秒，wap token 30分钟
     */
    public static final int WAP_TOKEN_EXPIRE_TIME = 30 * 60;
    /**
     * 单位秒，web token 2小时
     */
    public static final int WEB_TOKEN_EXPIRE_TIME = 3 * 24;
}
