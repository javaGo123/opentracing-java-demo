package com.example.demoopentracing;

import io.opentracing.Tracer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import io.jaegertracing.Configuration;


/**
 * @author wangxinxin
 * @date 2019.3.8
 */
@SpringBootApplication
public class DemoOpentracingApplication {

	@Bean
	public RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
		return restTemplateBuilder.build();
	}

	@Bean
	public Tracer jaegerTracer() {
		Configuration.SenderConfiguration senderConf = new Configuration.SenderConfiguration();
		senderConf.withAgentHost("10.5.6.10").withAgentPort(6831);

		Configuration.ReporterConfiguration reportConf = new Configuration.ReporterConfiguration();
		reportConf.withFlushInterval(1000).withLogSpans(false).withMaxQueueSize(100).withSender(senderConf);

		Configuration.SamplerConfiguration samplerConf = new Configuration.SamplerConfiguration();
		samplerConf.withParam(1);

		Configuration config = new Configuration("wangxinxin-jaeger-test");
		config.withReporter(reportConf).withSampler(samplerConf);

		return config.getTracer();
	}

//	@Bean
//	public io.opentracing.Tracer zipkinTracer() {
//		OkHttpSender okHttpSender = OkHttpSender.builder()
//				.encoding(Encoding.JSON)
//				.endpoint("http://localhost:9411/api/v1/spans")
//				.build();
//		AsyncReporter<Span> reporter = AsyncReporter.builder(okHttpSender).build();
//		Tracing braveTracer = Tracing.newBuilder()
//				.localServiceName("wangxinxin-test")
//				.reporter(reporter)
//				.traceId128Bit(true)
//				.sampler(Sampler.ALWAYS_SAMPLE)
//				.build();
//		return BraveTracer.create(braveTracer);
//	}

	public static void main(String[] args) {
		SpringApplication.run(DemoOpentracingApplication.class, args);
	}
}
