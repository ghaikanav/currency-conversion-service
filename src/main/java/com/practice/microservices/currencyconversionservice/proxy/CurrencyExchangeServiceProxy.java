package com.practice.microservices.currencyconversionservice.proxy;

import com.practice.microservices.currencyconversionservice.dto.CurrencyConversionBean;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "currency-exchange-service", url="localhost:8000")
public interface CurrencyExchangeServiceProxy {
    @GetMapping("/currency-exchange")
    public CurrencyConversionBean getExchangeValue(@RequestParam String from, @RequestParam String to);
}
