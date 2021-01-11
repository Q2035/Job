1.MyBaits resultMap 属性顺序由严格的规定
```text
The content of element type "resultMap" must match "(constructor?,id*,result*,association*,collection*,discriminator?)
```
2.MyBaits EnumTypeHandler 无法找到常量
```text
Error attempting to get column 'account_status' from result set.  Cause: java.lang.IllegalArgumentException: No enum constant top.hellooooo.job.pojo.AccountStatus.0
```
解决方案：https://segmentfault.com/a/1190000019168232

