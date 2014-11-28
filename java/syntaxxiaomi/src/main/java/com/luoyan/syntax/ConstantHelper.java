package com.luoyan.syntax;

/**
 * User: alex-fang
 * Date: 14-2-25
 * Time: 下午8:12
 */
public class ConstantHelper {
    //    内部缓存更新时间
    public static final long INTER_CACHE_UPDATE_PERIOD = 60000;

    //    内部缓存更新时间,单位小时
    public static final int INTER_CACHE_UPDATE_PERIOD_HOUR = 1;

    // 网络超时后重试次数
    public static final int RETRY_NUM = 3;

    public static final String REDIS_ALL_SEARCH_KEYWORD_VERSION = "1";

    //    存储商务推荐的搜索广告
    public static final String REDIS_BD_RECOMMEND_SEARCH_ADS = "2";

    public static final String REDIS_BD_RECOMMEND_SEARCH_ADS_JSON_KEY = "bd_recommend_ads";

    //    获取广告的默认返回数量
    public static final int DEFAULT_AD_RETURN_NO = 10;
    public static final int DEFAULT_AD_RETURN_TIMES = 2;


    //    游戏类别相关属性
    public static final int GAME_CATEGORY_LEVEL_ID = 15;
    public static final double GAME_BIDDING_PRICE_WEIGHT = 1.0;


    //  默认取推荐结果的某一部分
    public static final int RECOMMEND_NTH_RESULTS = 15;

    public static final int RECOMMEND_SCORE_MAX_AMPLITUDES = 7;


    //    新应用默认均值振幅
    public static final int NEW_APP_CTR_AMPLITUDES = 2;

    //  DecayAlgorithm算法相关常量
    public static final long DECAY_UNIT = 259200000;
    public static final double DECAY_MAX_VALUE = 10;
    public static final int UPDATE_DECAY_RECOMMENDS_RETRY_NO = 2;

    //    RandomAlgorithm算法相关常量
    public static final int RANDOM_ALGORITHM_RANDOM_RANGE = 10;

    //    首页给游戏的特定位置
    //    设置值小于零时则不进行特定位置的指定
//    目前是指定23为游戏专属推广位，前面已有6个CPD推广
    public static final int SPECIFIED_POSITION_FOR_GAME = 7;

    //    各个算法在实验平台平台分配的ID
    public static final int EXP_ID_APP_STORE_FEATURE = 2;
    public static final int EXP_ID_APP_STORE_SEARCH = 3;
    public static final int EXP_ID_BROWSER = 4;
    public static final int EXP_ID_DUO_KAN = 5;
    public static final int EXP_ID_FICTION = 6;
    public static final int EXP_ID_YI_DIAN_NEWS = 7;
    public static final int EXP_ID_ASSOCIATION = 8;
    public static final int EXP_ID_TV_SCREENSAVE = 9;//电视画报广告
    public static final int EXP_ID_PHONE_VIDEO = 10;//小米手机视频广告
    public static final int EXP_ID_THEME = 11;        //小米手机主题推荐


    //    redis读取时间限制在50ms以内
    public static final int REDIS_READ_TIME_OUT = 50;
}
