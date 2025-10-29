package br.com.paulopinheiro.springmvc.filters;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter(filterName = "DemoFilter", urlPatterns = {"/test/*"})
public class DemoFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println(filterConfig);
        // Filter Config can be used to get Servlet Context or init parameters of the Filter
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fc) throws IOException, ServletException {
        System.out.println("Do Filter");
        fc.doFilter(request, response);
    }

    @Override
    public void destroy() {
        System.out.println("Destroy method invocation from Web Filter");
    }
}
