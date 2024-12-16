/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.cart;

import com.google.gson.Gson;
import dto.CartDTO;
import dto.request.UserDTO;
import dto.response.ResponseDTO;
import entity.Cart;
import entity.Item;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;
import util.Validations;


@WebServlet(name = "AddToCart", urlPatterns = {"/AddToCart"})
public class AddToCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResponseDTO responseDTO = new ResponseDTO();
        Gson gson = new Gson();
        int itemId = Integer.valueOf(request.getParameter("itemId"));
        String qty = request.getParameter("qty");
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            HttpSession httpSession = request.getSession();

            if (!Validations.isInteger(qty)) {
                responseDTO.setMessage("The requested qty is not available");
            } else if (Integer.valueOf(qty) <= 0) {
                responseDTO.setMessage("The requested qty is not available");
            } else {

                Item item = (Item) session.get(Item.class, itemId);

                if (httpSession.getAttribute("user") != null) {

                    int reqQty = Integer.valueOf(qty);

                    //DB CART
                    UserDTO userDTO = (UserDTO) httpSession.getAttribute("user");

                    Criteria criteria1 = session.createCriteria(User.class);
                    criteria1.add(Restrictions.eq("email", userDTO.getEmail()));
                    User user = (User) criteria1.uniqueResult();

                    Criteria criteria2 = session.createCriteria(Cart.class);
                    criteria2.add(Restrictions.eq("user", user));
                    criteria2.add(Restrictions.eq("item", item));

                    if (criteria2.list().isEmpty()) {

                        //Not cart this item and user
                        if (item.getQuantity() >= reqQty) {

                            Cart cart = new Cart();
                            cart.setQty(reqQty);
                            cart.setItem(item);
                            cart.setUser(user);

                            session.save(cart);
                            transaction.commit();

                        } else {

                            responseDTO.setMessage("The requested qty is not available");
                        }

                    } else {

                        // found cart this item and user
                        Cart cart = (Cart) criteria2.list().get(0);

                        if (item.getQuantity() >= (cart.getQty() + reqQty)) {

                            cart.setQty(cart.getQty() + reqQty);
                            session.update(cart);
                            transaction.commit();

                        } else {

                            responseDTO.setMessage("The requested qty is not available");
                        }

                    }

                } else {
                    //SESSION CART
                    int reqQty = Integer.valueOf(qty);

                    if (httpSession.getAttribute("sessionCart") == null) {

                        if (item.getQuantity() >= reqQty) {
                            // new session cart
                            HashMap<Integer, CartDTO> sessionCartMap = new HashMap<>();
                            CartDTO cartDTO = new CartDTO();
                            cartDTO.setQty(reqQty);
                            cartDTO.setItem(item);

                            sessionCartMap.put(item.getId(), cartDTO);

                            httpSession.setAttribute("sessionCart", sessionCartMap);

                        } else {

                            responseDTO.setMessage("The requested qty is not available");

                        }

                    } else {

                        // find session cart
                        HashMap<Integer, CartDTO> sessionCartMap = (HashMap<Integer, CartDTO>) httpSession.getAttribute("sessionCart");

                        if (sessionCartMap.containsKey(itemId)) {
                            //this item find session cart

                            CartDTO cartDTO = sessionCartMap.get(itemId);
                            
                            if(item.getQuantity()>=(cartDTO.getQty()+reqQty)){
                                
                                cartDTO.setQty(reqQty);
                            
                            }else{
                              responseDTO.setMessage("The requested qty is not available");
                            
                            }

                        } else {

                            //this item not a session cart
                            if (reqQty <= item.getQuantity()) {

                                CartDTO cartDTO = new CartDTO();
                                cartDTO.setQty(reqQty);
                                cartDTO.setItem(item);

                                sessionCartMap.put(itemId, cartDTO);

                            } else {
                                responseDTO.setMessage("The requested qty is not available");
                            }

                        }

                    }
                }

            }

        } catch (Exception e) {
            responseDTO.setMessage("Server error");
            responseDTO.setCode(500);
        }
    }

}
