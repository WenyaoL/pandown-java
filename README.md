# pandown-java

### :bookmark:介绍

pandown-java是基于vue + spring boot简易的前后端分离项目，**本项目仅供学习参考，严禁商业用途**。

#### 前端

该项目主要有两个部分，一个是前端模块的`download-ui`，`download-ui`使用的是`vue3`+`element-plus`+`Ts`实现，使用的前端模板是`vue-admin-template`，因为`vue-admin-template`使用的是`vue2`+`element-ui`+`js`，所以这里稍微进行了修改，使其升级为`Vue3`+`element-plus`+`Ts`。

前端分享文件的获取主要依赖于后端，而下载功能主要依赖于Aria2下载工具，将链接发送给Aria2工具，利用Aria2工具进行下载。



#### 后端

后端模块是`download-core`，后端使用`spring boot`+`mybatis`，主要使用`restTemplate`来进行网络请求，登录使用的是`spring boot security`+`jwt`，通过jwt实现登录认证。

通过百度网盘的接口，获取网盘文件信息,处理后显示在网页中。通过api接口以及SVIP账号的Cookie(BDUSS)获取高速下载链接。本质就是用会员账号获取下载地址并发送给访客。

**项目中所涉及的接口均来自[百度官方](https://pan.baidu.com/union)，不涉及任何违法行为，本工具需要使用自己的百度网盘SVIP账号才能获取下载链接，代码全部开源，仅供学习参考；本工具使用的都是百度网盘的API，无任何非法加速接口。请不要将此项目用于商业用途，否则可能带来严重的后果**



### Aria2

> aria2 is a utility for downloading files. The supported protocols are HTTP(S), FTP, SFTP, BitTorrent, and Metalink. aria2 can download a file from multiple sources/protocols and tries to utilize your maximum download bandwidth. It supports downloading a file from HTTP(S)/FTP/SFTP and BitTorrent at the same time, while the data downloaded from HTTP(S)/FTP/SFTP is uploaded to the BitTorrent swarm. Using Metalink's chunk checksums, aria2 automatically validates chunks of data while downloading a file like BitTorrent.
>
> The project page is located at https://aria2.github.io/.
>
> See [aria2 Online Manual](https://aria2.github.io/manual/en/html/) ([Russian translation](https://aria2.github.io/manual/ru/html/), [Portuguese translation](https://aria2.github.io/manual/pt/html/)) to learn how to use aria2.

Aria2 Github地址：[aria2/aria2: aria2 is a lightweight multi-protocol & multi-source, cross platform download utility operated in command-line. It supports HTTP/HTTPS, FTP, SFTP, BitTorrent and Metalink. (github.com)](https://github.com/aria2/aria2)



### :pear:使用提示

- 本项目使用的接口容易导致账号限速，不建议做公益解析，只建议跟你的朋友分享你的SVIP账号时使用。
- 需要配置 `完整 Cookie`(普通账号和SVIP账号均可) + `BDUSS, STOKEN`(需SVIP账号) 才可以获取下载链接，获取方法需抓包。
- 任何邪恶 终将绳之以法！！！请勿做违法行为。





### 搭建教程

#### 数据导入

数据库的表在`download-data`目录下面，数据库用的是`mysql8`，项目自带一个admin账号，账号邮箱为`admin@pan.com`，密码为`123456`，搭建成功后切记在数据库中修改admin账号的密码。



#### 后端搭建

后端为`spring-boot`项目，直接使用maven打包即可。进入`download-core`目录，运行一下命令

```
mvn clean package -Dmaven.test.skip=true
```



#### 前端搭建

前端为`Vue3`项目，使用`npm`打包。进入`download-ui`目录，运行一下命令

```
npm run build
```



### LICENSE

[MIT](./LICENSE)

### :pill:Thanks

[yuantuo666/baiduwp-php: A tool to get the download link of the Baidu netdisk / 一个获取百度网盘分享链接下载地址的工具 (github.com)](https://github.com/yuantuo666/baiduwp-php)



