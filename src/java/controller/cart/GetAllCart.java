/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import com.google.gson.Gson;
import dto.CartDTO;
import dto.CartItemDTO;
import dto.request.UserDTO;
import dto.response.ResponseDTO;
import entity.Cart;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;


@WebServlet(name = "GetAllCart", urlPatterns = {"/GetAllCart"})
public class GetAllCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        ResponseDTO responseDTO = new ResponseDTO();

        ArrayList<CartItemDTO> cartItemList = new ArrayList<>();

        try {

            if (request.getSession().getAttribute("user") != null) {
                Session session = HibernateUtil.getSessionFactory().openSession();
                //login user

                UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");

                Criteria criteria = session.createCriteria(User.class);
                criteria.add(Restrictions.eq("email", userDTO.getEmail()));

                User user = (User) criteria.uniqueResult();

                Criteria criteria2 = session.createCriteria(Cart.class);
                criteria2.add(Restrictions.eq("user", user));

                if (!criteria2.list().isEmpty()) {

                    List<Cart> carts = criteria2.list();

                    for (Cart cart : carts) {

                        CartItemDTO cartItemDTO = new CartItemDTO();
                        cartItemDTO.setCartId(cart.getId());
                        cartItemDTO.setId(cart.getId());
                        cartItemDTO.setPrice(cart.getItem().getPrice());
                        cartItemDTO.setItemId(cart.getItem().getId());
                        cartItemDTO.setItemName(cart.getItem().getTitle());
                        cartItemDTO.setQty(cart.getQty());

                        cartItemList.add(cartItemDTO);

                    }

                } else {

                    responseDTO.setMessage("Cart is empty");
                }

            } else {
                //session cheak

                if (request.getSession().getAttribute("sessionCart") != null) {

                    HashMap<Integer, CartDTO> sessionCartMap = (HashMap<Integer, CartDTO>) request.getSession().getAttribute("sessionCart");

                    if (!sessionCartMap.isEmpty()) {
                        
                        for(Integer itemId:sessionCartMap.keySet()){
                        
                            CartDTO cartDTO = sessionCartMap.get(itemId);
                            
                            CartItemDTO cartItemDTO = new CartItemDTO();
                        
                            cartItemDTO.setCartId(itemId);
                            cartItemDTO.setId(itemId);
                            cartItemDTO.setItemId(itemId);
                            cartItemDTO.setItemName(cartDTO.getItem().getTitle());
                            cartItemDTO.setPrice(cartDTO.getItem().getPrice());
                            cartItemDTO.setQty(cartDTO.getQty());
                            
                            cartItemList.add(cartItemDTO);
                        
                            
                        }

                    } else {
                        responseDTO.setMessage("Cart is empty");

                    }

                } else {

                    responseDTO.setMessage("Cart is empty");
                }
            }

        } catch (Exception e) {
        }
    }

}
