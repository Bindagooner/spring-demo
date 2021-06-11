package neospider.mngr.mvc.configuration;

import com.hazelcast.config.Config;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.web.WebFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Properties;

/**
 * HazelcastConfiguration.
 */
@Configuration
public class HazelcastConfiguration {

    @Bean
    public WebFilter webFilter(@Qualifier("hazelcastInstance") HazelcastInstance hazelcastInstance) {

        Properties properties = new Properties();
        properties.put("instance-name", hazelcastInstance.getName());
        properties.put("sticky-session", "false");

        return new WebFilter(properties);
    }
}
