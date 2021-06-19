package edu.wuyi.fans.config;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @Author fan <wuyi_edu@163.com>
 * @Date 2020/11/11
 */
@Configuration
@MapperScan("edu.wuyi.fans.mapper")
@ServletComponentScan
@EnableAsync
@EnableCaching
public class ApplicationContext {

    @Bean
    public PaginationInterceptor paginationInterceptor() {
        PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
        return paginationInterceptor;
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        RestHighLevelClient client = new RestHighLevelClient(
                //RestClient.builder(new HttpHost("127.0.0.1",9200,"http"))
                RestClient.builder(new HttpHost("172.17.0.6",9200,"http"))
        );
        return client;
    }
}
