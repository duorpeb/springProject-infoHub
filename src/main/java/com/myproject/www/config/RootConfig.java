package com.myproject.www.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/* Annotation 설명 
 * 
 * > @Configuration 
 *   - 상속 혹은 구현하지 않는 root 파일임을 의미하는 annotation 으로 해당 클래스가 하나 이상의 @Bean 메서드를 
 *    정의한 설정 클래스 임을 나타냄 
 *    
 *   - 내부적으로 @Component 처리되어 Spring Container 가 관리 
 *   
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 *  
 * > @EnableTransactionManagement 
 *  
 *   - @EnableTransactionManagement 은 선언적 트랜잭션 처리를 활성화하는 어노테이션으로 @Transaction 이 붙은
 *     메서드에 대해 AOP 기반으로 Transcation 경계 설정 
 *     
 *   - @Configuration 클래스에 선언 
 * 
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > @MapperScan(base={"...", "...", ...}) 
 *   
 *   - MyBatis 의 Mapper Interface 를 자동으로 스캔하여 @Bean 으로 등록하는 Annotation 으로 쌍따옴표 안에 패키지 경로를 작성
 *   
 *   - @Configuration 클래스 위 혹은 메인 애플리케이션 클래스 위에 작성 
 *   
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > @Autowired
 *   
 *   - Sprin Container 에 등록된 Bean 에 의존성을 주입 
 *   
 *   - 의존성 
 * 
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * > @Bean 
 * 
 *   - 해당 메서드의 반환값을 Spring Bean 으로 등록하여 Spring 이 생애주기를 관리하도록 하게 하며 다른 @Bean 에서 
 *     @Autowired 가능 
 * 
 * 
 * ――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――――
 * */
@MapperScan(basePackages = {"com.myproject.www.repository"})
@EnableTransactionManagement
@Configuration
public class RootConfig {
	// 초기화 
	 // 필드 주입
	 // 스프링 컨테이너 자신(ApplicationContext)을 이 필드에 주입하여 필요하다면 
	 //코드에서 빈 조회, 이벤트 발행, 프로퍼티 조회 등 컨텍스트 API를 직접 사용할 수 있게 함
	@Autowired
	ApplicationContext applicationContext;
	
	
	// dataSource() - JDBC Driver, URL, Username, Password 를 설정
	 //  HikariCP 커넥션 풀을 설정해서 DataSource 빈으로 등록
	@Bean
	public DataSource dataSource() {
		HikariConfig hikariConfig = new HikariConfig();
		
		hikariConfig.setDriverClassName("net.sf.log4jdbc.sql.jdbcapi.DriverSpy");
		hikariConfig.setJdbcUrl("jdbc:log4jdbc:mysql://localhost:3306/spring_myproject_db");
		hikariConfig.setUsername("springuser");
		hikariConfig.setPassword("mysql");
		
		/* Set additional Options
		 * 
		 * > 단, setMaximumPoolSize(), setMinimumIdle(), setConnectionTestQuery()
		 *   , setPoolName() 는 기본적인 추가 설정
		 * */  
		 // 최대 커넥션 개수
		hikariConfig.setMaximumPoolSize(5);
		 // 최소 유휴 커넥션 개수 
		hikariConfig.setMinimumIdle(5);
		 // 연결이 잘됐는지 확인하는 Query
		hikariConfig.setConnectionTestQuery("SELECT now()");
		 // Pool Name 은 로깅·모니터링·JMX 등록 시 지정할 이름
		hikariConfig.setPoolName("spring_myproject_HikariCP");
		
		 // cachePrepStmts() - 캐시 사용여부 설정 
		  // addDataSourceProperty("name","value") 는 사용자가 임의로 설정하는 옵션을 의미
		  // 이 옵션들은 MySQL Connector/J 같은 드라이버가 제공하는 캐시 기능을 활성화해,
		  // 반복적인 쿼리 컴파일 오버헤드를 줄여줌 
		hikariConfig.addDataSourceProperty("dataSource.cachePrepStmts", "true");
		hikariConfig.addDataSourceProperty("dataSource.prepStmtsCacheSize", "250");
		hikariConfig.addDataSourceProperty("dataSource.prepStmtsCacheSqlLimit", "true");
		hikariConfig.addDataSourceProperty("dataSource.useServerPrepStmts", "true");
		
		// 생성
		HikariDataSource hikariDataSource = new HikariDataSource(hikariConfig);
		
		return hikariDataSource;
	}
	
	
	// sqlSessionFactory() - dataSource() 를 sql 과 연결 
	@Bean
	public SqlSessionFactory sqlSessionFactory() throws Exception {
		// 초기화
		SqlSessionFactoryBean sqlFactoryBean = new SqlSessionFactoryBean();
		
		// SQL 과 위에서 작성한 dataSource() 를 연결 
		sqlFactoryBean.setDataSource(dataSource());
		
		// Mapper 
		 // applicationContext.getResources() 사용 시 예외처리 해야 함 
		 /* classpath:
		  * 
		  * classpath: 는 스프링의 ResourceLoader 에게 클래스패스 (자바 애플리케이션의 클래스·리소스 루트)
		  * 에서 파일을 찾으라고 알려주는 리소스 위치 접두사 (prefix)
		  * 
		  * > 클래스패스란 개발할 때 src/main/resources 에 넣은 파일들을 의미하고 빌드 후엔 
		  * jar 내부의 루트 디렉터리 등 애플리케이션의 클래스와 설정 파일이 담긴 경로 집합을 의미
		  * */ 
		sqlFactoryBean.setMapperLocations(applicationContext.getResources("classpath:/mapper/*.xml"));
		
		// DB 표기법 (스네이크 표기법) 과 Java 표기법 (카멜 표기법) 이 다른 것을 자동으로 변환 
		 // 시켜 mybatisConfig.xml 에게 인지시켜 줌 
		
		// 클래스패스 최상위에 있는 mybatisConfig.xml 파일을 설정 파일로 읽어들이겠다는 의미
		sqlFactoryBean.setConfigLocation(applicationContext.getResource("classpath:/mybatisConfig.xml"));
		
		// sqlFactoryBean.getObject() 는 예외 처리가 필요
		return sqlFactoryBean.getObject();
	}
	
	
	// transactionManager() - 트랜잭션 매니저 설정
	@Bean
	public DataSourceTransactionManager transactionManager() {
		return new DataSourceTransactionManager(dataSource());
	}	

	
	
}
