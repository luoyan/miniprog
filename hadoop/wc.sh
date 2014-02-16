$HADOOP_HOME/bin/hadoop jar $HADOOP_HOME/contrib/streaming/hadoop-streaming-1.2.1.jar \
-input /home/hadoop/data \
-output /home/hadoop/output2 \
-mapper cat \
-reducer wc
