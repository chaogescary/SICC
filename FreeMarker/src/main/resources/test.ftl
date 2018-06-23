<html>
<head>
	<meta charset="utf-8">
	<title>Freemarker入门小DEMO </title>
</head>
<body>
	<#-- 我只是一个注释，我不会有任何输出 -->
	<#-- 包含指令: -->
	<#include "head.ftl">
	</br>
	
	<#-- if指令 -->
	<#if success=true>
	  你已通过实名认证
	<#else>  
	  你未通过实名认证
	</#if>
	</br>
	
	<#-- if指令加空值判断符??判断变量是否存在 -->
	<#if abc??>
  	空值判断符:abc变量存在
	<#else>
  	空值判断符:abc变量不存在
	</#if>
	</br>
	
	<#-- 空值处理符!'',判断变量是否存在 -->
  	${abc!'空值处理符:abc变量不存在'}
	</br>
	
	<#-- 常规变量，须使用程序进行值的替换 -->
	${name},你好。
	${message}
	</br>
	
	<#-- assign指令:直接在ftl里面定义变量 -->
	<#assign linkman="李四">
	${linkman}:管理员
	</br>
	
	<#-- list指令:list变量或者变量别名加上“_index即可得到索引” -->
	<#list goodsList as goods>
  	${goods_index+1} 商品名称： ${goods.name} 价格：${goods.price}<br>
	</#list>
	</br>
	
	<#-- 内建函数?eval:用于字符串转换 -->
	<#assign text="{'bank':'工商银行','account':'10101920201920212'}" />
	<#assign data=text?eval />
	开户行：${data.bank}  账号：${data.account}
	</br>
</body>
</html>