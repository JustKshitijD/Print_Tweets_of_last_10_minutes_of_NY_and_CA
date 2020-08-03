# # YouTube Video: https://www.youtube.com/watch?v=rhBZqEWsZU4
# import tweepy
# from tweepy import API 
# from tweepy import Cursor
# from tweepy.streaming import StreamListener
# from tweepy import OAuthHandler
# from tweepy import Stream
 
# import twitter_credentials
# import pandas as pd

# # # # # TWITTER CLIENT # # # #
# class TwitterClient():
#     def __init__(self, twitter_user=None):    #=None is the defaluth arguement that wud be used.
#         self.auth = TwitterAuthenticator().authenticate_twitter_app()
#         self.twitter_client = API(self.auth)     # Client object

#     def get_api(self):
#         return(self.twitter_client)


#      # for info on more functions, see the functions available with tweepy API. http://docs.tweepy.org/en/latest/api.html   
# class StreamListener(StreamListener):
#     def on_status(self, status):
#         print(status.text)
#         print("---------------------------------------------------------")
#         # file1 = open('myfile.txt', 'a') 
    
#         # file1.write(status.location)
#         # file1.write("\n")
       
#         # file1.write("----------------------------------------------------------------")
#         # file1.write("\n")
#     def on_error(self, status_code):
#         if status_code == 420:
#             return False
# # # # # TWITTER AUTHENTICATER # # # #
# class TwitterAuthenticator():

#     def authenticate_twitter_app(self):
#         auth = OAuthHandler(twitter_credentials.CONSUMER_KEY, twitter_credentials.CONSUMER_SECRET)
#         auth.set_access_token(twitter_credentials.ACCESS_TOKEN, twitter_credentials.ACCESS_TOKEN_SECRET)
#         return auth 


 
# if __name__ == '__main__':
 
#     # Authenticate using config.py and connect to Twitter Streaming API.
#     #hash_tag_list = ["donald trump", "hillary clinton", "barack obama", "bernie sanders"]
#     fetched_tweets_filename = "tweets.txt"

#     twitter_client = TwitterClient()

#     api=twitter_client.get_api()

#     myStreamListener = StreamListener()
#     myStream = tweepy.streaming.Stream(auth = api.auth, listener=myStreamListener)
#     myStream.filter(track=['Corona'], locations=[-79,40,-72,45])
#     #myStream.filter(track=['Corona'])

#     # -124,33,-115,42: California
#     # -79,41,-72,45: New York

from tweepy.streaming import StreamListener
from tweepy import OAuthHandler
from tweepy import Stream
import threading
import time
#import thread
import sys
 
import twitter_credentials

import json

state=""
flag=0
 
# # # # TWITTER STREAMER # # # #
class TwitterStreamer():
    """
    Class for streaming and processing live tweets.
    """
    def __init__(self):
        pass

    def stream_tweets(self, fetched_tweets_filename, hash_tag_list):
        # This handles Twitter authetification and the connection to Twitter Streaming API
        global flag
        # if(flag==1):
        #     return
        listener = StdOutListener(fetched_tweets_filename)
        # if(flag==1):
        #     return
        auth = OAuthHandler(twitter_credentials.CONSUMER_KEY, twitter_credentials.CONSUMER_SECRET)
        auth.set_access_token(twitter_credentials.ACCESS_TOKEN, twitter_credentials.ACCESS_TOKEN_SECRET)
        # if(flag==1):
        #     return
        stream = Stream(auth, listener)
        # if(flag==1):
        #     return

        # This line filter Twitter Streams to capture data by the keywords: 
        stream.filter(locations=[-79.46,40.3,-71.51,45.1,-124.26,32.32,-114.8,42])


