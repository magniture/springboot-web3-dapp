package com.dapp.springbootweb3dapp.access.config.datasource;

import com.alibaba.druid.filter.Filter;
import com.alibaba.druid.filter.logging.Slf4jLogFilter;
import com.alibaba.druid.filter.stat.StatFilter;
import com.alibaba.druid.pool.DruidDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * druid 配置属性
 *
 * @author ruoyi
 */
@Configuration
public class DruidProperties {

    private static final String href_xy = "jdbc:mysql://";

    private static final String href_host = "127.0.0.1:3306";

    // 数据库名称
    private static final String href_db = "web3-seeker-study";

    private static final String href_str = "serverTimezone=Asia/Shanghai&characterEncoding=utf8&nullCatalogMeansCurrent=true&useUnicode=true&zeroDateTimeBehavior=convertToNull&connectTimeout=10000&socketTimeout=10000";


    public static String url = href_xy + href_host + "/" + href_db + "?" + href_str;

    // 数据库账号密码
    public static String userName = "test";
    public static String passWord = "test123456";


//    @Value("${spring.datasource.druid.initialSize}")
    /**
     * 初始连接数
     */
    private int initialSize = 5;

//    @Value("${spring.datasource.druid.minIdle}")
    /**
     * 最小连接池数量
     */
    private int minIdle = 5;

//    @Value("${spring.datasource.druid.maxActive}")
    /**
     * 最大连接池数量
     */
    private int maxActive = 20;

//    @Value("${spring.datasource.druid.maxWait}")
    /**
     * 配置获取连接等待超时的时间
     */
    private int maxWait = 72000;

//    @Value("${spring.datasource.druid.timeBetweenEvictionRunsMillis}")
    /**
     * 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒
     */
    private int timeBetweenEvictionRunsMillis = 60000;

//    @Value("${spring.datasource.druid.minEvictableIdleTimeMillis}")
    /**
     * 配置一个连接在池中最小生存的时间，单位是毫秒
     */
    private int minEvictableIdleTimeMillis = 300000;

//    @Value("${spring.datasource.druid.maxEvictableIdleTimeMillis}")
    /**
     * 配置一个连接在池中最大生存的时间，单位是毫秒
     */
    private int maxEvictableIdleTimeMillis = 900000;

//    @Value("${spring.datasource.druid.validationQuery}")
    /**
     * 配置检测连接是否有效
     */
    private String validationQuery = "SELECT 1 FROM DUAL";

//    @Value("${spring.datasource.druid.testWhileIdle}")
    /**
     *
     */
    private boolean testWhileIdle = true;

    //    @Value("${spring.datasource.druid.testOnBorrow}")
    private boolean testOnBorrow = false;

    //    @Value("${spring.datasource.druid.testOnReturn}")
    private boolean testOnReturn = false;


    /**
     * @return
     * @description 配置慢sql拦截器
     */
    @Bean(name = "statFilter")
    public StatFilter statFilter() {
        StatFilter statFilter = new StatFilter();
        //慢sql时间设置,即执行时间大于200毫秒的都是慢sql
        statFilter.setSlowSqlMillis(200);
        statFilter.setLogSlowSql(true);
        statFilter.setMergeSql(true);
        return statFilter;
    }

    /**
     * @return
     * @description 配置日志拦截器
     */
    @Bean(name = "logFilter")
    public Slf4jLogFilter logFilter() {
        Slf4jLogFilter slf4jLogFilter = new Slf4jLogFilter();
        slf4jLogFilter.setDataSourceLogEnabled(true);
        slf4jLogFilter.setStatementExecutableSqlLogEnable(true);
        return slf4jLogFilter;
    }


    public DruidDataSource dataSource(DruidDataSource datasource) {

        datasource.setUrl(url);
        datasource.setUsername(userName);
        datasource.setPassword(passWord);


        /* 配置初始化大小、最小、最大 */
        //初始化时建立物理连接的个数。初始化发生在显示调用init方法，或者第一次getConnection时
        datasource.setInitialSize(initialSize);
        //最大连接池数量
        datasource.setMaxActive(maxActive);
        datasource.setMinIdle(minIdle);

        /* 配置获取连接等待超时的时间 */
        //获取连接时最大等待时间，单位毫秒。配置了maxWait之后，缺省启用公平锁，并发效率会有所下降，如果需要可以通过配置useUnfairLock属性为true使用非公平锁。
        datasource.setMaxWait(maxWait);

        /* 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位是毫秒 */
        datasource.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);

        /* 配置一个连接在池中最小、最大生存的时间，单位是毫秒 */
        datasource.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
        datasource.setMaxEvictableIdleTimeMillis(maxEvictableIdleTimeMillis);
        /*
         *
         * 用来检测连接是否有效的sql，要求是一个查询语句，常用select 'x'。如果validationQuery为null，testOnBorrow、testOnReturn、testWhileIdle都不会起作用。
         */
        datasource.setValidationQuery(validationQuery);
        /* 建议配置为true，不影响性能，并且保证安全性。申请连接的时候检测，如果空闲时间大于timeBetweenEvictionRunsMillis，执行validationQuery检测连接是否有效。 */
        datasource.setTestWhileIdle(testWhileIdle);
        /* 申请连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnBorrow(testOnBorrow);
        /* 归还连接时执行validationQuery检测连接是否有效，做了这个配置会降低性能。 */
        datasource.setTestOnReturn(testOnReturn);


        datasource.setConnectTimeout(120000);
        datasource.setSocketTimeout(240000);


        List<Filter> filters = new ArrayList<>();
        filters.add(statFilter());
        filters.add(logFilter());
        datasource.setProxyFilters(filters);

        try {
            datasource.setFilters("stat,wall,slf4j");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return datasource;
    }

    public static void main(String[] args) {
        System.out.println("18903182799_8823022279900911".length());
    }
}
