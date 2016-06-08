package com.ccmapper.core.example;


public enum SqlSign{
	
	IsNull(" is null "), IsNotNull(" is not null"), EqualTo(" ="), NotEqualTo(" <>"),
	GreaterThan(" >"),GreaterThanOrEqualTo(" >="), LessThan(" <"), LessThanOrEqualTo(" <="),
	In(" in"), NotIn(" not in"),Like(" like"), NotLike(" not like"),
	Between(" between", ExampleConstant.DEFAULT_BETWEEN_GENERATESQL), NotBetween(" not between", ExampleConstant.DEFAULT_BETWEEN_GENERATESQL);
	
	protected String sign;
	private GenerateSql generateSql;
	
	SqlSign(String sign){
		this(sign, null);
	}
	
	SqlSign(String sign, GenerateSql generateSql){
		this.sign = sign;
		if(generateSql == null){
			this.generateSql = ExampleConstant.DEFAULT_GENERATESQL;
		}else{
			this.generateSql = generateSql;
		}
	}
	
	public String getCondition(String columnName, int index){
		return generateSql.generate(this, columnName, index);
	}
	
	public String toString(){
		return this.sign;
	}
}
