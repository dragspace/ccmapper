package com.ccmapper.demo;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 数据库表 转 JavaBean
 * 
 * @author 201508031514
 */
public class ReflectBeanUtils {

    private Connection connection;

    /* mysql url的连接字符串 */
    private static String url = "jdbc:mysql://127.0.0.1:3306/springmvcmybatisdemo?useUnicode=true&characterEncoding=utf-8";

    // 账号
    private static String user = "root";

    // 密码
    private static String password = "123456";

    // mysql jdbc的java包驱动字符串
    private String driverClassName = "com.mysql.jdbc.Driver";

    // 数据库的列名称
    private String[] colnames; // 列名数组

    // 列名类型数组
    private String[] colTypes;

    /**
     * 
     */
    public ReflectBeanUtils() {
        try {// 驱动注册
            Class.forName(driverClassName);
            if (connection == null || connection.isClosed()) {
                // 获得链接
                connection = DriverManager.getConnection(url, user, password);
            }
        }
        catch (ClassNotFoundException ex) {
            ex.printStackTrace();
            System.out.println("Oh,not");
        }
        catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Oh,not");
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    /**
     * 获取表中字段、字段类型
     * 
     * @param tableName 表名
     */
    public void doAction(String tableName) {
        String sql = "select * from " + tableName;
        try {
            PreparedStatement statement = connection.prepareStatement(sql);
            // 获取数据库的元数据
            ResultSetMetaData metadata = statement.getMetaData();
            // 数据库的字段个数
            int len = metadata.getColumnCount();
            // 字段名称
            colnames = new String[len + 1];
            // 字段类型 --->已经转化为java中的类型名称了
            colTypes = new String[len + 1];
            for (int i = 1; i <= len; i++) {
                System.out.println(metadata.getColumnName(i) + " : " + metadata.getColumnTypeName(i) + " : "
                        + sqlType2JavaType(metadata.getColumnTypeName(i).toLowerCase()) + " : " + metadata.getColumnDisplaySize(i) + " : "
                        + metadata.getColumnLabel(i));
                colnames[i] = metadata.getColumnName(i).toLowerCase(); // 获取字段名称
                colTypes[i] = sqlType2JavaType(metadata.getColumnTypeName(i)); // 获取字段类型
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * mysql的字段类型转化为java的类型
     * 
     * @param sqlType 数据库字段类型
     * @return String java属性类型
     */
    private String sqlType2JavaType(String sqlType) {
        if (sqlType.equalsIgnoreCase("bit")) {
            return "Boolean";
        } else if (sqlType.equalsIgnoreCase("tinyint")) {
            return "Integer"; // 对应java中的byte类型
        } else if (sqlType.equalsIgnoreCase("smallint")) {
            return "Short";
        } else if (sqlType.equalsIgnoreCase("int")) {
            return "Integer";
        } else if (sqlType.equalsIgnoreCase("bigint")) {
            return "Long";
        } else if (sqlType.equalsIgnoreCase("float")) {
            return "Float";
        } else if (sqlType.equalsIgnoreCase("decimal") || sqlType.equalsIgnoreCase("numeric") || sqlType.equalsIgnoreCase("real")
                || sqlType.equalsIgnoreCase("money") || sqlType.equalsIgnoreCase("smallmoney")) {
            return "BigDecimal";
        } else if (sqlType.equalsIgnoreCase("varchar") || sqlType.equalsIgnoreCase("char") || sqlType.equalsIgnoreCase("nvarchar")
                || sqlType.equalsIgnoreCase("nchar") || sqlType.equalsIgnoreCase("text")) {
            return "String";
        } else if (sqlType.equalsIgnoreCase("datetime") || sqlType.equalsIgnoreCase("date")) {
            return "Date";
        } else if (sqlType.equalsIgnoreCase("image")) {
            return "Blod";
        }

        return null;
    }

    /**
     * Description: 校验name和type是否合法
     * 
     * @param name 字段名
     * @param type 属性类型
     * @return boolean
     * @Author 201508031514 Create Date: 2016-2-25 上午09:47:21
     */
    public boolean check(String name, String type) {
        if ("".equals(name) || name == null || name.trim().length() == 0) {
            return false;
        }
        if ("".equals(type) || type == null || type.trim().length() == 0) {
            return false;
        }
        return true;

    }

    /**
     * Description: 获取属性字符串
     * 
     * @param name 字段名
     * @param type 属性类型
     * @return StringBuilder
     * @Author 201508031514 Create Date: 2016-2-25 上午09:46:19
     */
    public StringBuilder getAttrbuteString(String name, String type) {
        if (!check(name, type)) {
            System.out.println("类中有属性或者类型为空");
            return null;
        }
        if ("id".equals(name) ) {
            System.out.println("剔除字段'id' 的对应属性");
            return null;
        }
        String zhujie = "    @Column(name = \"" + name.toUpperCase() + "\")\n";
        String format = String.format("    private %s %s;\n\r", type , getTuoFeng(name, "0"));
        return new StringBuilder(zhujie).append(format);
    }

    /**
     * Description: 获取get方法字符串
     * 
     * @param name 字段名
     * @param type 属性类型
     * @return StringBuilder
     * @Author 201508031514 Create Date: 2016-2-25 上午09:48:05
     */
    private StringBuilder getGetMethodString(String name, String type) {
        if (!check(name, type)) {
            System.out.println("类中有属性或者类型为空");
            return null;
        }
        if ("id".equals(name) || "creator_id".equals(name) || "create_date".equals(name) || "update_user_id".equals(name)
                || "update_date".equals(name)) {
            System.out.println("剔除字段'id' 'creator_id' 'create_date' 'update_user_id' 'update_date'的对应属性get方法");
            return null;
        }
        String methodname = "get" + getTuoFeng(name, "1");
        name = getTuoFeng(name, "0");
        String format = String.format("    public %s %s(){\n", new Object[] { type, methodname });
        format += String.format("        return this.%s;\r\n", new Object[] { name });
        format += "    }\r\n";
        return new StringBuilder(format);
    }

    /**
     * Description: 获取字段的set方法字符串
     * 
     * @param name 字段名
     * @param type 属性类型
     * @return Object
     * @Author 201508031514 Create Date: 2016-2-25 上午09:49:03
     */
    private StringBuilder getSetMethodString(String name, String type) {
        if (!check(name, type)) {
            System.out.println("类中有属性或者类型为空");
            return null;
        }
        if ("id".equals(name) ) {
            System.out.println("剔除字段'id' 的对应属性set方法");
            return null;
        }
        String methodname = "set" + getTuoFeng(name, "1");
        name = getTuoFeng(name, "0");
        String format = String.format("    public void %s(%s %s){\n", new Object[] { methodname, type, name });
        format += String.format("        this.%s = %s;\r\n", new Object[] { name, name });
        format += "    }\r\n";
        return new StringBuilder(format);
    }

    /**
     * Description: 将名称首字符大写
     * 
     * @param name 字段名
     * @param type 0:生成属性 1：生成get set方法
     * @return String
     * @Author 201508031514 Create Date: 2016-2-25 上午09:48:30
     */
    private String getTuoFeng(String name, String type) {
        StringBuilder nameBuffer = new StringBuilder();
        String[] filedNames = name.trim().split("_");
        for (String filedName : filedNames) {
            if (filedName.length() > 1) {
                nameBuffer.append(filedName.substring(0, 1).toUpperCase()).append(filedName.substring(1));
            } else {
                nameBuffer.append(filedName.toUpperCase());
            }
        }
        if ("0".equals(type)) {// 首字母要小写
            return nameBuffer.toString().substring(0, 1).toLowerCase() + nameBuffer.toString().substring(1);
        }
        return nameBuffer.toString();
    }

    /**
     * Description:获取整个类的字符串并且输出为java文件
     * 
     * @param tableName 表名
     * @return StringBuilder
     * @Author 201508031514 Create Date: 2016-2-25 上午09:45:55
     */
    public StringBuilder getClassStr(String tableName) {
        StringBuilder strNameBuffer = new StringBuilder("");
        String[] classNames = tableName.toLowerCase().split("_");
        if (!tableName.toLowerCase().contains("_")) {
            strNameBuffer.append(classNames[0].substring(0, 1).toUpperCase()).append(classNames[0].substring(1));
        } else {
            int i = 0;
            for (String name : classNames) {
                if (i == 0 && name.length() == 1) {
                    continue;// 去掉表名的第一个前缀
                } else if (name.length() > 1) {
                    strNameBuffer.append(name.substring(0, 1).toUpperCase()).append(name.substring(1));
                } else {
                    strNameBuffer.append(name.toUpperCase());
                }
            }
        }
        strNameBuffer.append("Entity");

        // 输出的类字符串
        StringBuilder str = new StringBuilder("");
        // 获取表类型和表名的字段名
        this.doAction(tableName);
        if (null == colnames && null == colTypes) {
            return null;
        }
        // 拼接
        str.append("@Table(name=\"" + tableName + "\")\n");
        str.append("public class " + strNameBuffer.toString() + " extends BaseEntity {\r\n");
        // 拼接属性
        for (int index = 1; index < colnames.length; index++) {
            StringBuilder sbf = getAttrbuteString(colnames[index], colTypes[index]);
            if (sbf != null) {
                str.append(sbf);
            }
        }
        // 拼接get，Set方法
        for (int index = 1; index < colnames.length; index++) {
            StringBuilder sbfGet = getGetMethodString(colnames[index], colTypes[index]);
            StringBuilder sbfSet = getSetMethodString(colnames[index], colTypes[index]);
            if (sbfGet != null && sbfSet != null) {
                str.append(sbfGet);
                str.append(sbfSet);
            }
        }
        str.append("}\r\n");
        // 输出到文件中
        File file = new File(strNameBuffer.toString() + ".java");
        BufferedWriter write = null;

        try {
            write = new BufferedWriter(new FileWriter(file));
            write.write(str.toString());
            write.close();
        }
        catch (IOException e) {
            e.printStackTrace();
            if (write != null) {
                try {
                    write.close();
                }
                catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return str;
    }

    /**
     * @param args 参数
     */
    public static void main(String[] args) {
        String[] tables = { "DEMO" };
        ReflectBeanUtils bean = new ReflectBeanUtils();
        for (String table : tables) {
            System.out.println(bean.getClassStr(table));;
        }
    }

}