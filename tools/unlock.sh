host=localhost
port=2201
db=campaigns
nick=chinchinstyle
echo "unlocking "$nick" ..."
echo "db.sync_status.update({\"_id\": \"$nick\"}, {\"sync_lock\":false})"|mongo $host:$port/$db
echo "unlocking "$nick" done"
