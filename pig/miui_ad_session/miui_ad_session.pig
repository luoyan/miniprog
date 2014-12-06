define PARSE_AD_QUERY_LOG `python parse_ad_query_log.py` ship('parse_ad_query_log.py');
/*
define PARSE_AD_STORE_LOG `python parse_ad_store_log.py` ship('parse_ad_store_log.py');
define PARSE_APPSTORE_LOG `python parse_appstore_log.py` ship('parse_appstore_log.py');
define PARSE_SESSION_LOG `python parse_session_log.py` ship('parse_session_log.py');
*/

ad_query_data = load '$AD_QUERY_LOG' using org.apache.pig.piggybank.storage.SequenceFileLoader();
ad_query_data = stream ad_query_data through PARSE_AD_QUERY_LOG as (imei, service, type, path, positions, miui_ad_alg, miui_ad_exp, miui_ad_ads, category_id, position_type, position, package_name, consumption_type, download_no, price, consumption);

/*
ad_store_data = load '$AD_STORE_LOG' using org.apache.pig.piggybank.storage.SequenceFileLoader();
ad_store_data = stream ad_store_data through PARSE_AD_STORE_LOG as (imei, time, log_type);

appstore_data = load '$APPSTORE_LOG' using org.apache.pig.piggybank.storage.SequenceFileLoader();
appstore_data = stream appstore_data through PARSE_APPSTORE_LOG as (user, time, ops, url);

session_data = load '$SESSION_LOG' using org.apache.pig.piggybank.storage.SequenceFileLoader();
session_data = stream session_data through PARSE_SESSION_LOG as (user, time, ops, url);

data = union ad_query_data, ad_store_data;
data_grp = group data by imei;
data_sort = foreach data_grp { generate flatten(data);};


define SESSION `python session.py` ship('session.py', 'utils.py', 'UserSession.py');
result = stream data_sort through SESSION;
result = ad_query_data
*/

store ad_query_data into '$OUTPUT_DATA';

