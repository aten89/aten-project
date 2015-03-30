/**
 * 基于cas-client-java-2.1.1修改
 */
package org.eapp.cas;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

/**
 * 基于cas-client-java-2.1.1修改
 */
public class ProxyTicketValidator extends ServiceTicketValidator {

	// Additive state
	protected List<String> proxyList;

	// Accessors
	/**
	 * Retrieves a list of proxies involved in the current authentication.
	 */
	public List<String> getProxyList() {
		return proxyList;
	}


	// Response parser
	protected DefaultHandler newHandler() {
		return new ProxyHandler();
	}

	protected class ProxyHandler extends ServiceTicketValidator.Handler {

		// Constants
		protected static final String PROXIES = "cas:proxies";
		protected static final String PROXY = "cas:proxy";

		// Parsing state
		protected List<String> proxyList = new ArrayList<String>();
		protected boolean proxyFragment = false;

		// Parsing logic
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			super.startElement(uri, localName, qName, attributes);
			if (authenticationSuccess && qName.equals(PROXIES)) {
				proxyFragment = true;
			}
		}

		public void endElement(String uri, String localName, String qName) throws SAXException {
			super.endElement(uri, localName, qName);
			if (qName.equals(PROXIES)) {
				proxyFragment = false;
			} else if (proxyFragment && qName.equals(PROXY)) {
				proxyList.add(currentText.toString().trim());
			}
		}
 
		public void endDocument() throws SAXException {
			super.endDocument();
			if (authenticationSuccess) {
				ProxyTicketValidator.this.proxyList = proxyList;
			}
		}
	}

	/**
	 * Clears internally manufactured state.
	 */
	protected void clear() {
		super.clear();
		proxyList = null;
	}
}
