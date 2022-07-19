package org.spacehq.openclassic.client.util.cookie;

import java.io.IOException;
import java.net.CookieHandler;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.spacehq.openclassic.api.OpenClassic;

public class CookieList extends CookieHandler {
	private List<Cookie> cookieJar = Collections.synchronizedList(new CopyOnWriteArrayList<Cookie>());

	@Override
	public void put(URI paramURI, Map<String, List<String>> paramMap) throws IOException {
		List<String> cookies = paramMap.get("Set-Cookie");
		if(cookies != null) {
			for(String str : cookies) {
				Cookie cookie = new Cookie(paramURI, str);

				for(Cookie c : this.cookieJar) {
					if(c != null && cookie.uriMatches(c.getURI()) && cookie.getName().equals(c.getName())) {
						this.cookieJar.remove(c);
						break;
					}
				}

				this.cookieJar.add(cookie);
			}
		}
	}

	@Override
	public Map<String, List<String>> get(URI uri, Map<String, List<String>> responseHeader) throws IOException {
		StringBuilder sb = new StringBuilder();
		for(Cookie cookie : this.cookieJar) {
			if(cookie.hasExpired()) {
				this.cookieJar.remove(cookie);
			} else if(cookie.matches(uri)) {
				if(sb.length() > 0) {
					sb.append(", ");
				}

				sb.append(cookie.toString());
			}
		}

		List<String> cookies;
		Map<String, List<String>> result = new HashMap<String, List<String>>(responseHeader);

		if(sb.length() > 0) {
			cookies = Collections.singletonList(sb.toString());
			result.put("Cookie", cookies);
		}

		return Collections.unmodifiableMap(result);
	}

	public Cookie getCookie(String uri, String name) {
		try {
			return this.getCookie(new URI(uri), name);
		} catch(URISyntaxException e) {
			OpenClassic.getLogger().severe("Invalid URI in getCookie");
		}
		
		return null;
	}

	public Cookie getCookie(URI uri, String name) {
		for(Cookie localCookie : this.cookieJar) {
			if(localCookie.isEquivalent(uri, name)) {
				return localCookie;
			}
		}
		
		return null;
	}
}
