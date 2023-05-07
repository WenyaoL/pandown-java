# pandown-java

### :bookmark:介绍

pandown-java是基于vue + spring boot简易的前后端分离项目，**本项目仅供学习参考，严禁商业用途**。

#### 前端

该项目主要有两个部分，一个是前端模块的`download-ui`，`download-ui`使用的是`vue3`+`element-plus`+`Ts`实现，使用的前端模板是`vue-admin-template`，因为`vue-admin-template`使用的是`vue2`+`element-ui`+`js`，所以这里稍微进行了修改。

前端分享文件的获取主要依赖于后端，而下载功能主要依赖于Aria2下载工具，将链接发送给Aria2工具，利用Aria2工具进行下载。

#### 后端

后端模块是`download-core`，后端使用`spring boot`+`mybatis`，主要使用`restTemplate`来进行网络请求，登录使用的是`spring boot security`+`jwt`，通过jwt实现登录认证。

通过百度网盘的接口，获取网盘文件信息,处理后显示在网页中。通过api接口以及SVIP账号的Cookie(BDUSS)获取高速下载链接。本质就是用会员账号获取下载地址并发送给访客。

**项目中所涉及的接口均来自[百度官方](https://pan.baidu.com/union)，不涉及任何违法行为，本工具需要使用自己的百度网盘SVIP账号才能获取下载链接，代码全部开源，仅供学习参考；本工具使用的都是百度网盘的API，无任何非法加速接口。请不要将此项目用于商业用途，否则可能带来严重的后果**



### :pear:使用提示

- 本项目使用的接口容易导致账号限速，不建议做公益解析，只建议跟你的朋友分享你的SVIP账号时使用。
- 需要配置 `完整 Cookie`(普通账号和SVIP账号均可) + `BDUSS, STOKEN`(需SVIP账号) 才可以获取下载链接，获取方法需抓包。
- 任何邪恶 终将绳之以法！！！请勿做违法行为。



### LICENSE

[MIT](./LICENSE)

### :pill:Thanks

[yuantuo666/baiduwp-php: A tool to get the download link of the Baidu netdisk / 一个获取百度网盘分享链接下载地址的工具 (github.com)](https://github.com/yuantuo666/baiduwp-php)



