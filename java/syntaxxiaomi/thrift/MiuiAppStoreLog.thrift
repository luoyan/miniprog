namespace java com.xiaomi.miui.ad.thrift.model
struct MiuiLogScribeInfo{}
struct MiuiAppStoreLogHttpRequest {
    1: MiuiLogScribeInfo scribeInfo;
    2: i64 timestamp;
    3: string logType;
    4: string path;
    5: map<string, string> httpParams;
    6: string httpMethod;
    7: string httpMethodPath;
    8: i64 userId;
    9: optional string searchResultByJson; 
}
