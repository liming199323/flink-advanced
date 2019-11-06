# 测试数据集

# 目录 words 数据集说明
模拟一篇文章为.txt文件，对其中单词进行一些频次分析
>参考 `io.gourd.flink.scala.data.Words`
# 目录 game 数据集说明
模拟游戏业务数据
>参考 `io.gourd.flink.scala.games.data` 内容，提供模拟数据集模型定义及数据载入工具方法
>以角色扮演游戏举例，用户登录账户后，可以选择不同的角色进行游戏，一个用户拥有多个角色
>UserXXX 表示用户事件，RoleXXX 表示角色事件。

**表说明**
所有表默认包含字段`platform,server,dataUnix`

|表名|描述|pojo定义，字段类型含义参考注释|
|---|---|---|
|UserLogin|用户登录|[UserLogin](/src/main/scala/io/gourd/flink/scala/games/data/pojo/UserLogin.scala)|
|RoleLogin|角色登录|[RoleLogin](/src/main/scala/io/gourd/flink/scala/games/data/pojo/RoleLogin.scala) |
|RolePay  |角色充值|[RolePay](/src/main/scala/io/gourd/flink/scala/games/data/pojo/RolePay.scala)|
|RoleShop |角色商城消费|[RoleShop](/src/main/scala/io/gourd/flink/scala/games/data/pojo/RoleShop.scala)|



