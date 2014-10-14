package channy.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ServiceResponseWrapper extends HttpServletResponseWrapper {

	public ServiceResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	private StringWriter sw = new StringWriter(1024);
	
	public PrintWriter getWriter() throws IOException {
		return new PrintWriter(sw);
	}
	
	public String toString() {
	    return sw.toString();
	  }
}
