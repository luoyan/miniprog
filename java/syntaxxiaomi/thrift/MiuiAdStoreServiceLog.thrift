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