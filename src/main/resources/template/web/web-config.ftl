package ${packageName}.service.config;

import ${packageName}.service.resolver.PageDefaultResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.List;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

  @Override
  public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
      argumentResolvers.add(new PageDefaultResolver());
  }

  @Override
  public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
  }
}
