package com.meimeitech.db;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author paul
 * @description
 * @date 2019/4/16
 */
@ConfigurationProperties(prefix = "spring.h2.server")
public class H2ServerProperties {

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getArgs() {
        return args;
    }

    public void setArgs(String args) {
        this.args = args;
    }

    private boolean enabled = false;

    private String args = "-tcp -web";

    public String[] args() {
        if (args == null) {
            return null;
        }
        String[] splits = args.split(" ");
        List<String> result = new ArrayList<>();
        for (String s : splits) {
            if (s == null) {
                continue;
            }
            s = s.trim();

            if (s.length() == 0) {
                continue;
            }
            result.add(s);
        }
        return result.toArray(new String[result.size()]);
    }
}
