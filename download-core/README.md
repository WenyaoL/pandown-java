# pandown-java后端模块

## 介绍

该后端模块开发使用的是`spring-boot`，安全登录框架使用是`spring security`。并且支持简易的权限控制。

## Controller介绍

controller全是RestController，有以下controller接口：

### PandownUserController

#### 路径

基础路径为**/user**

| 请求路径（不包含基础路径） | 功能                                    | 参数                  | 返回                           | 权限   |
| -------------------------- | --------------------------------------- | --------------------- | ------------------------------ | ------ |
| /register                  | 用户注册接口                            | UserRegisterDTO       | 提示字符串                     | 无     |
| /login                     | 用户登录接口                            | UserLoginDTO          | 返回一个{token:"asdfasdfasdf"} | 无     |
| /logout                    | 用户登出接口(消耗Redis中的登录用户详情) | 无(需要请求头的token) | 提示字符串                     | 认证后 |
| /info                      | 用户基础信息接口                        | 无(需要请求头的token) | UserInfoPO                     | 认证后 |
| /postCaptcha               | 发送注册邮箱验证码(注册)                | {email:"xxx@xxx"}     | 验证码字符串                   | 无     |
| /postCaptchaByForgetPwd    | 发送邮箱验证码(忘记密码)                | {email:"xxx@xxx"}     | 验证码字符串                   | 无     |
| /resetPassword             | 重置密码接口                            | UserRegisterDTO       | 提示字符串                     | 无     |



#### 相关POJO

DTO对象`UserRegisterDTO`

```java
{
	private String username;

    @NotBlank(message = "密码不能为空")
    private String password;

    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotBlank(message = "邮箱验证码不能为空")
    private String captcha;
}
```

DTO对象`UserLoginDTO`

```java
{
	@NotBlank(message = "邮箱不能为空")
    private String email;

    @NotBlank(message = "密码不能为空")
    private String password;
}
```





### PandownUserFlowController 

用于用户流量查询

#### 路径

基础路径为**/api/pandown-user-flow**

| 请求路径（不包含基础路径） | 功能                 | 参数                                                  | 返回                      | 权限   |
| -------------------------- | -------------------- | ----------------------------------------------------- | ------------------------- | :----- |
| /getUserFlowInfo           | 获取登录用户流量信息 | 无(自动获取用户的登录Token，根据登录用户获取流量信息) | PandownUserFlow实体类对象 | 认证后 |

#### 相关POJO

实例类对象`PandownUserFlow`

```
{
	id:123456, //用户id
	parseNum:0, //解析文件数
	parseFlow:0, //解析流量
	limitFlow:500, //限额流量(剩余流量，单位字节)
    state:1 //流量状态，1可以，0流量超标，-1冻结
}
```



### DbtableCommonAccountController

#### 路径

基础路径为**/api/dbtable-common-account**

| 请求路径（不包含基础路径） | 功能             | 参数                 | 返回                         | 权限  |
| -------------------------- | ---------------- | -------------------- | ---------------------------- | ----- |
| /getAccountNum             | 获取普通账号数量 | 无                   | `CommonAccountNumDTO`        | admin |
| /getAccountDetail          | 获取普通账号信息 | 无                   | `List<DbtableCommonAccount>` | admin |
| /deleteAccount             | 删除普通账号     | DbtableCommonAccount | 无                           | admin |
| /addAccount                | 添加普通账号     | DbtableCommonAccount | 无                           | admin |
| /updateAccount             | 跟新普通账号     | DbtableCommonAccount | 无                           | admin |

#### 相关POJO

CommonAccountNumDTO

```java
{
	private Integer accountNum;
    private Long availableAccountNum;
}
```

DbtableCommonAccount

```java
{

    private Long id;

    @ApiModelProperty(value = "普通账号的用户名")
    private String name;

    @ApiModelProperty(value = "普通账号的cookies字符串")
    private String cookie;

    @ApiModelProperty(value = "账号状态")
    private Integer state;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
```





