package santamaria.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import santamaria.util.AppConstant;

public class AuthenticationFilter implements Filter {

	@SuppressWarnings("unused")
	private FilterConfig config;
	
	@Override
	public void destroy() {
		this.config = null;
	}
	
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {		
		HttpSession session = ((HttpServletRequest) request).getSession();
		String authKey = (String) session.getAttribute(AppConstant.AUTH_KEY);		
		if (authKey == null || authKey.isEmpty()) {
			HttpServletRequest reqt = (HttpServletRequest) request;
			HttpServletResponse resp = (HttpServletResponse) response;			
			resp.sendRedirect(reqt.getContextPath());
		} 
		chain.doFilter(request, response);
	}
	
	@Override
	public void init(FilterConfig config) throws ServletException {
		this.config = config;
	}
	
}
