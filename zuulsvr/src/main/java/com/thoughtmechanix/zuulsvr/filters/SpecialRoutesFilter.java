package com.thoughtmechanix.zuulsvr.filters;


import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.thoughtmechanix.zuulsvr.model.AbTestingRoute;
import com.thoughtmechanix.zuulsvr.model.UserInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.ProxyRequestHelper;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;



public class SpecialRoutesFilter extends ZuulFilter {
    private static final int FILTER_ORDER =  3;
    private static final boolean SHOULD_FILTER = true;

    @Autowired
    FilterUtils filterUtils;

    @Autowired
    RestTemplate restTemplate;

    @Override
    public String filterType() {
        return filterUtils.ROUTE_FILTER_TYPE;
    }

    @Override
    public int filterOrder() {
        return FILTER_ORDER;
    }

    @Override
    public boolean shouldFilter() {
        return SHOULD_FILTER;
    }

    private AbTestingRoute getAbRoutingInfo(String serviceName){
        ResponseEntity<AbTestingRoute> restExchange = null;
        try {
            restExchange =
                    restTemplate.exchange(
                            "http://specialroutesservice/v1/route/abtesting/{serviceName}",
                            HttpMethod.GET,
                            null, AbTestingRoute.class, serviceName);
        }
        catch(HttpClientErrorException ex){
            if (ex.getStatusCode()== HttpStatus.NOT_FOUND) {
                return null;
            }

            throw ex;
        }


        return restExchange.getBody();
    }

    private String buildRouteString(String oldEndpoint, String newEndpoint, String serviceName){
        int index = oldEndpoint.indexOf(serviceName);

        String strippedRoute = oldEndpoint.substring(index);

        return String.format("%s/%s", newEndpoint, strippedRoute);
    }


    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();

        AbTestingRoute abTestRoute = getAbRoutingInfo( ctx.get("serviceId").toString());


        System.out.println("!!!!! CTX: " + ctx.getRequest().getRequestURI());
        System.out.println("!!!!! CTX2: " + ctx.get("serviceId"));

        if (abTestRoute!=null) {
            System.out.printf("!!!! ABTESTROUTE: %s\n" , abTestRoute.getEndpoint());
            System.out.printf("!!!! New Route : %s\n", buildRouteString(ctx.getRequest().getRequestURI(),
                                                                        abTestRoute.getEndpoint(),
                                                                        ctx.get("serviceId").toString()));
        }


        //ctx.setRes
        return null;

    }
}
