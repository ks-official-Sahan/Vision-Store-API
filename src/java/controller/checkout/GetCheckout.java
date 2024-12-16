/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.checkout;

import com.google.gson.Gson;
import dto.request.UserDTO;
import dto.response.GetCheckoutDTO;
import dto.response.ResponseDTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;


@WebServlet(name = "GetCheckout", urlPatterns = {"/GetCheckout"})
public class GetCheckout extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();
        ResponseDTO<GetCheckoutDTO> responseDTO = new ResponseDTO<>();

        try {

            HttpSession httpSession = request.getSession();
            Session session = HibernateUtil.getSessionFactory().openSession();

            if (httpSession.getAttribute("user") != null) {

                // login user
                UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");

                //DB user
                Criteria criteria1 = session.createCriteria(User.class);
                criteria1.add(Restrictions.eq("email", userDTO.getEmail()));
                User user = (User) criteria1.uniqueResult();

                //DB user last address
                Criteria criteria2 = session.createCriteria(Address.class);
                criteria2.add(Restrictions.eq("user", user));
                criteria2.addOrder(Order.desc("id"));
                criteria2.setMaxResults(1);
                Address address = (Address) criteria2.list().get(0);
                address.setUser(null);
                
                //DB city list
                Criteria criteria3 = session.createCriteria(City.class);
                List<City> citys =  criteria3.list();
                
                //user cart items from DB
                Criteria criteria4 = session.createCriteria(Cart.class);
                criteria4.add(Restrictions.eq("user", user));
                List<Cart> cartList = criteria4.list();
                
                for(Cart cart : cartList){
                
                    cart.setUser(null);
                    cart.getItem().getShop().setOwner(null);
                
                }
                
                GetCheckoutDTO getCheckoutDTO = new GetCheckoutDTO();
                getCheckoutDTO.setAddress(address);
                getCheckoutDTO.setCartList(cartList);
                getCheckoutDTO.setCityList(citys);
                
                responseDTO.setMessage("success");
                responseDTO.setData(getCheckoutDTO);
                
                
            } else {

                responseDTO.setMessage("Not signed in");

            }

        } catch (Exception e) {
            responseDTO.setMessage("server error");
        }

        response.getWriter().write(gson.toJson(responseDTO));
    }

}
