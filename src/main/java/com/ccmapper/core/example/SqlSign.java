package com.ccmapper.core.example;


public enum SqlSign{
	
	IsNull(" is null ", ExampleConstant.NOVALUE_GENERATESQL), 
	IsNotNull(" is not null ", ExampleConstant.NOVALUE_GENERATESQL), 
	EqualTo(" = "), 
	NotEqualTo(" <> "),
	GreaterThan(" > "),
	GreaterThanOrEqualTo(" >= "), 
	LessThan(" < "), 
	LessThanOrEqualTo(" <= "),
	In(" in ", ExampleConstant.IN_GENERATESQL), 
	NotIn(" not in ", ExampleConstant.IN_GENERATESQL),
	Like(" like "), 
	NotLike(" not like "),
	Between(" between ", ExampleConstant.BETWEEN_GENERATESQL), 
	NotBetween(" not between ", ExampleConstant.BETWEEN_GENERATESQL);
	
	protected String sign;
	private GenerateSql generateSql;
	
	SqlSign(String sign){
		this(sign, null);
	}
	
	SqlSign(String sign, GenerateSql generateSql){
		this.sign = sign;
		if(generateSql == null){
			this.generateSql = ExampleConstant.COMMON_GENERATESQL;
		}else{
			this.generateSql = generateSql;
		}
	}
	
	public String getCondition(String columnName, int index, int valuesSize){
		return generateSql.generate(this, columnName, index, valuesSize);
	}
	
	public String toString(){
		return this.sign;
	}
}