### DbtableSvipController

#### 路径

基础路径为**/api/dbtableSvip**

| 请求路径（不包含基础路径） | 功能                 | 参数        | 返回                | 权限  |
| -------------------------- | -------------------- | ----------- | ------------------- | ----- |
| /getAccountNum             | 获取svip账号数量信息 | 无          | SvipAccountNumDTO   | admin |
| /getAccountDetail          | 获取svip账号信息     | 无          | `List<DbtableSvip>` | admin |
| /deleteAccount             | 删除svip账号         | DbtableSvip | 无                  | admin |
| /addAccount                | 添加svip账号         | DbtableSvip | DbtableSvip         | admin |
| /updateAccount             | 更新svip账号         | DbtableSvip | 无                  | admin |



#### 相关POJO

SvipAccountNumDTO

```java
{
	private Integer accountNum;
    private Long availableAccountNum;
}
```

DbtableSvip

```java
{
	@ApiModelProperty(value = "id(雪花id)")
    private Long id;

    @ApiModelProperty(value = "账号名称")
    private String name;

    @ApiModelProperty(value = "会员bduss")
    private String svipBduss;

    @ApiModelProperty(value = "会员stoken")
    private String svipStoken;

    @ApiModelProperty(value = "会员状态(0:正常,-1:限速)")
    private Integer state;

    @ApiModelProperty(value = "会员类型(1:普通会员,2:超级会员,3:??会员)")
    private Integer vipType;

    @ApiModelProperty(value = "会员账号加入时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "是否正在使用(非零表示真)")
    private LocalDateTime updateTime;
}
```



### DownloadApiController

#### 路径

基础路径为**/api/download**

| 请求路径（不包含基础路径） | 功能                   | 参数                | 返回                 | 权限       |
| -------------------------- | ---------------------- | ------------------- | -------------------- | ---------- |
| /list_all_files            | 获取分享的文件树结构   | ListFileDTO         | List                 | user,admin |
| /list_dir                  | 解析分享目录           | ListShareDirDTO     | Map                  | user,admin |
| /getSignAndTime            | 获取sign和时间搓       | ListFileDTO         | SignAndTimeDTO       | user,admin |
| /getDlink                  | 获取普通连接           | GetDlinkDTO         | {list:[]}            | user,admin |
| /getSvipDlink              | 获取直链(只能解析文件) | GetSvipDlinkDTO     | `List<ShareFileDTO>` | user,admin |
| /getAllSvipDlink           | 获取直链(能分析目录)   | ListAllSvipDlinkDTO | `List<ShareFileDTO>` | user,admin |



#### 相关POJO

ListFileDTO

```java
{
	private String surl;
    private String pwd;
}
```

ListShareDirDTO

```java
{
	private String surl;

    private String pwd;

    private String dir = "";

    private Integer page = 0;
}
```

GetDlinkDTO

```java
{
	private String sign;

    private Long timestamp;

    private String seckey;

    private ArrayList fsIdList;

    private String shareid;

    private String uk;
}
```

GetSvipDlinkDTO

```java
{
	private List<ShareFileDTO> shareFileList;

    private String sign;

    private Long timestamp;

    private String seckey;

    private String shareid;

    private String uk;
}
```

ShareFileDTO

```java
{
	@ApiModelProperty(value = "文件id")
    private String fs_id;

    @ApiModelProperty(value = "是否为文件夹")
    private int isdir;

    @ApiModelProperty(value = "md5")
    private String md5;

    @ApiModelProperty(value = "文件路径")
    private String path;

    @ApiModelProperty(value = "文件大小")
    private Long size;

    @ApiModelProperty(value = "文件名")
    private String server_filename;

    @ApiModelProperty(value = "文件普通下载链接")
    private String dlink;

    @ApiModelProperty(value = "文件svip下载链接")
    private String svipDlink;
}
```

