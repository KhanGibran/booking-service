package com.imaginea.bookingservice.config;

import io.github.resilience4j.cache.Cache;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.cache.CacheManager;
import javax.cache.Caching;
import javax.cache.configuration.MutableConfiguration;

@Slf4j
@Setter
@Getter
@Configuration
public class CacheConfig{

	private CacheManager cacheManager;
	private Cache<Object,Object> cacheContext;

	public CacheConfig() {
		this.cacheManager = Caching.getCachingProvider().getCacheManager();
		this.cacheContext = Cache.of(cacheManager.createCache("screensCache",new MutableConfiguration<>()));
	}
}