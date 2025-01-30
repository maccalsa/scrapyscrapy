package com.scrapyscrapy;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import reactor.core.publisher.Flux;

public class WebCrawlFluxFactory {

	Flux<UrlVisit> createFlux(String url, int maxDepth, int depth) {
		UrlVisit visit = new UrlVisit(url, urlContent(url), maxDepth, depth);
		return Flux.just(visit).expand(urlVisit -> {
			List<UrlVisit> children = new ArrayList<>();
			// use flux map here
			if (urlVisit.depth < urlVisit.maxDepth) {
				System.out.println("here in expand");
				children = extractLinks(urlVisit.getContent()).stream()
					.map(link -> new UrlVisit(link, urlContent(link), urlVisit.getMaxDepth(), urlVisit.getDepth() + 1))
					.toList();
			}
			return Flux.fromIterable(children);
		});
	}

	public static class UrlVisit {

		private String url;

		private String content;

		private int maxDepth;

		private int depth;

		UrlVisit(String url, String content, int maxDepth, int depth) {
			this.setUrl(url);
			this.setContent(content);
			this.setDepth(depth);
			this.setMaxDepth(maxDepth);
			System.out.println(String.format("Visit url: %s, maxDepth: %s, depth: %s", url, maxDepth, depth));
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getContent() {
			return content;
		}

		public void setContent(String content) {
			this.content = content;
		}

		public int getMaxDepth() {
			return maxDepth;
		}

		public void setMaxDepth(int maxDepth) {
			this.maxDepth = maxDepth;
		}

		public int getDepth() {
			return depth;
		}

		public void setDepth(int depth) {
			this.depth = depth;
		}

	}

	String urlContent(String url) {
		try {
			System.out.println("loading " + url);
			Scanner scanner = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A");
			if (scanner.hasNext()) {
				return scanner.next();
			}
			else {
				return "nothing from " + url;
			}
		}
		catch (Exception ex) {
			String message = String.format("cant load %s because %s", url, ex.getMessage());
			ex.printStackTrace();
			return message;
		}
		finally {
			System.out.println("loaded " + url);
		}
	}

	List<String> extractLinks(String content) {
		// TODO
		return List.of("https://www.google.com", "https://www.bbc.co.uk/sport");
	}

}
