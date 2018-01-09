
master merge from app.
app merge from service.
service merge from cliser.

cliser -> service -> app -> master

cliser分支:     data module用于存共用的bean和constants；
                client module为客户端api；
                server为 module服务端api；
                (data、client、server都是java库)

service分支:    service module为基于server提供的service

app分支:        ui界面开发

master分支:     主分支，主要用来发布版本    

=================================v0.8.1==================================

2018.01.09
1. 在分支service中，添加开机启动权限。

=================================v0.8.0==================================

2018.01.08
1. 完成了分支cliser、service的基本功能。
2. 完成了分支app的扫描机器人功能。