# # # # TWITTER STREAM LISTENER # # # #
class StdOutListener(StreamListener):
    """
    This is a basic listener that just prints received tweets to stdout.
    """ 
    fetched_tweets_filename=""

    def __init__(self, fetched_tweets_filename):
        self.fetched_tweets_filename = fetched_tweets_filename

    def on_data(self, data):
        global flag
        global state

        try:
            # if flag==1:
            #     return
            y=data.lower().find('full_name')
            ss=data[y:y+60]
            #if(('corona' in data.lower()) and ('ontario' not in data.lower()) and ('new jersey' not in data.lower()) and ('vermont' not in data.lower()) and ( 'san francisco' not in data.lower()) and ( 'connecticut' not in data.lower()) and ('canada' not in data.lower()) and ( 'pennsylvania' not in data.lower() ) and ( 'ottawa' not in data.lower()) and ( 'las vegas' not in data.lower()) and  ( 'nevada' not in data.lower()) and ( 'delaware' not in data.lower())  ):
            if( ('corona' in data.lower()) and ('OR' not in data) and ('MA' not in data) and ('ontario' not in data.lower()) and ('arizona' not in data.lower()) and ('NJ' not in ss) and ('VT' not in ss) and ( 'SF' not in ss) and ( 'CT' not in ss) and ('canada' not in data.lower()) and ( 'PA' not in ss )  and  ( 'NV' not in ss) and ( 'DE' not in ss  ) and ('ontario' not in data.lower()) and ('new jersey' not in data.lower()) and ('vermont' not in data.lower()) and ( 'san francisco' not in data.lower()) and ( 'connecticut' not in data.lower()) and ('canada' not in data.lower()) and ( 'pennsylvania' not in data.lower() ) and ( 'ottawa' not in data.lower()) and ( 'las vegas' not in data.lower()) and  ( 'nevada' not in data.lower()) and ( 'delaware' not in data.lower()) ):

                # ( 'mi' not in data.lower() ) and ('nj' not in data.lower())  and ( 'pa' not in data.lower()) and ( 'nv' not in data.lower())
                x=data.lower().find('location')
                y=data.lower().find('city')
                z=data.lower().find('corona')
                print(data[x:x+100])
                print(data[y:y+100])
                print(data[z:z+100])
                print("####################")
                print(data)
                print("----------------------------------------------------------")

                if(state in data[x:x+100] or state in data[y:y+100] ):
                    with open("tweets.txt",'a') as tf:
                        y = json.loads(data)
                        if 'extended_tweet' in data:
                            print("TEXT_FULL:::::::::::::: ",y["extended_tweet"]["full_text"])
                            tf.write(y["extended_tweet"]["full_text"]+"\n\n")
                        else:
                            print("TEXT:::::::::::::: ",y["text"])
                            tf.write(y["text"]+"\n\n")   

                # elif('CA' in data[x:x+100] or 'CA' in data[y:y+100] ):
                #     with open('CA_tweets.txt','a') as tf:
                #         y = json.loads(data)
                #         print("TEXT:::::::::::::: ",y["text"])
                #         tf.write(y["text"]+"\n\n")         
        
                return True
        except BaseException as e:
            print("Error on_data %s" % str(e))
        return True
          

    def on_error(self, status):
        print(status)

def run():
    # Authenticate using config.py and connect to Twitter Streaming API.
    global flag
    hash_tag_list = ["Corona"]
    fetched_tweets_filename = "tweets.txt"

    twitter_streamer = TwitterStreamer()
    # if(flag==1):
    #         return
    twitter_streamer.stream_tweets(fetched_tweets_filename, hash_tag_list)
    # if(flag==1):
    #         return

            
def ex():
    global flag
    time.sleep(600)
    #flag=1

 
if __name__ == '__main__':
    state=sys.argv[1]
    #state = input("Type NY or CA: ")


    t1=threading.Thread(name='daemon',target=run)
    t2=threading.Thread(target=ex) 

    t1.setDaemon(True)
    t1.start()
    t2.start()
    t2.join()   # thread 2 joined first. thread 1 executing still.

    #t1.join()
    #print("Heyyy")

    #t1.join()
