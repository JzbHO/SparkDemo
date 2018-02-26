#coding=UTF-8
import random
import time
url_paths=[
    "class/112.html",
    "class/113.html",
    "class/122.html",
    "class/132.html",
    "class/147.html",
    "learn/112.html"
]
ip_slices=[123,221,234,532,120,903.872,364,286,3,1,19,9]

http_referers=[
    "https://www.google.com/search?q={query}",
    "https://www.sogou.com/web?query={query}",
    "https://www.baidu.com/search?q={query}",
    "https://search.yahoo.com/search?p={query}"
]
search_keyword=[
    "Hadoop基础",
    "Strom实战",
    "Spark Streaming实战",
    "大数据面试"
]
status_codes=["200","404","500"]
def generator_refer():
    if random.uniform(0,1)<0.5:
        return "-"
    http=random.sample(http_referers,1)
    word=random.sample(search_keyword,1)
    return http[0].format(query=word[0])

def generator_code():
    status=random.sample(status_codes,1)
    return status[0]
def sample_url():
    return random.sample(url_paths,1)[0]

def generator_ip():
    ip_slice=random.sample(ip_slices,4)
    return ".".join([str(item) for item in ip_slice])


def generator_log (count=100):
    time_str=time.strftime("%Y-%m-%d %H:%M:%S",time.localtime())

    f=open("/home/spark/data/project/log/access.log","w+")

    while count>=1:
        query_log="{ip}\t{time}\t{url}\t{status}\t{refere}   ".format(url=sample_url(),ip=generator_ip(),refere=generator_refer(),status=generator_code(),time=time_str)
        print query_log
        f.write(query_log+"\n")
        count=count-1

if __name__=='__main__':
    generator_log()




