package com.scrapyscrapy;

import java.util.List;

import reactor.core.publisher.Flux;

public class DummyFluxFactory {

	static Flux<Flux<String>> createFlux(int depth, String id, int maxDepth) {
		var ths = Flux.fromIterable(List.of(id));

		if (depth >= maxDepth) {
			return Flux.just(ths);
		}
		var result = Flux.range(0, 2)
			.map(i -> createFlux(depth + 1, id + "." + depth + "/" + i, maxDepth))
			.flatMap(i -> i);
		return result.mergeWith(Flux.just(ths));
	}

}
