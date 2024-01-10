package ir.co.kishsys.sanhabinquiry.config;

import ir.co.kishsys.sanhabinquiry.models.SanhabServiceInfo;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;

import java.util.concurrent.TimeUnit;

@org.springframework.context.annotation.Configuration
public class Configuration {

    @Value("${sanhab.read.timeout}")
    private long readTimeout;

    @Value("${sanhab.write.timeout}")
    private long writeTimeout;

    @Bean
    public OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS)
                .writeTimeout(writeTimeout, TimeUnit.MILLISECONDS)
                .build();
    }

}
