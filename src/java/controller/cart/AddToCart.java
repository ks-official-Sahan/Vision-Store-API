package controller.cart;

import com.google.gson.Gson;
import dto.CartDTO;
import dto.request.UserDTO;
import dto.response.ResponseDTO;
import entity.Cart;
import entity.Item;
import entity.User;
import java.io.IOException;
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

@WebServlet(name = "AddToCart", urlPatterns = {"/api/AddToCart"})
public class AddToCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResponseDTO responseDTO = new ResponseDTO();
        Gson gson = new Gson();

        int itemId = Integer.parseInt(request.getParameter("itemId"));
        String qty = request.getParameter("qty");
        System.out.println(qty);
        System.out.println(itemId);
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();
            HttpSession httpSession = request.getSession();

            if (!Validations.isInteger(qty)) {
                responseDTO.setMessage("The requested quantity is not available");
            } else if (Integer.parseInt(qty) <= 0) {
                responseDTO.setMessage("The requested quantity is not available");
            } else {
                Item item = (Item) session.get(Item.class, itemId);

                String email = "";
                boolean isUser = false;

                if (request.getSession().getAttribute("user") != null) {
                    UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");
                    email = userDTO.getEmail();
                    isUser = true;
                    System.out.println("Session User");
                } else if (request.getParameter("email") != null) {
                    email = request.getParameter("email");
                    isUser = true;
                    System.out.println("Param User");
                }
                System.out.println(email);

                if (isUser) {
                    System.out.println("User Found");
                    // user is in session
                    int reqQty = Integer.parseInt(qty);

                    //DB USER
                    Criteria criteria1 = session.createCriteria(User.class);
                    criteria1.add(Restrictions.eq("email", email));
                    User user = (User) criteria1.uniqueResult();

                    //DB CART
                    Criteria criteria2 = session.createCriteria(Cart.class);
                    criteria2.add(Restrictions.eq("user", user));
                    criteria2.add(Restrictions.eq("item", item));

                    if (criteria2.list().isEmpty()) {
                        //Not cart this item and user
                        System.out.println("New Cart Item");
                        if (item.getQuantity() >= reqQty) {

                            Cart cart = new Cart();
                            cart.setQty(reqQty);
                            cart.setItem(item);
                            cart.setUser(user);

                            session.save(cart);
                            transaction.commit();

                            // responseDTO.setData(cartItemList);
                            responseDTO.setStatus(true);

                        } else {
                            responseDTO.setMessage("The requested quantity is not available");
                        }

                    } else {
                        // found cart this item and user
                        Cart cart = (Cart) criteria2.list().get(0);
                        System.out.println("Cart Item Found");

                        if (item.getQuantity() >= (cart.getQty() + reqQty)) {
                            cart.setQty(cart.getQty() + reqQty);
                            session.update(cart);
                            transaction.commit();

                            // responseDTO.setData(cartItemList);
                            responseDTO.setStatus(true);

                        } else {
                            responseDTO.setMessage("The requested quantity is not available");
                        }
                    }
                } else {
                    //SESSION CART
                    System.out.println("User Not Found");
                    int reqQty = Integer.valueOf(qty);

                    if (httpSession.getAttribute("sessionCart") == null) {
                        System.out.println("Session Carts Not Found");

                        if (item.getQuantity() >= reqQty) {
                            // new session cart
                            HashMap<Integer, CartDTO> sessionCartMap = new HashMap<>();
                            CartDTO cartDTO = new CartDTO();
                            cartDTO.setQty(reqQty);
                            cartDTO.setItem(item);

                            sessionCartMap.put(item.getId(), cartDTO);

                            httpSession.setAttribute("sessionCart", sessionCartMap);

                            // responseDTO.setData(cartItemList);
                            responseDTO.setStatus(true);
                        } else {
                            responseDTO.setMessage("The requested quantity is not available");
                        }

                    } else {
                        // find session cart
                        System.out.println("Session Carts Found");
                        HashMap<Integer, CartDTO> sessionCartMap = (HashMap<Integer, CartDTO>) httpSession.getAttribute("sessionCart");

                        if (sessionCartMap.containsKey(itemId)) {
                            //this item found on session cart
                            System.out.println("Session Cart Item Found");
                            CartDTO cartDTO = sessionCartMap.get(itemId);

                            if (item.getQuantity() >= (cartDTO.getQty() + reqQty)) {
                                cartDTO.setQty(reqQty);
                                // responseDTO.setData(cartItemList);
                                responseDTO.setStatus(true);
                            } else {
                                responseDTO.setMessage("The requested quantity is not available");
                            }

                        } else {
                            //this item not a session cart
                            System.out.println("New Session Cart Item");
                            if (reqQty <= item.getQuantity()) {

                                CartDTO cartDTO = new CartDTO();
                                cartDTO.setQty(reqQty);
                                cartDTO.setItem(item);

                                sessionCartMap.put(itemId, cartDTO);
                                responseDTO.setStatus(true);
                            } else {
                                responseDTO.setMessage("The requested quantity is not available");
                            }
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            responseDTO.setMessage("server error: "+ e.getMessage());
            responseDTO.setCode(500);
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseDTO));
    }
}
