package model;

import java.util.Arrays;
import java.util.List;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


public class Bcors {

    private static Bcors instance;
    private List<String> allowedOrigins;
    private List<String> allowedMethods;
    private String allowedHeaders = "Content-Type, Authorization";
    private String allowCredentials = "true";
    private String cookieDomain = "localhost";
    private int maxAge = 3600; // 1 hour max age for preflight response

    //DEFAULT CONFIGURATIONS
    //private static final List<String> defaultOrigins = Arrays.asList("http://localhost:3000","http://localhost:3001", "*");
    private static final List<String> defaultOrigins = Arrays.asList("*");
    private static final List<String> defaultMethods = Arrays.asList("POST", "GET", "OPTIONS");

    private Bcors(List<String> allowedOrigins, List<String> allowedMethods) {
        this.allowedOrigins = allowedOrigins;
        this.allowedMethods = allowedMethods;
    }

    /**
     * *
     * GET INSTANCE *
     */
    public static Bcors getInstance(List<String> allowedOrigins, List<String> allowedMethods) {
        if (instance == null) {
            instance = new Bcors(allowedOrigins, allowedMethods);
        }
        return instance;
    }

    public static Bcors getInstance() {
        if (instance == null) {
            instance = new Bcors(defaultOrigins, defaultMethods);
        }
        return instance;
    }

    //CORES SETTER
    public static void setCors(HttpServletRequest req, HttpServletResponse res) {
        Bcors corsFactory = Bcors.getInstance();
        corsFactory.setCORSHeaders(req, res);
        corsFactory.setSessionCookie(req, res);
    }

    public void setCORSHeaders(HttpServletRequest req, HttpServletResponse res) {
        String origin = req.getHeader("Origin");
        String requestMethod = req.getMethod();

        if (origin != null && this.getAllowedOrigins().contains(origin)) {
            res.setHeader("Access-Control-Allow-Origin", origin);
            res.setHeader("Access-Control-Allow-Methods", String.join(", ", this.getAllowedMethods()));
            res.setHeader("Access-Control-Allow-Headers", this.getAllowedHeaders());
            res.setHeader("Access-Control-Allow-Credentials", this.getAllowCredentials());
            res.setHeader("Access-Control-Max-Age", String.valueOf(this.getMaxAge())); // Set max age for preflight cache

            // Handle preflight requests
            if ("OPTIONS".equalsIgnoreCase(requestMethod)) {
                res.setStatus(HttpServletResponse.SC_OK);
                return; // Don't process further for preflight requests
            }
        } else {
            res.setHeader("Access-Control-Allow-Origin", "");
        }
    }

    public void setSessionCookie(HttpServletRequest req, HttpServletResponse res) {
        HttpSession session = req.getSession(false);
        if (session != null) {
            Cookie sessionCookie = new Cookie("JSESSIONID", session.getId());
            sessionCookie.setPath("/");

            // Configure SameSite=None and Secure attributes
            sessionCookie.setHttpOnly(true);
            sessionCookie.setSecure(req.isSecure()); //USE SECURE IF THE REQUEST IS OVER HTTPS
            sessionCookie.setMaxAge(-1); //SESSION COOKIE, SO IT WILL EXPIRE WHEN THE BROWSER IS CLOSED
            sessionCookie.setDomain(this.cookieDomain); //SET THE APPROPRIATE DOMAIN FOR CROSS-DOMAIN ACCESS

            res.addCookie(sessionCookie);
        }
    }

    public List<String> getAllowedOrigins() {
        return allowedOrigins;
    }

    public void setAllowedOrigins(List<String> allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public List<String> getAllowedMethods() {
        return allowedMethods;
    }

    public void setAllowedMethods(List<String> allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public String getAllowedHeaders() {
        return allowedHeaders;
    }

    public void setAllowedHeaders(String allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public String getAllowCredentials() {
        return allowCredentials;
    }

    public void setAllowCredentials(String allowCredentials) {
        this.allowCredentials = allowCredentials;
    }

    public String getCookieDomain() {
        return cookieDomain;
    }

    public void setCookieDomain(String cookieDomain) {
        this.cookieDomain = cookieDomain;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }
}
