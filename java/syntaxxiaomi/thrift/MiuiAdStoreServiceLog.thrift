namespace java com.xiaomi.miui.ad.thrift.model

include "../../../../miui_ad/miui_ad_common/thrift/MiuiBiddingAdModel.thrift"
include "../../../../../svn/xiaomi-miliao/cloud/miuicloud/market/AppMarket-Dev/thrift/AppMarketSearchModel.thrift"
include "MiuiLogCommon.thrift"

struct ClientInfo{}
struct ClientInfoV2{}
struct AdInfo{}
struct AdResultInfo{}
struct ClientInfoV3{}
struct SearchAdResult{}
struct AppMarketSearchParam{}
struct XiaomiAdInfo{}
struct MiuiLogScribeInfo{}

struct MiuiAdStoreServiceLogAppStore {
    1: MiuiLogScribeInfo scribeInfo;
    2: string logType;
    3: string clientInfoV3;
    4: string packageName;
    5: string downloadUrl;
    6: i64 timestamp;
}

struct MiuiAdStoreServiceLogAlgorithmDownloadDetail {
    1: MiuiLogScribeInfo scribeInfo;
    2: string logType;
    3: string imei;
    4: string packageName;
    5: i32 categoryId;
    6: string position;
    7: string consumptionType;
    8: i64 downloadNo;
    9: i64 price;
    10: i64 consumption;
    11: string miuiAdAlgorithm;
    12: optional string miuiAdExperiment;
}

struct MiuiAdStoreServiceLogBiddingEffectRecord {
    1: MiuiLogScribeInfo scribeInfo;
    2: string logType;
    3: string packageName;
    4: i64 downloadNo;
    5: i64 price;
    6: i64 consumption;
}

struct MiuiAdStoreServiceLogConsumptionDetail {
    1: MiuiLogScribeInfo scribeInfo;
    2: string logType;
    3: string imei;
    4: string packageName;
    5: i32 categoryId;
    6: string position;
    7: string consumptionType;
    8: i64 downloadNo;
    9: i64 price;
    10: i64 consumption;
}

struct MiuiAdStoreServiceLogOtherBiddingEffectRecord {
    1: MiuiLogScribeInfo scribeInfo;
    2: string logType;
    3: string packageName;
    4: i32 mediaType;
    5: i64 downloadNo;
    6: i64 consumption;
}

struct MiuiAdStoreServiceLogFictionEventDetail {
    1: MiuiLogScribeInfo scribeInfo;
    2: string logType;
    3: string eventType;
    4: string imei;
    5: string bookId;
    6: string bookName;
    7: string packageName;
    8: i64 bookNum;
    9: i64 price;
}

struct MiuiAdStoreServiceLogFiction {
    1: MiuiLogScribeInfo scribeInfo;
    2: string logType;
    3: list<string> eventType;
    4: list<string> packageName;
    5: list<string> wifiType;
    6: list<string> unknownCt;
    7: list<string> unknownAt;
    8: list<string> unknownT;
    9: list<string> unknownS;
    10: list<string> unknownPi;
    11: list<string> unknownC;
    12: i64 timestamp;
}
#comment
struct MiuiAdStoreServiceLogBiddingStatusChange {
    1: MiuiLogScribeInfo scribeInfo;
    2: string logType;
    3: string packageName;
    4: string fromStatus;
    5: string toStatus;
}