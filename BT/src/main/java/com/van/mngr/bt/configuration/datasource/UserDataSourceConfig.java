package com.van.mngr.bt.configuration.datasource;

import com.zaxxer.hikari.util.DriverDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.van.mngr.bt.persistence.repository.user", sqlSessionFactoryRef = "userSqlSessionFactory")
public class UserDataSourceConfig {

    @Bean(name = "userDataSource")
    @Primary
    @ConfigurationProperties(prefix = "spring.datasource.user-ds")
    @Profile("sboot")
    public DataSource getUserDatasource() {
        DataSource dataSource = DataSourceBuilder.create().build();
        return dataSource;
    }

    @Bean(name = "userDataSource")
    @Profile("!sboot")
    public DataSource getBookJndiDatasource(@Value("${spring.datasource.user-ds.jndi-name}") String jndi) {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        dataSourceLookup.setResourceRef(true);
        return dataSourceLookup.getDataSource(jndi);
    }

    @Bean(name = "userSqlSessionFactory")
    @Primary
    public SqlSessionFactory userSqlSessionFactory(@Qualifier("userDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mybatis/UserMapper.xml"));
        return bean.getObject();
    }
    @Bean(name = "userSqlSessionTemplate")
    @Primary
    public SqlSessionTemplate userSqlSessionTemplate(@Qualifier("userSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
