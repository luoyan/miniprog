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

struct MiuiAdQueryServiceLogAlgorithm {
    1: MiuiLogScribeInfo scribeInfo;
    2: string algorithmName;
    3: string clientInfoStr;
    4: i32 unknown1;
    5: string packageNameList; 
}

struct MiuiAdQueryServiceLogAlgorithmExposeDetail {
    1: MiuiLogScribeInfo scribeInfo;
    2: string logType;
    3: ClientInfo clientInfo;
    4: string algorithmName;
    5: string experiment;
    6: list<string> packageNameList;
}

struct MiuiAdQueryServiceLogAppStoreSearchExpose {
    1: MiuiLogScribeInfo scribeInfo;
    2: string logType;
    3: ClientInfoV3 clientInfoV3;
    4: AppMarketSearchParam searchParam;
    5: SearchAdResult SearchAdResult;
}