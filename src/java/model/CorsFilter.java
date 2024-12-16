package model;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


@WebFilter(filterName="CorsFilter", urlPatterns={"/*"})
public class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //Process when Filter init
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        //write code here
        
        HttpServletRequest req = (HttpServletRequest) request;        
        HttpServletResponse res = (HttpServletResponse) response;
        
        //APPLY CORS HEADERS
        Bcors.setCors(req, res);
        
        //FOR OPTIONS REQUEST, END THE PROCESSING EARLY
        if("OPTIONS".equalsIgnoreCase(req.getMethod())){
            res.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        chain.doFilter(request, response);
        System.out.println("CorsFilter Added To: " + Bcors.getInstance().getAllowedOrigins());
        System.out.println("CorsFilter Added To: " + Bcors.getInstance().getAllowedOrigins());
    }

    @Override
    public void destroy() {
        //Process when Filter destroy
    }

}
