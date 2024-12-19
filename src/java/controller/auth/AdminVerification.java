package controller.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.response.ResponseDTO;
import entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.Role;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

@WebServlet(name = "Verification", urlPatterns = {"/auth/Verification"})
public class AdminVerification extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResponseDTO responseDTO = new ResponseDTO();

        Gson gson = new Gson();
        JsonObject dto = gson.fromJson(request.getReader(), JsonObject.class);

        String verification = dto.get("verification").getAsString();

        String email;
        if (request.getSession().getAttribute("email") != null) {
            email = request.getSession().getAttribute("email").toString();
            System.out.println("Session");
        } else {
            email = dto.get("email").getAsString();
            System.out.println("Parameter");
        }

        if (email != null) {

            // if (request.getSession().getAttribute("type") != null) {
            // String email = request.getSession().getAttribute("email").toString();
            Session session = HibernateUtil.getSessionFactory().openSession();

            //if (request.getSession().getAttribute("type").toString().equals("user")) {
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", email));

            if (!criteria1.list().isEmpty()) {
                User user = (User) criteria1.list().get(0);

                if (user.getRole() == Role.admin) {
                    String verificationStatus = user.getVerification();
                    if (verificationStatus.equalsIgnoreCase("verified")) {
                        responseDTO.setMessage("already verified");
                    } else {
                        criteria1.add(Restrictions.eq("verification", verification));
                        if (!criteria1.list().isEmpty()) {
                            user.setVerification("verified");

                            session.update(user);
                            session.beginTransaction().commit();

                            request.getSession().removeAttribute("email");
                            responseDTO.setMessage("success");
                            responseDTO.setStatus(true);

                            /* sending response twice may result in error on client */
                            // response.setContentType("application/json");
                            // response.getWriter().write(gson.toJson(responseDTO));
                        } else {
                            responseDTO.setMessage("Invalid Verification code");
                        }
                    }
                } else {
                    responseDTO.setMessage("You are not an Admin. Use regular Sign In");
                }
            }

        } else {
            responseDTO.setMessage("Cannot retrieve the email ⚠️ Please Sign In");
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseDTO));
    }

}
