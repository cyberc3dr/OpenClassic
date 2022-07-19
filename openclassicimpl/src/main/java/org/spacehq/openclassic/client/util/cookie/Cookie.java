package org.spacehq.openclassic.client.util.cookie;

import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.Validate;

import org.spacehq.openclassic.api.OpenClassic;

public class Cookie {
	
	private static DateFormat expiresFormat1 = new SimpleDateFormat("E, dd MMM yyyy k:m:s 'GMT'", Locale.US);
	private static DateFormat expiresFormat2 = new SimpleDateFormat("E, dd-MMM-yyyy k:m:s 'GMT'", Locale.US);

	private String name;
	private String value;
	private URI uri;
	private String domain;
	private Date expires;
	private String path;

	public Cookie(URI uri, String paramString) {
		String[] elements = paramString.split(";");
		String data = elements[0].trim();
		this.uri = uri;
		this.name = data.substring(0, data.indexOf('='));
		this.value = data.substring(data.indexOf('=') + 1);
		this.path = "/";
		this.domain = uri.getHost().toLowerCase();

		for(int index = 1; index < elements.length; index++) {
			data = elements[index].trim();
			int equals = data.indexOf('=');
			if(equals == -1) {
				continue;
			}

			String name = data.substring(0, equals);
			String value = data.substring(equals + 1);
			if(name.equalsIgnoreCase("domain")) {
				String host = uri.getHost();
				if(host.equals(value)) {
					this.domain = value.toLowerCase();
				} else {
					if(!value.startsWith(".")) {
						value = "." + value;
					}

					host = host.substring(host.indexOf('.'));
					Validate.isTrue(host.equalsIgnoreCase(value), "Trying to set foreign cookie");
					this.domain = value.toLowerCase();
				}
			} else if(name.equalsIgnoreCase("path")) {
				this.path = value;
			} else {
				if(!name.equalsIgnoreCase("expires")) continue;

				try {
					this.expires = expiresFormat1.parse(value);
				} catch(Exception e) {
					try {
						this.expires = expiresFormat2.parse(value);
					} catch(Exception e1) {
						OpenClassic.getLogger().warning("Bad date format in header: " + value);
					}
				}
			}
		}
	}

	public boolean hasExpired() {
		if(this.expires == null) {
			return false;
		}
		Date localDate = new Date();
		return localDate.after(this.expires);
	}

	public String getName() {
		return this.name;
	}

	public String getValue() {
		return this.value;
	}

	public URI getURI() {
		return this.uri;
	}

	public boolean domainMatches(String domain) {
		if(domain.equalsIgnoreCase(this.domain)) return true;
		return domain.endsWith("." + this.domain);
	}

	public boolean pathMatches(String path) {
		if(path.equals(this.path)) return true;
		return path.startsWith(this.path);
	}

	public boolean uriMatches(URI uri) {
		if(!domainMatches(uri.getHost())) {
			return false;
		}

		String path = uri.getPath();
		if((path == null) || (path.length() == 0)) path = "/";

		return pathMatches(path);
	}

	public boolean isEquivalent(URI uri, String name) {
		if(hasExpired()) {
			return false;
		}

		if(!uriMatches(uri)) {
			return false;
		}

		return this.name.equals(name);
	}

	public boolean matches(URI uri) {
		if(hasExpired()) {
			return false;
		}
		String path = uri.getPath();
		if(path == null) {
			path = "/";
		}

		return path.startsWith(this.path);
	}

	@Override
	public String toString() {
		return this.name + "=" + this.value;
	}
	
}
