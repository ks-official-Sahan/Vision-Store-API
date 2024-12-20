package controller.auth;

import com.google.gson.Gson;
import dto.request.UserDTO;
import dto.request.UserLoginDTO;
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
import util.threads.MailSender;

@WebServlet(name = "Login", urlPatterns = {"/auth/Logon"})
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ResponseDTO responseDto = new ResponseDTO();

        Gson gson = new Gson();

        UserLoginDTO userLoginDTO = gson.fromJson(request.getReader(), UserLoginDTO.class);

        if (userLoginDTO.getEmail().isEmpty()) {
            responseDto.setMessage("Please enter your Email");

        } else if (userLoginDTO.getPassword().isEmpty()) {
            responseDto.setMessage("Please enter your Password");
        } else {
            Session session = HibernateUtil.getSessionFactory().openSession();

            Criteria criteria1 = session.createCriteria(User.class);
            criteria1.add(Restrictions.eq("email", userLoginDTO.getEmail()));
            criteria1.add(Restrictions.eq("password", userLoginDTO.getPassword()));

            if (!criteria1.list().isEmpty()) {

                User user = (User) criteria1.list().get(0);

                if (!user.getVerification().equals("verified")) {
                    request.getSession().setAttribute("email", user.getEmail());
                    request.getSession().setAttribute("type", "user");

                    responseDto.setMessage("unverified");

                    int code = (int) (Math.random() * 1000000);

                    user.setVerification(String.valueOf(code));

                    session.update(user);
                    session.beginTransaction().commit();

                    MailSender mailSender = new MailSender(user.getEmail(), "Vision Store", user.getVerification());
                    mailSender.start();

                    System.out.println(request.getSession().getAttribute("email"));
                    System.out.println(request.getSession().getAttribute("type"));

                } else {
                    UserDTO userDTO = new UserDTO(user.getName(), user.getEmail(), user.getPassword());

                    
                    // store session
                    // request.getSession().setAttribute("user", gson.toJson(userDTO));
                    request.getSession().setAttribute("user", userDTO);
                    request.getSession().removeAttribute("email");
                    request.getSession().removeAttribute("type");

                    // send response
                    userDTO.setPassword("");
                    responseDto.setData(userDTO); 
                    responseDto.setMessage("login sucess");
                    responseDto.setStatus(true);
                }

            } else {
                responseDto.setMessage("Invalid credentials! please check your email and password");
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseDto));

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write("Sign In");
    }

}
