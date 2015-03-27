package hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.codahale.metrics.*;
import com.codahale.metrics.httpclient.HttpClientMetricNameStrategies;
import com.codahale.metrics.jvm.GarbageCollectorMetricSet;
import com.codahale.metrics.jvm.MemoryUsageGaugeSet;
import com.codahale.metrics.jvm.ThreadStatesGaugeSet;
import com.ryantenney.metrics.spring.config.annotation.EnableMetrics;
import com.ryantenney.metrics.spring.config.annotation.MetricsConfigurerAdapter;
import org.apache.http.client.HttpClient;
import org.elasticsearch.metrics.ElasticsearchReporter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import javax.management.MBeanServerFactory;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Arvydas on 3/16/15.
 */
@SpringBootApplication
@Configuration
@EnableMetrics
@ComponentScan
@EnableAutoConfiguration
public class Application extends MetricsConfigurerAdapter {

    static final MetricRegistry metrics = new MetricRegistry();

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);

    }

    @Override
    public void configureReporters(MetricRegistry metricRegistry) {

//        metricRegistry.registerAll(new GarbageCollectorMetricSet());
//        metricRegistry.registerAll(new MemoryUsageGaugeSet());
//        metricRegistry.registerAll(new ThreadStatesGaugeSet());

        ConsoleReporter reporter = ConsoleReporter.forRegistry(metrics)
                .convertRatesTo(TimeUnit.SECONDS)
                .convertDurationsTo(TimeUnit.MILLISECONDS)
                .build();
        reporter.start(10, TimeUnit.SECONDS);

        JmxReporter jmxReporter = JmxReporter.forRegistry(metrics).inDomain("net.gogii").build();

        jmxReporter.start();

        ElasticsearchReporter esReporter = null;
        try {
            esReporter = ElasticsearchReporter.forRegistry(metricRegistry)
                    .hosts("9cn22.lax1.gogii.net:9200")
//                    .percolationFilter(MetricFilter.ALL)
                    .index("metricsdemo")
                    .timeout(10000)
                    .build();
            esReporter.start(10, TimeUnit.SECONDS);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public MetricRegistry getMetricRegistry() {
        return metrics;
    }

}
