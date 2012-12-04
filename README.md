#!/bin/sh

exec /share/vim/vim73/vim "$@"

根据face++ api 自己简单封装一个android sdk 并附上demo；
同时加入了json解析模块和对返回的json数据进行了一层自定义封装；
加入异常信息提示；

如果想在java环境下使用，摘出src/com.konka.facerecognition/api/下的文件即可，主要是ApiHandle类和HttpUtils类,
json解析还需导入json的相关jar包。