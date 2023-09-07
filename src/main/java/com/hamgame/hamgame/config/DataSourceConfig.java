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

	/*
	 * Master DataSource 생성
	 * Spring Boot 2.0부터 Hikari cp default로 적용되어 type을 HikariDataSource로 설정
	 * application.yml 파일 설정값 받와와 Hikari DataSource에 설정
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.master.hikari")
	public DataSource masterDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	/*
	 * Slave DataSource 생성
	 */
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource.slave.hikari")
	public DataSource slaveDataSource() {
		return DataSourceBuilder.create().type(HikariDataSource.class).build();
	}

	/*
	 * 직접 생성한 DataSource 정보를 등록한다.
	 */
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

	/*
	 * transaction 시작 전에 dataSource connection을 가져와 끝날 때까지 같은 소스만 사용하여
	 * query 실행 시점에 dataSourceConnection을 가져올 수 있도록 LazyConnection으로 설정한다.
	 */
	@Bean
	@DependsOn("routingDataSource")
	public LazyConnectionDataSourceProxy dataSource(DataSource routingDataSource) {
		return new LazyConnectionDataSourceProxy(routingDataSource);
	}

	/*
	 * hibernate 설정은 JpaBaseConfigutarion을 상속한 HibernateJpaConfiguration 클래스에 적용된다.
	 * 현재 LocalContainerEntityManagerFactoryBean을 직접 등록하여 auto configuration이 불가능하기 때문에
	 * JPA에서 사용할 EntityManagerFactory에 hibernate 설정 직접 주입해준다.
	 */
	@Primary
	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
		em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		em.setDataSource(dataSource);
		em.setPackagesToScan("com.hamgame.hamgame.domain");

		Map<String, Object> properties = new HashMap<>();
		// 네이밍 전략을 camelCase -> snake_case로 변경한다.
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

	/*
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
