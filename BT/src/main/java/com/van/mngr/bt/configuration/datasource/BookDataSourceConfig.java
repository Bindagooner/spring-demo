package com.van.mngr.bt.configuration.datasource;

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
@MapperScan(basePackages = "neospider.mngr.bt.persistence.repository.book", sqlSessionFactoryRef = "bookSqlSessionFactory")
public class BookDataSourceConfig {

    @Bean(name = "bookDataSource")

    @Profile("sboot")
    @ConfigurationProperties(prefix = "spring.datasource.book-ds")
    public DataSource getbookDatasource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "bookDataSource")
    @Profile("!sboot")
    public DataSource getBookJndiDatasource(@Value("${spring.datasource.book-ds.jndi-name}") String jndi) {
        JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
        dataSourceLookup.setResourceRef(true);
        return dataSourceLookup.getDataSource(jndi);
    }

    @Bean(name = "bookSqlSessionFactory")
    public SqlSessionFactory bookSqlSessionFactory(@Qualifier("bookDataSource") DataSource dataSource) throws Exception {
        SqlSessionFactoryBean bean = new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        bean.setMapperLocations(new PathMatchingResourcePatternResolver().getResources("classpath*:/mybatis/BookMapper.xml"));
        return bean.getObject();
    }
    @Bean(name = "bookSqlSessionTemplate")
    public SqlSessionTemplate bookSqlSessionTemplate(@Qualifier("bookSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
