package org.spacehq.openclassic.client.util.protocol.zip;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class ZIPURLConnection extends URLConnection {

	protected ZIPURLConnection(URL url) {
		super(url);
	}

	@Override
	public void connect() throws IOException {
	}

	@Override
	public InputStream getInputStream() throws IOException {
		InputStream result = null;
		String spec = this.url.getFile();
		int separator = spec.indexOf('!');
		if(separator == -1) {
			throw new MalformedURLException("No ! found in URL spec:" + spec);
		}

		URL zipFileURL = new URL(spec.substring(0, separator++));
		String entryName = spec.substring(separator + 1);
		ZipInputStream zis = new ZipInputStream(zipFileURL.openStream());
		ZipEntry entry = null;
		if((entry = zis.getNextEntry()) != null) {
			while((entry = zis.getNextEntry()) != null) {
				if(entryName.equals(entry.getName())) {
					result = zis;
					break;
				}
			}
		} else {
			throw new IOException("Given path is not referring ZIP file.");
		}
		
		return result;
	}
	
}
