package com.hamgame.hamgame.config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.hamgame.hamgame.common.RoutingDataSource;
import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableTransactionManagement
@RequiredArgsConstructor
@Profile("prod|dev")
public class DataSourceConfig {
	private final Environment env;

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.master.hikari")
	public DataSource masterDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
	public DataSource slaveDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	// routing dataSource Bean
	@Bean
	@DependsOn({"masterDataSource", "slaveDataSource"})
	public DataSource routingDataSource(
		@Qualifier("masterDataSource") DataSource master,
		@Qualifier("slaveDataSource") DataSource slave) {

		RoutingDataSource routingDataSource = new RoutingDataSource();

		Map<Object, Object> dataSourceMap = new HashMap<>();
		dataSourceMap.put("master", master);
		dataSourceMap.put("slave", slave);

		routingDataSource.setTargetDataSources(dataSourceMap);
		routingDataSource.setDefaultTargetDataSource(master);

		return routingDataSource;
	}

	@Bean
	@DependsOn("routingDataSource")
	public LazyConnectionDataSourceProxy dataSource(DataSource routingDataSource) {
		return new LazyConnectionDataSourceProxy(routingDataSource);
	}

	/**
	 * JPA에서 사용할 EntityManagerFactory 설정
	 * hibernate 설정 직접 주입
	 */
	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.hamgame.hamgame.domain");

		Map<String, Object> properties = new HashMap<>();
		properties.put("hibernate.physical_naming_strategy",
			CamelCaseToUnderscoresNamingStrategy.class.getName());
		properties.put("hibernate.implicit_naming_strategy",
			SpringImplicitNamingStrategy.class.getName());
		properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
		properties.put("hibernate.dialect", env.getProperty("spring.jpa.database-platform"));
		properties.put("hibernate.show_sql", env.getProperty("spring.jpa.properties.hibernate.show_sql"));
		properties.put("hibernate.format_sql", env.getProperty("spring.jpa.properties.hibernate.format_sql"));
		properties.put("hibernate.default_batch_fetch_size",
			env.getProperty("spring.jpa.properties.hibernate.default_batch_fetch_size"));
		em.setJpaPropertyMap(properties);

		return em;
	}

	/**
	 * JPA에서 사용할 TransactionManager 설정
	 */
	@Primary
	@Bean
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager tm = new JpaTransactionManager();
		tm.setEntityManagerFactory(entityManagerFactory);
		return tm;
	}
}
