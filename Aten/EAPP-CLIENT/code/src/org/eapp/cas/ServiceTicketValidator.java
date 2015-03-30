/**
 * 基于cas-client-java-2.1.1修改
 */
package org.eapp.cas;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;


/**
 * 基于cas-client-java-2.1.1修改
 */
public class ServiceTicketValidator {
	private static Log log = LogFactory.getLog(ServiceTicketValidator.class);
	
	private String casValidateUrl;
	private String serviceTicket;
	private String service;
	private String proxyCallbackUrl;
	private boolean renew = false;
	
	private String entireResponse;
	private String  pgtIou, user, errorCode, errorMessage;
	private boolean successfulAuthentication;


  	// Accessors
	public void setCasValidateUrl(String casValidateUrl) {
		this.casValidateUrl = casValidateUrl;
	}
	
	public void setServiceTicket(String serviceTicket) {
		this.serviceTicket = serviceTicket;
	}

	public void setService(String service) {
		this.service = service;
	}
	
	public void setProxyCallbackUrl(String proxyCallbackUrl) {
		this.proxyCallbackUrl = proxyCallbackUrl;
	}
	
	public void setRenew(boolean renew) {
		this.renew = renew;
	}


	public String getResponse()  {
		return this.entireResponse;
	}
	
	public String getPgtIou() {
		return this.pgtIou;
	}
	
	public String getUser() {
		return this.user;
	}
	
	public String getErrorCode() {
		return this.errorCode;
	}
	
	public String getErrorMessage() {
		return this.errorMessage;
	}

	public boolean isAuthenticationSuccesful() {
		return this.successfulAuthentication;
	}

  
	public void validate() throws IOException, SAXException, ParserConfigurationException {
		if (casValidateUrl == null || serviceTicket == null) {
			throw new IllegalStateException("must set validation URL and ticket");
		}
		clear();
		StringBuffer sb = new StringBuffer();
		sb.append(casValidateUrl);
		if (casValidateUrl.indexOf('?') == -1) {
			sb.append('?');
		} else {
			sb.append('&');
		}
		sb.append("service=" + service + "&ticket=" + serviceTicket);
		if (proxyCallbackUrl != null) {
			sb.append("&pgtUrl=" + proxyCallbackUrl);
		}
		if (renew) {
			sb.append("&renew=true");
		}
		String response = retrieve(sb.toString());
		this.entireResponse = response;
		// parse the response and set appropriate properties
	    if (response != null) {
	    	XMLReader r = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
	    	r.setFeature("http://xml.org/sax/features/namespaces", false);
	    	r.setContentHandler(newHandler());
	    	r.parse(new InputSource(new StringReader(response)));
	    }
	}

	protected DefaultHandler newHandler() {
		return new Handler();
	}

	protected class Handler extends DefaultHandler {
		// Constants
		protected static final String AUTHENTICATION_SUCCESS = "cas:authenticationSuccess";
		protected static final String AUTHENTICATION_FAILURE = "cas:authenticationFailure";
		protected static final String PROXY_GRANTING_TICKET = "cas:proxyGrantingTicket";
		protected static final String USER = "cas:user";

		// Parsing state
		protected StringBuffer currentText = new StringBuffer();
		protected boolean authenticationSuccess = false;
		protected boolean authenticationFailure = false;
		protected String  pgtIou, user, errorCode, errorMessage;
    
		// Parsing logic
		@Override
		public void startElement(String uri, String localName, String qName, Attributes attributes) {
			// clear the buffer
			currentText = new StringBuffer();
			// check outer elements
			if (qName.equals(AUTHENTICATION_SUCCESS)) {
				authenticationSuccess = true;
			} else if (qName.equals(AUTHENTICATION_FAILURE)) {
				authenticationFailure = true;
				errorCode = attributes.getValue("code");
				if (errorCode != null) {
					errorCode = errorCode.trim();
				}
			}
		}

		@Override
		public void characters(char[] ch, int start, int length) {
			// store the body, in stages if necessary
			currentText.append(ch, start, length);
		}

		@Override
		public void endElement(String uri, String localName, String qName) throws SAXException {
			if (authenticationSuccess) {
		        if (qName.equals(USER)) {
		        	user = currentText.toString().trim();
		        }
		        if (qName.equals(PROXY_GRANTING_TICKET)) {
		        	pgtIou = currentText.toString().trim();
		        }
			} else if (authenticationFailure) {
		        if (qName.equals(AUTHENTICATION_FAILURE)) {
		        	errorMessage = currentText.toString().trim();
		        }
			}
		}
 
		@Override
		public void endDocument() throws SAXException {
			// save values as appropriate
			if (authenticationSuccess) {
				ServiceTicketValidator.this.user = user;
				ServiceTicketValidator.this.pgtIou = pgtIou;
				ServiceTicketValidator.this.successfulAuthentication = true;
			} else if (authenticationFailure) {
				ServiceTicketValidator.this.errorMessage = errorMessage;
				ServiceTicketValidator.this.errorCode = errorCode;
				ServiceTicketValidator.this.successfulAuthentication = false;
			} else {
				throw new SAXException("no indication of success or failure from CAS");
			}
		}
	}

	
	protected void clear() {
		user = pgtIou = errorMessage = null;
		successfulAuthentication = false;
	}
	
    /** 
     * Retrieve the contents from the given URL as a String, assuming the
     * URL's server matches what we expect it to match.
     */
	public static String retrieve(String url) throws IOException {
    	if (log.isTraceEnabled()){
    		log.trace("entering retrieve(" + url + ")");
    	}
        BufferedReader r = null;
        try {
            URL u = new URL(url);
            URLConnection uc = u.openConnection();
            uc.setRequestProperty("Connection", "close");
            r = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String line;
            StringBuffer buf = new StringBuffer();
            while ((line = r.readLine()) != null) {
                buf.append(line + "\n");
            }
            return buf.toString();
        } finally {
            try {
                if (r != null)
                    r.close();
            } catch (IOException ex) {
                log.error(ex.getMessage(), ex);
            }
        }
    }
}
