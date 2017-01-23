# ccmapper
> - 用来自定义通用mapper的工具集(可以自己定义通用mapper的sql方法) 注：  参考了 mapper插件的API。。。 。。
> - 原理      动态生成泛型子接口，并注入到spring中
> - 核心只有2个类。。。MapperDynamicUtils和泛型辅助类GenericUtils
> - 核心方法   (方法返回自动生成接口的beanname)
```
String MapperDynamicUtils.registerCommonMapper(beanClazz, registry, commonMapperClazz<T>, SqlProviderClass<? extend AbstractSqlProvider>， String sqlSessionFactoryBeanName);
```


## 自定义commonMapper 
> - core 核心包 
> - 3个custom包   扩展查询包
> > - custom bean属性和字段一致 bean名就是表明 
> > - customanno javax.persistence  注解映射
> > - customsimple  最简单的例子  

> - demo 样例bean包

## 限制 
> - 返回值类型目前只支持T 和Map， 注意泛型只能指定为T
## 测试看test包

##目前集成
> - 扫描指定包bean 工具
> - persistence注解工具类
> - 增删改查样例
> - 动态查询

## 后续持续改进
> - 简单多表关联
	
##使用举例

```
@Autowired
private CustomAnnoMapper<UserAndOrg> userAndOrgCustomAnnoMapper;
@Autowired
private CustomAnnoMapper<OrgAnno> orgAnnoCustomAnnoMapper;

```    
##说明
> - 内部会自动生成CustomAnnoMapper  2个接口，一个是UserAndOrg的泛型接口，一个是OrgAnno的泛型接口， 这样依赖spring的泛型注入就可以使用了**
> - CustomAnnoMapper的方法可以任意定制
> - 返回值只能是  
> > 泛型 T, 
> > - List<T>
> > - void
> > - Map<String, Object>
> > - List<Map<String, Object>>
