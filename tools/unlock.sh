host=localhost
port=2201
db=campaigns
nick=chinchinstyle
nick=$1
echo "unlocking "$nick" ..."
#echo "db.sync_status.update({\"_id\": \"$nick\"}, {\"sync_lock\":false})"|mongo $host:$port/$db
echo "db.sync_status.remove({\"_id\": \"$nick\"})"|mongo $host:$port/$db
echo "db.sync_status.insert({ \"_id\" : \"$nick\", \"last_sync_time\" : ISODate(\"2013-08-30T14:44:54.215Z\"), \"nick\" : \"$nick\", \"sync_failed\" : false, \"sync_lock\" : false, \"sync_lock_time\" : ISODate(\"2013-08-30T14:44:53.746Z\") })"| mongo $host:$port/$db
echo "unlocking "$nick" done"
