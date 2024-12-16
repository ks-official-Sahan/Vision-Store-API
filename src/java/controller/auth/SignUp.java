package controller.auth;

import com.google.gson.Gson;
import dto.request.UserDTO;
import dto.response.ResponseDTO;
import entity.User;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;
import util.Validations;
import util.threads.MailSender;

@WebServlet(name = "SignUp", urlPatterns = {"/auth/SignUp"})
public class SignUp extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ResponseDTO responseDTO = new ResponseDTO();
        Gson gson = new Gson();

        UserDTO userDTO = gson.fromJson(request.getReader(), UserDTO.class);
        // System.out.println(gson.toJson(userDTO));

        if (userDTO.getName().isEmpty()) {
            responseDTO.setMessage("Please enter your Name");
      
            responseDTO.setStatus(false);
        } else if (userDTO.getEmail().isEmpty()) {
            responseDTO.setMessage("Please enter your Email");
      
            responseDTO.setStatus(false);
        } else if (!Validations.isEmailValid(userDTO.getEmail())) {
            responseDTO.setMessage("Please enter your valid Email");
    
            responseDTO.setStatus(false);
        } else if (userDTO.getPassword().isEmpty()) {
            responseDTO.setMessage("Please enter your Password");
 
            responseDTO.setStatus(false);
        } else if (userDTO.getPassword().length() < 8) {
            responseDTO.setMessage("Password length low");
 
            responseDTO.setStatus(false);
        } else {

            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", userDTO.getEmail()));

            if (!criteria1.list().isEmpty()) {
                responseDTO.setMessage("User with this email already exists");

            } else {

                int code = (int) (Math.random() * 1000000);

                User user = new User();
                user.setName(userDTO.getName());
                user.setEmail(userDTO.getEmail());
                user.setPassword(userDTO.getPassword());
                user.setVerification(String.valueOf(code));
                user.setStatus(1);

                MailSender mailSender = new MailSender(user.getEmail(), "Vision Store", user.getVerification());
                mailSender.start();
                int uid = (int) session.save(user);

                session.beginTransaction().commit();
                session.close();

                // store session
                request.getSession().setAttribute("email", user.getEmail());
                request.getSession().setAttribute("type", "user");
                
                System.out.println(request.getSession().getAttribute("email"));
                System.out.println(request.getSession().getAttribute("type"));

                // Send Response
                responseDTO.setMessage("sucess");
                responseDTO.setStatus(true);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(responseDTO));
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("Sign Up");
    }
}
