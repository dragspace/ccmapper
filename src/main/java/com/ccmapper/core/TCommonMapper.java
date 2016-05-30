package com.ccmapper.core;

import java.util.List;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;

import com.ccmapper.custom.CommonDynamicMapperProvider;
import com.ccmapper.custom.CommonMapper;

/**
 * @Description: TCommonMapper 
 * 这个类属于泛型模板类 修改CommonMapper类 
 * 必须同步修改这个类 直接自动实现生成就可以了
 * 主要是为了方便提取泛型信息。。。
 * @author xiaoruihu 2016年5月26日 下午2:57:53
 */
public interface TCommonMapper extends CommonMapper<TClass> {

	@Override
	@InsertProvider(type = CommonDynamicMapperProvider.class, method = "insert")
	public void insert(TClass t);

	@Override
	@UpdateProvider(type = CommonDynamicMapperProvider.class, method = "updateByPrimaryKey")
	public void update(TClass t);

	@Override
	@UpdateProvider(type = CommonDynamicMapperProvider.class, method = "updateNotNullByPrimaryKey")
	public void updateNotNull(TClass t);

	@Override
	@SelectProvider(type = Object.class, method = "selectByPrimaryKey")
	public TClass getByPrimaryKey(Object key);

	@Override
	@SelectProvider(type = Object.class, method = "selectByPropertyEqual")
	public TClass getByPropertyEqual(String property, Object value);
	
	@Override
	@SelectProvider(type = CommonDynamicMapperProvider.class, method = "selectByPropertyEqual")
	public List<TClass> getListByPropertyEqual(String property, Object value);

}
