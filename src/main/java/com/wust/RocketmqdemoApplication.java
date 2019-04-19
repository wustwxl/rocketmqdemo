package com.wust;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//增加注解@EnableCaching，开启缓存功能
@EnableCaching
@SpringBootApplication
//@EnableAutoConfiguration(exclude={DataSourceAutoConfiguration.class})
//开启事务管理
@EnableTransactionManagement
//与dao层的@Mapper二选一写上即可(主要作用是扫包)
@MapperScan("com.wust.mapper")
public class RocketmqdemoApplication {

	public static void main(String[] args) {

		//常规开启Banner
		//SpringApplication.run(RocketmqdemoApplication, args);


		//修改Banner的模式为OFF
		SpringApplicationBuilder builder = new SpringApplicationBuilder(RocketmqdemoApplication.class);
		builder.bannerMode(Banner.Mode.OFF).run(args);

	}

}