ListAllSvipDlinkDTO

```java
{
	private String surl;

    private String pwd;

    private String uk;

    private String shareid;

    private String seckey;

    private List<ShareFileDTO> shareFileList;
}
```

SignAndTimeDTO

```java
{
	@ApiModelProperty(value = "签名")
    private String sign;

    @ApiModelProperty(value = "时间搓")
    private Long timestamp;
}
```



### PandownParseController

#### 路径

基础路径为**/api/pandownParse**

| 请求路径（不包含基础路径） | 功能                 | 参数                  | 返回                  | 权限       |
| -------------------------- | -------------------- | --------------------- | --------------------- | ---------- |
| /userHistory               | 获取用户解析历史记录 | BasePageDTO           | `IPage<PandownParse>` | user,admin |
| /deleteUserParseHistory    | 删除用户解析历史记录 | DeleteParseHistoryDTO | 无                    | user,admin |

#### 相关POJO

BasePageDTO

```java
{
    @NotNull(message = "页码不可为空")
    private Long pageNum;

    @NotNull(message = "页大小不可为空")
    private Long pageSize;
}
```

PandownParse

```java
{
    private Integer id;

    @ApiModelProperty(value = "用户ip")
    private Long userId;

    @ApiModelProperty(value = "文件名")
    private String filename;

    @ApiModelProperty(value = "文件大小")
    private Long size;

    @ApiModelProperty(value = "文件效验码")
    private String md5;

    @ApiModelProperty(value = "文件路径")
    private String path;

    @ApiModelProperty(value = "文件创建时间")
    private String serverCtime;

    @ApiModelProperty(value = "文件下载地址")
    private String reallink;

    @ApiModelProperty(value = "解析账号id")
    private Long parseAccountId;

    @ApiModelProperty(value = "解析时间")
    private LocalDateTime createTime;
}
```

DeleteParseHistoryDTO

```java
{
	private List deleteIds;
}
```





## 登录认证模块

#### 介绍

登录认证使用的是`spring security`，使用的是密码邮箱登陆认证，登录成功后，讲会返回`JWT`,用于后面的认证授权。

登录成功后，后面的请求应该都在请求头中携带token字段，只有拥有合法的token才能访问相应的后端接口。

登录后的请求，都将会被自定义的JwtAuthenticationFilter拦截，在JwtAuthenticationFilter中，将会在请求头中截获token字段，并对字段解析，通过从`JWT`（token）中提取信息和Redis中的用户登录信息进行对应，然后使用Redis中的用户登录信息进行认证和授权。根据授权信息来决定，请求是否有权请求对应的`URL`



#### 登录功能

自定义登录接口在`/user/login`

认证核心逻辑在`PandownUserServiceImpl`服务类的`loadUserByUsername`方法中。（查数据库，权限注入）

登录成功后，使用tokenService生成JWT，并且在Redis中生成用户的登录信息

#### JWT认证

只有使用邮箱和密码登录后才有JWT。

在请求过来后，自定义的`JwtAuthenticationFilter`将会拦截请求，并对请求头中token字段进行解析。

解析token（JWT），并通过解析后的信息和Redis中的用户登录信息进行关联。

使用Redis中的用户登录信息进行认证和权限填充。

#### 权限管理

权限管理在数据库表`pandown_permission`和`pandown_role_permission`和`pandown_role`中管理。这三个表使权限和角色进行关联。

通过在`pandown_permission`中添加权限规则

通过在`pandown_role`中添加角色

通过`pandown_role_permission`进行角色和权限进行关联

通过`pandown_user_role`进行用户和角色进行关联

#### 相关服务

##### TokenService

`TokenService`包含`JWTService`和`RedisService`

TokenService提供JWT生成并在Redis生成用户登录信息功能，提供JWT解析和Redis中的用户登录信息关联功能等

##### JWTService

JWTService是使用`auth0`进行JWT生成的。

JWTService提供最简单的JWT生成。
