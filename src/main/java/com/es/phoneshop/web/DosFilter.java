package com.es.phoneshop.web;

import com.es.phoneshop.service.DosService;
import com.es.phoneshop.service.impl.DosServiceImpl;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DosFilter implements Filter {

    private static final int TOO_MANY_REQUESTS_ERROR = 429;
    private DosService dosService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        dosService = DosServiceImpl.getInstance();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String ip = servletRequest.getRemoteAddr();

        if (dosService.isAllowed(ip)) {
            filterChain.doFilter(servletRequest, servletResponse);
        } else {
            ((HttpServletResponse) servletResponse).sendError(TOO_MANY_REQUESTS_ERROR, "Too many requests");
        }
    }

    @Override
    public void destroy() {
    }
}
