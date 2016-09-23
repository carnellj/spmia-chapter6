package com.thoughtmechanix.zuulsvr;

import com.thoughtmechanix.zuulsvr.filters.AuthenticationFilter;
import com.thoughtmechanix.zuulsvr.filters.ResponseFilter;
import com.thoughtmechanix.zuulsvr.filters.SpecialRoutesFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import com.thoughtmechanix.zuulsvr.filters.TrackingFilter;


@SpringBootApplication
@EnableZuulProxy
public class ZuulServerApplication {

    @Bean
    public TrackingFilter trackingFilter(){
        return new TrackingFilter();
    }

    @Bean
    public AuthenticationFilter authenticationFilter(){ return new AuthenticationFilter();}

    @Bean
    public ResponseFilter responseFilter(){
        return new ResponseFilter();
    }

    @Bean
    public SpecialRoutesFilter routesFilter() {return new SpecialRoutesFilter();}

    public static void main(String[] args) {
        SpringApplication.run(ZuulServerApplication.class, args);
    }
}

