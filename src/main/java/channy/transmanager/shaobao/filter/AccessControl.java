package channy.transmanager.shaobao.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.StringTokenizer;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import channy.transmanager.shaobao.config.Configuration;
import channy.transmanager.shaobao.data.TokenDao;
import channy.transmanager.shaobao.model.Token;
import channy.transmanager.shaobao.model.user.User;
import channy.transmanager.shaobao.service.user.UserService;
import channy.util.JsonResponse;

public class AccessControl implements Filter {
	private ArrayList<String> loginExclusion = null;
	private ArrayList<String> timeoutExclusion = null;

	public void destroy() {

	}

	public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		request.setCharacterEncoding("UTF-8");
		HttpServletResponse response = (HttpServletResponse) resp;
		response.setCharacterEncoding("UTF-8");
		String path = request.getContextPath();
		String url = request.getServletPath();
		System.out.println("url=" + url);
		// String action = request.getParameter("action");

		if (isLoginNeeded(request, url)) {
			System.out.println("Login needed");
			redirect(response, path, "");
			return;
		}

		try {
			if (url.startsWith("/mobile") && !url.equals("/mobile/auth")) {
				touch(request);
			}
			
			//PrintWriterWrapper writer = new PrintWriterWrapper(response.getWriter());
			//ServiceResponseWrapper wrapper = new ServiceResponseWrapper(response);
			chain.doFilter(req, resp);
			//System.out.println(wrapper.toString());
		} catch (Exception e) {
			if (isAjax(request)) {
				resp.setContentType("application/json");
				resp.getWriter().write(new JsonResponse(e.getMessage()).generate());
			} else {
				resp.getWriter().write(e.getLocalizedMessage());
			}
			System.out.println(e.getStackTrace());
		}
	}

	public void init(FilterConfig config) throws ServletException {
		Configuration.context = config.getInitParameter("context");
		Configuration.update();

		String urls = config.getInitParameter("login exceptions");
		StringTokenizer token = new StringTokenizer(urls, ",");
		loginExclusion = new ArrayList<String>();
		while (token.hasMoreTokens()) {
			loginExclusion.add(token.nextToken());
		}

		String ajax = config.getInitParameter("timeout exceptions");
		timeoutExclusion = new ArrayList<String>();
		token = new StringTokenizer(ajax, ",");
		while (token.hasMoreElements()) {
			timeoutExclusion.add(token.nextToken());
		}

		System.out.println("AccessControl completed initialization");
	}

	private void redirect(HttpServletResponse response, String basePath, String page) {
		try {
			PrintWriter pw = response.getWriter();
			pw.println("<html>");
			pw.println("<script type=\"text/javascript\">");
			StringBuffer buffer = new StringBuffer();
			buffer.append("window.open(\"");
			buffer.append(basePath + "/" + page);
			buffer.append("\", \"_top\")");
			pw.println(buffer.toString());
			pw.println("</script>");
			pw.println("</html>");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private boolean isAjax(HttpServletRequest request) {
		String ajax = request.getHeader("X-Requested-With");
		if (ajax != null && ajax.equals("XMLHttpRequest")) {
			return true;
		}

		return false;
	}

	private boolean isLoginNeeded(HttpServletRequest request, String url) {
		HttpSession session = request.getSession();
		if (session == null) {
			return true;
		}
		if (session.getAttribute("currentUser") == null && !isExcludedFromLogin(url)) {
			return true;
		}

		return false;
	}

	private boolean isExcludedFromLogin(String url) {
		for (String u : loginExclusion) {
			if (u.equals(url)) {
				return true;
			} else if (!u.equals("/") && url.startsWith(u.replace("**", ""))) {
				return true;
			}
		}

		return false;
	}

	private void touch(HttpServletRequest request) {
		UserService userService = new UserService();
		@SuppressWarnings("unchecked")
		Map<String, String> params = request.getParameterMap();
		for (String key : params.keySet()) {
			System.out.println(String.format("%s=%s", key, request.getParameter(key)));
		}
		
		String id = request.getParameter("employeeId");
		if (id == null) {
			return;
		}
		User user = userService.getByEmployeeId(id);
		if (user == null) {
			return;
		}
		Token token = TokenDao.getByUser(user);
		if (token == null) {
			return;
		}
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, 120);
		Date expiration = calendar.getTime();
		token.setExpiration(expiration);
		TokenDao.update(token);
	}
}
