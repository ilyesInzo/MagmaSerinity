/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.magma.filter;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author inzo
 */
//@WebFilter(filterName = "NewFilter", urlPatterns = {"/*"})
//public class NewFilter implements Filter {
public class NewFilter{
  /*  public void init(FilterConfig filterConfig) {

    }

    public void destroy() {

    }

    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        System.err.println("cscs" + req.getRequestURI());

        filterChain.doFilter(request, response);
    }*/
}
