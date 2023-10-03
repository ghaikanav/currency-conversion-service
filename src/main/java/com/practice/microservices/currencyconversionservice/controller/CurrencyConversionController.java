package com.practice.microservices.currencyconversionservice.controller;

import com.practice.microservices.currencyconversionservice.dto.CurrencyConversionBean;
import com.practice.microservices.currencyconversionservice.proxy.CurrencyExchangeServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Configuration(proxyBeanMethods = false)
class RestTemplateConfiguration {
    @Bean
    RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder.build();
    }
}

@RestController
public class CurrencyConversionController {

    @Autowired
    private CurrencyExchangeServiceProxy proxy;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/currency-convertor")
    public CurrencyConversionBean getConvertedAmount(@RequestParam String from, @RequestParam String to, @RequestParam BigDecimal amount) {
        // Feign will address this problem
        Map<String, String> uriVariables = new HashMap<>();
        uriVariables.put("from", from);
        uriVariables.put("to", to);
        ResponseEntity<CurrencyConversionBean> responseEntity = restTemplate
                .getForEntity(
                        "http://localhost:8000/currency-exchange?from={from}&to={to}",
                        CurrencyConversionBean.class,
                        uriVariables);
        CurrencyConversionBean response = responseEntity.getBody();
        return new CurrencyConversionBean(
                response.getId(),
                from,
                to,
                response.getConversionMultiple(),
                amount,
                amount.multiply(response.getConversionMultiple()),
                response.getPort());
    }

    @GetMapping("/currency-convertor-feign")
    public CurrencyConversionBean convertCurrency(@RequestParam String from, @RequestParam String to, @RequestParam BigDecimal amount) {
        CurrencyConversionBean response = proxy.getExchangeValue(from, to);
        return new CurrencyConversionBean(
                response.getId(),
                from,
                to,
                response.getConversionMultiple(),
                amount,
                amount.multiply(response.getConversionMultiple()),
                response.getPort());
    }
}
