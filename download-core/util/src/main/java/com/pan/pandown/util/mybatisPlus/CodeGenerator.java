package com.pan.pandown.util.mybatisPlus;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.ArrayList;
import java.util.List;


/**
 * @author wenyao
 */
public class CodeGenerator {

    private final static AutoGenerator autoGenerator = new AutoGenerator();

    //自定义模式
    //0代表使用默认的项目结构目录,1代表自定义输出路径
    private static int flag = 1;
    //是否覆盖原文件
    private static boolean fileOverride = false;

    //模式1下的自定义的工程路径（当模式为0时，将会忽略，除了XML的自定义）
    private static String MYprojectPath = System.getProperty("user.dir");
    private static String MYCONTROLLER_PATH = MYprojectPath+"/web/src/main/java/com/pan/pandown/web/controller";
    private static String MYSERVICE_PATH = MYprojectPath+"/service/src/main/java/com/pan/pandown/service";
    private static String MYSERVICEIMPL_PATH = MYprojectPath+"/service/src/main/java/com/pan/pandown/service/Impl";
    private static String MYMAPPERXML_PATH = MYprojectPath+"/dao/src/main/resources/mapper";
    private static String MYMAPPERJAVA_PATH = MYprojectPath+"/dao/src/main/java/com/pan/pandown/dao/mapper";
    private static String MYENTITY_PATH = MYprojectPath+"/dao/src/main/java/com/pan/pandown/dao/entity";



    //表名映射(','号分割符)
    private static String INCLUDE_TABLES = "pandown_parse";

    //包名配置
    private static String CONTROLLER = "web.controller";
    private static String SERVICE = "service";
    private static String SERVICEIMPL = "service.Impl";
    private static String MAPPER = "dao.mapper";
    private static String ENTITY = "dao.entity";
    private static String projectPath = System.getProperty("user.dir");// 获取用户程序当前路径（项目根的路径）
    private static String ParentName = "com.pan.pandown";
    //private static String ModuleName = "";

    //公共全局
    private static String AUTHOR = "yalier";


    //数据源配置
    private static String DB_USERNAME = "root";
    private static String DB_PASSWORD = "A258258258";
    private static String DB_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static String DB_NAME = "pandown";
    private static String DB_URL = "jdbc:mysql://127.0.0.1:3306/{0}?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8".replace("{0}",DB_NAME);

    //模板文件路径
    private static String XML_TEMPLATEPATH = "/templates/mapper.xml.ftl";
    private static String MAPPER_TEMPLATEPATH = "/templates/mapper.java.ftl";
    private static String SERVICE_TEMPLATEPATH = "/templates/service.java.ftl";
    private static String SERVICEIMPL_TEMPLATEPATH = "/templates/serviceImpl.java.ftl";
    private static String ENTITY_TEMPLATEPATH = "/templates/entity.java.ftl";
    private static String CONTROLLER_TEMPLATEPATH = "/templates/controller.java.ftl";

    //配置
    private static GlobalConfig gc = new GlobalConfig();
    private static DataSourceConfig dsc = new DataSourceConfig();
    private static PackageConfig pc = new PackageConfig();
    private static InjectionConfig cfg = new InjectionConfig() {
        @Override
        public void initMap() {
            // to do nothing
        }
    };
    private static TemplateConfig templateConfig = new TemplateConfig();
    private static StrategyConfig strategy = new StrategyConfig();



    /**
     * 初始化全局配置
     */
    private static void initGlobalConfig(){
        // 全局配置
        if (flag==0)
            gc.setOutputDir(projectPath + "/src/main/java");// 生成文件的输出目录(默认值：D 盘根目录)

        gc.setAuthor(AUTHOR);// 开发人员(默认值：null)
        gc.setOpen(true);// 是否打开输出目录(默认值：null)
        gc.setFileOverride(fileOverride);// 是否覆盖已有文件(默认值：false)
        gc.setSwagger2(true);// 实体属性 Swagger2 注解
        gc.setBaseResultMap(true); //在xml中生成BaseResultMap
        gc.setBaseColumnList(true); //
        autoGenerator.setGlobalConfig(gc);//把全局配置添加到代码生成器主类
    }

    /**
     * 初始化数据源配置
     */
    private static void initDataSourceConfig(){
        // 数据源配置

        dsc.setUrl(DB_URL);
        //dsc.setSchemaName("public");// 数据库方案名
        dsc.setDbType(DbType.MYSQL);// 数据库类型
        dsc.setDriverName(DB_DRIVER);// 驱动名称
        dsc.setUsername(DB_USERNAME); // 用户名
        dsc.setPassword(DB_PASSWORD); // 密码
        autoGenerator.setDataSource(dsc);//把数据源配置添加到代码生成器主类
    }

