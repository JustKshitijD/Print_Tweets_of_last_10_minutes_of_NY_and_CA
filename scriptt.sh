#! /bin/bash

div ()  # Arguments: dividend and divisor
{
        if [ $2 -eq 0 ]; then echo division by 0; exit; fi
        local p=12                            # precision
        local c=${c:-0}                       # precision counter
        local d=.                             # decimal separator
        local r=$(($1/$2)); echo -n $r        # result of division
        local m=$(($r*$2))
        [ $c -eq 0 ] && [ $m -ne $1 ] && echo -n $d
        [ $1 -eq $m ] || [ $c -eq $p ] && echo && return
        local e=$(($1-$m))
        c=$(($c+1))
        div $(($e*10)) $2
} 

/usr/bin/python /home/ubuntu/accessing_published_tweets.py CA

#gnome-terminal -e /home/ubuntu/kafka_2.12-1.1.1/bin/zookeeper-server-start.sh /home/ubuntu/kafka_2.12-1.1.1/config/zookeeper.properties" 
/home/ubuntu/kafka_2.12-1.1.1/bin/zookeeper-server-start.sh /home/ubuntu/kafka_2.12-1.1.1/config/zookeeper.properties &
#wait $!
sleep 30
#gnome-terminal -e /home/ubuntu/kafka_2.12-1.1.1/bin/kafka-server-start.sh /home/ubuntu/kafka_2.12-1.1.1/config/server.properties"  
/home/ubuntu/kafka_2.12-1.1.1/bin/kafka-server-start.sh /home/ubuntu/kafka_2.12-1.1.1/config/server.properties &

sleep 60

echo "QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ"
/home/ubuntu/kafka_2.12-1.1.1/bin/kafka-console-producer.sh --broker-list "ec2-54-210-100-54.compute-1.amazonaws.com:9092" --topic "mytopic" < /home/ubuntu/tweets.txt 
echo "QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ"
timeout 60 /home/ubuntu/spark-3.0.0-preview2-bin-hadoop2.7/bin/spark-submit /home/ubuntu/stream.jar "ec2-54-210-100-54.compute-1.amazonaws.com:9092" "mygroup" "mytopic" 

sleep 10

arr=(/home/ubuntu/Text_files/*/)

mv -v ${arr[0]} /home/ubuntu/Text_files/save

python insert_comma.py

tweet_count=$(grep -c "" /home/ubuntu/comma_save_file-1.txt)

echo "CCCCCCCCCCCC: tweet_count: $tweet_count"

#echo $tweet_count >> /home/sayukta/Desktop/IITM-Courses/Cloud/Assgn_5/fp_output 

minSupport=$(div 3 $tweet_count)

/home/ubuntu/spark-3.0.0-preview2-bin-hadoop2.7/bin/spark-submit /home/ubuntu/fp.jar /home/ubuntu/comma_save_file-1.txt --minSupport $minSupport

echo "Tweet_count: $tweet_count" >> /home/ubuntu/fp_output
