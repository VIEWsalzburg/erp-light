package at.erp.light.view.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SecurityFilter implements Filter {

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {

		HttpServletRequest httpServletRequest = null;
		HttpServletResponse httpServletResponse = null;

		if (request instanceof HttpServletRequest) {
			if (response instanceof HttpServletResponse) {
				
				httpServletResponse = (HttpServletResponse) response;
				httpServletRequest = (HttpServletRequest) request;
				
				// if request is of type http, then redirect to https site
				if (request.getScheme().equals("http"))
				{
					// append https:// to the substring of requested url
					String url = "https://"+httpServletRequest.getRequestURL().substring(7);
					System.out.println("new url: "+url);
					httpServletResponse.sendRedirect(url);
					return;
				}
				
				
				
				//No page caching
				httpServletResponse.setHeader("Cache-Control", "no-cache");
				httpServletResponse.setDateHeader("Expires", 0);
				httpServletResponse.setHeader("Pragma", "No-cache");

				httpServletRequest = (HttpServletRequest) request;

				Object authenticated = httpServletRequest.getSession()
						.getAttribute("authenticated");

				if (authenticated != null && (boolean) authenticated) {
					//If authentication successful
					filterChain.doFilter(request, response);
					return;
				} else {
					//If authentication unsuccessful
					
					httpServletResponse.setContentType("text/html");
					httpServletResponse.sendRedirect("/ERP-light");
					return;
				}
			}
		}
		
		filterChain.doFilter(request, response);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

}
