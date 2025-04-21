package com.example.demo;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import org.apache.tomcat.util.http.parser.TE;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class TenantFilter implements Filter {

    private static final String TENANT_HEADER = "X-TenantID";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String tenantId = ((HttpServletRequest) request).getHeader(TENANT_HEADER);
        if (tenantId != null && !tenantId.isEmpty()) {
            TenantContext.setCurrentTenant(tenantId);
        } else {
            TenantContext.setCurrentTenant("public");
        }
        try {
            chain.doFilter(request, response);
        } finally {
            TenantContext.clear();
        }
    }
}

