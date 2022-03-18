package com.cable.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class DBConfiguration {

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.master")
	public DataSource getDataSource() {
		return DataSourceBuilder.create().build();
	}
	
	@Bean
	@Qualifier(value = "jdbcTemplate")
	public JdbcTemplate getTemplate() {
		return new JdbcTemplate(getDataSource());
	}
}
