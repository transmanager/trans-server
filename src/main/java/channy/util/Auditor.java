package channy.util;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class Auditor extends HandlerInterceptorAdapter {
	private static final Logger logger = Logger.getLogger(Auditor.class);

	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		// System.out.println("pre handle");
		return true;
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
		// System.out.println("post handle");
		// TODO: Add audit operations
	}
}