    /**
     * 初始化包配置
     */
    private static void initPackageConfig(){
        // 包配置
        //pc.setModuleName(ModuleName);// 父包模块名
        pc.setParent(ParentName);// 父包名。如果为空，将下面子包名必须写全部， 否则就只需写子包名
        pc.setService(SERVICE);// Service包名
        pc.setEntity(ENTITY);// Entity包名
        pc.setServiceImpl(SERVICEIMPL);// ServiceImpl包名
        pc.setMapper(MAPPER);// Mapper包名
        pc.setController(CONTROLLER);// Contoller包名
        // pc.setXml("mapper.xml");// Mapper.xml包名(自定义配置到resource下，所以这里不使用默认路径)
        autoGenerator.setPackageInfo(pc);// 把包配置添加到代码生成器主类
    }

    /**
     * 初始化自定义配置，针对xml文件的
     * 这里配置生成的mapper.xml文件的自定义输出路径（默认不是输出到resources文件夹下，所以需要自定义）
     */
    private static List initInjectionConfigForXml(){
        // 自定义配置
        // 自定义输出配置，自定义配置会被优先输出
        List<FileOutConfig> focList = new ArrayList<>();
        focList.add(new FileOutConfig(XML_TEMPLATEPATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return MYMAPPERXML_PATH
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        autoGenerator.setCfg(cfg);
        return focList;
    }

    /**
     * 初始化自定义配置，针对java文件的
     * 这里配置生成的xxx.java文件的自定义输出路径
     * @return
     */
    private static List initInjectionConfigForJava(){
        List<FileOutConfig> focList = new ArrayList<>();

        //mapper.java的文件路径配置
        focList.add(new FileOutConfig(MAPPER_TEMPLATEPATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return MYMAPPERJAVA_PATH
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_JAVA;
            }
        });
        //service.java的文件路径配置
        focList.add(new FileOutConfig(SERVICE_TEMPLATEPATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return MYSERVICE_PATH
                        + "/I" + tableInfo.getEntityName() + "Service" + StringPool.DOT_JAVA;
            }
        });

        //serviceImpl.java的文件路径配置
        focList.add(new FileOutConfig(SERVICEIMPL_TEMPLATEPATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return MYSERVICEIMPL_PATH
                        + "/" + tableInfo.getEntityName() + "ServiceImpl" + StringPool.DOT_JAVA;
            }
        });

        //controller.java的文件路径配置
        focList.add(new FileOutConfig(CONTROLLER_TEMPLATEPATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return MYCONTROLLER_PATH
                        + "/" + tableInfo.getEntityName() + "Controller" + StringPool.DOT_JAVA;
            }
        });

        //entity.java
        focList.add(new FileOutConfig(ENTITY_TEMPLATEPATH) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return MYENTITY_PATH
                        + "/" + tableInfo.getEntityName() + StringPool.DOT_JAVA;
            }
        });
        cfg.setFileOutConfigList(focList);
        autoGenerator.setCfg(cfg);
        return focList;
    }

    /**
     * 初始化自定义配置,针对java文件的
     * 这里配置生成的所有文件的自定义输出路径
     * 包含mapper.xml, mapper.java, service.java, entity.java
     */
    private static void initAllInjectionConfig(){
        //自定义mapper.xml的输出路径
        List listXml = initInjectionConfigForXml();

        //自定义其他java的输出路径
        List listJava = initInjectionConfigForJava();

        //同时配置xml和java
        listJava.addAll(listXml);
        cfg.setFileOutConfigList(listJava);
        autoGenerator.setCfg(cfg);
    }


    /**
     * 初始化模板配置
     */
    private static void initTemplateConfig(){
        // 配置模板
        templateConfig.setXml(null);
        autoGenerator.setTemplate(templateConfig);
    }

    /**
     * 生成策略初始化
     */
    private static void initStrategyConfig(){
        // 策略配置

        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        // strategy.setSuperEntityClass("你自己的父类实体,没有就不用设置!");
        strategy.setEntityLombokModel(true);
        strategy.setRestControllerStyle(true);
        // 公共父类
        // strategy.setSuperControllerClass("你自己的父类控制器,没有就不用设置!");
        // 写于父类中的公共字段
        // strategy.setSuperEntityColumns("id");
        strategy.setInclude(INCLUDE_TABLES.split(","));
        strategy.setControllerMappingHyphenStyle(true);
        //  strategy.setTablePrefix(pc.getModuleName() + "_");
        autoGenerator.setStrategy(strategy);
        autoGenerator.setTemplateEngine(new FreemarkerTemplateEngine());
    }

    public static void init(){
        initGlobalConfig();
        initDataSourceConfig();
        initPackageConfig();
        if (flag == 0) {
            initInjectionConfigForXml();
        }
        else {
            initAllInjectionConfig();
        }
        initTemplateConfig();
        initStrategyConfig();
    }

    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {
        init();
        autoGenerator.execute();
    }

}
