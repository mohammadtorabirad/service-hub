package ir.co.kishsys.sanhabinquiry.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bootstrap.config.BootstrapPropertySource;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.core.env.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Properties;

@Service
public class PropsConfig {

    Environment env;

    private Properties props;

    public PropsConfig(Environment env) {
        this.env = env;
    }

    public Properties loadProperties(){
        if (props == null){
            props = applicationProperties();
        }
        return props;
    }
    private Properties applicationProperties() {
        var properties = new Properties();
        for (Iterator it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
            PropertySource propertySource = (PropertySource) it.next();
            if (propertySource instanceof BootstrapPropertySource<?>) {
                properties.putAll(((HashMap) propertySource.getSource()));
            }
        }
        return properties;
    }

}
