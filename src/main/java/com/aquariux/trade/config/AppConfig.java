package com.aquariux.trade.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AppConfig {

  @Value("${app.service.aggregation.source.binance}")
  private String binanURL;

  @Value("${app.service.aggregation.source.houbi}")
  private String houbiURL;

}
