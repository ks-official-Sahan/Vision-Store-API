package controller.auth;

import com.google.gson.Gson;
import dto.OwnerDTO;
import dto.request.OwnerLoginDTO;
import dto.response.ResponseDTO;
import entity.Shop;
import entity.Owner;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.MeasuringType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

@WebServlet(name = "OwnerLogin", urlPatterns = {"/OwnerLogin"})
public class OwnerLogin extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResponseDTO responseDTO = new ResponseDTO();
        Gson gson = new Gson();
        OwnerLoginDTO loginDTO = gson.fromJson(request.getReader(), OwnerLoginDTO.class);

        if (loginDTO.getEmail().isEmpty()) {

            responseDTO.setMessage("Please enter your Email");

        } else if (loginDTO.getOwnerCode().isEmpty()) {
            responseDTO.setMessage("Please enter your Key");
        } else {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Criteria criteria = session.createCriteria(Owner.class);
            criteria.add(Restrictions.eq("email", loginDTO.getEmail()));
            criteria.add(Restrictions.eq("ownerCode", loginDTO.getOwnerCode()));

            if (!criteria.list().isEmpty()) {
                Owner owner = (Owner) criteria.list().get(0);
                
                if(!owner.getVerification().equals("verified")){
                   request.getSession().setAttribute("email", loginDTO.getEmail());
                    responseDTO.setMessage("Not verified");
               
                }else{
                
                  Criteria gardenCriteria = session.createCriteria(Shop.class);
                  gardenCriteria.add(Restrictions.eq("owner", owner));
                  
                  Shop garden = (Shop) gardenCriteria.list().get(0);
                  request.getSession().setAttribute("garden", garden);
                  responseDTO.setMessage("sucess");
                  responseDTO.setStatus(true);
                }
                
            } else {

                responseDTO.setMessage("Invalid details! please try again");
            }
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseDTO));

    }

}
