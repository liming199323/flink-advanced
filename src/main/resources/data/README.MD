# 测试数据集

# 目录 words 数据集说明
模拟一片文章为.txt文件，对其中单词进行一些频次分析
>参考 `io.gourd.flink.scala.data.Words`
# 目录 game 数据集说明
模拟游戏业务数据
>参考 `io.gourd.flink.scala.data.Games`
>user 表示用户，role 表示角色。一个用户拥有多个角色。

**表说明**

|表名|描述|字段|
|---|---|---|
|role-login|角色登录|platform,channel,region,server,uid,rid,data_unix,status|
|role-order|角色充值|platform,channel,region,server,uid,rid,data_unix,payWay,orderId,valid,money|
|role-shop|角色商城消费|platform,channel,region,server,uid,rid,data_unix,mallId,itemId,currencyType,num,singleMoney,totalMoney|
|user-login|用户登录|platform,channel,region,server,uid,data_unix,status|

**字段说明**

|字段|说明|
|---|---|
|platform  |平台|
|channel   |渠道|
|region    |游戏大区|
|server    |游戏服|
|uid       |用户唯一ID|
|rid       |角色唯一ID|
|dataUnix |事件发生时间(秒)|
|payWay     |支付方式|
|orderId    |订单ID|
|valid      |验证结果|
|money      |金额|
|status    |状态（登录、登出）|
|mallId   |商城ID|
|itemId   |道具ID|
|currencyType |游戏虚拟币类型|
|num           |购买数量|
|singleMoney  |单价|
|totalMoney   |总支付|
