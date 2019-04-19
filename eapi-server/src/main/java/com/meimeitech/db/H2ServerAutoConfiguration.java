package com.meimeitech.db;

/*
 * Copyright 2012-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import org.h2.tools.Server;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import java.util.concurrent.atomic.AtomicBoolean;


/**
 * {@link EnableAutoConfiguration Auto-configuration} for H2's web console.
 *
 * @author Andy Wilkinson
 * @author Marten Deinum
 * @author Stephane Nicoll
 * @since 1.3.0
 */
@Configuration
@ConditionalOnClass(name = "org.h2.Driver")
@ConditionalOnProperty(prefix = "spring.datasource", name = "driver-class-name", havingValue = "org.h2.Driver", matchIfMissing = false)
@EnableConfigurationProperties(H2ServerProperties.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class H2ServerAutoConfiguration implements ServletContextInitializer, InitializingBean {

    private H2ServerProperties h2ServerProperties;

    private AtomicBoolean isStarted = new AtomicBoolean(false);

    public H2ServerAutoConfiguration(H2ServerProperties h2ServerProperties) {
        this.h2ServerProperties = h2ServerProperties;
    }


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {

    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if (h2ServerProperties.isEnabled() && !isStarted.get() && isStarted.compareAndSet(false,true)) {
            new Server().runTool(h2ServerProperties.args());
        }
    }
}

