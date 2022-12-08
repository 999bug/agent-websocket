# i2agent



## 快速入门
### windows
1、解压 package 目录下面 XXX-windows.zip

2、**首次启动需要生成配置文件，二次启动不需要执行此命令**

``
启动 generateConfig.bat
``

**示例**
```bash
使用指南：[]内为默认值，未填写新值时自动使用默认值
请设置代理服务用户名: user
请设置代理服务密码: 123@$ind)8[];
请输入DRM服务地址：[127.0.0.1]:192.168.46.11
请输入DRM服务端口：[8080]:8080
请输入连接DRM重试间隔，单位(秒)[30]:35
正在保存：agent.conf
配置文件：agent.conf 生成成功
```
3、启动 startAgent.bat 

4、查看日志，log目录下面，会按照每天进行切割记录
### linux
1、解压 package 目录下面 XXX-linux.tar.gz

``
tar -zxvf XXX-linux.tar.gz
``

2、 进入XXX-linux

``cd XXX-linux``

3、**首次启动需要生成配置文件，二次启动不需要执行此命令**

``
./start.sh configure
``

**示例**
````bash
[root@curry agent-linux]# ./start.sh configure
使用指南：[]内为默认值，未填写新值时自动使用默认值
请设置代理服务用户名: user
请设置代理服务密码: 123@$ind)8[];
请输入DRM服务地址：[127.0.0.1]:192.168.46.11
请输入DRM服务端口：[8080]:8080
请输入连接DRM重试间隔，单位(秒)[30]:35
正在保存：agent.conf
配置文件：agent.conf 生成成功
````
4、启动程序

``
./start.sh start 
``

5、查看日志，会按照每天进行切割记录
```bash
 tailf -n 200 log/agent-server.log
```
<hr>

## 替换jar包
`将修改的 jar 包放入 agent 目录下即可`

