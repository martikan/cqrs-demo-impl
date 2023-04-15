package com.demobank.account.cmd.config;

import com.demobank.account.common.converter.ZonedDateTimeReadConverter;
import com.demobank.account.common.converter.ZonedDateTimeWriteConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.support.DefaultConversionService;

@Configuration
public class Config {

    @Bean
    public ConversionService conversionService() {
        final var service = new DefaultConversionService();
        service.addConverter(new ZonedDateTimeReadConverter());
        service.addConverter(new ZonedDateTimeWriteConverter());

        return service;
    }

}
