package com.scrapyscrapy;

import static com.scrapyscrapy.DummyFluxFactory.createFlux;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
public class SampleController {

	@GetMapping("/samplemono")
	public Mono<String> sampleMono() {
		String strContext = String.format("%s processors:%s maxMemory: %s  ", Runtime.version(),
				Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().maxMemory());
		return Mono.fromCallable(() -> {
			return "Mono Hello, Reactive Spring Boot on GraalVM!<br>" + strContext;
		});
	}

	@GetMapping("/sampleflux")
	public Flux<String> sampleFlux() {
		var flux = createFlux(0, "1", 3);
		return Flux.just(flux).flatMap(i -> i).flatMap(i -> i).map(i -> i + "<br>\r\n");
	}

	@PostMapping("/webCrawl")
	public Flux<WebCrawlFluxFactory.UrlVisit> webCrawl(String url) {
		var flux = (new WebCrawlFluxFactory().createFlux(url, 3, 0)).map(urlVisit -> {
			urlVisit.setContent(urlVisit.getContent().substring(0, 100));
			return urlVisit;
		});
		return flux;
	}

}
