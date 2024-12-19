package controller.cart;

import com.google.gson.Gson;
import dto.CartDTO;
import dto.CartItemDTO;
import dto.request.UserDTO;
import dto.response.ResponseDTO;
import entity.Cart;
import entity.User;
import java.io.IOException;
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

@WebServlet(name = "GetAllCart", urlPatterns = {"/api/GetAllCart"})
public class GetAllCart extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Gson gson = new Gson();

        ResponseDTO responseDTO = new ResponseDTO();

        ArrayList<CartItemDTO> cartItemList = new ArrayList<>();

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            String email = "";
            boolean isUser = false;

            if (request.getSession().getAttribute("user") != null) {
                UserDTO userDTO = (UserDTO) request.getSession().getAttribute("user");
                email = userDTO.getEmail();
                isUser = true;
            } else if (request.getParameter("email") != null) {
                email = request.getParameter("email");
                isUser = true;
            }

            if (isUser) {
                //login user
                Criteria criteria = session.createCriteria(User.class);
                criteria.add(Restrictions.eq("email", email));

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
                        cartItemDTO.setItemQty(cart.getItem().getQuantity());
                        // cartItemDTO.setItemName(cart.getItem().getTitle());
                        cartItemDTO.setItemName(cart.getItem().getName());
                        cartItemDTO.setItemImagePath(cart.getItem().getImagePath());
                        cartItemDTO.setQty(cart.getQty());

                        cartItemList.add(cartItemDTO);
                    }

                    responseDTO.setData(cartItemList);
                    responseDTO.setStatus(true);
                } else {
                    responseDTO.setMessage("Cart is empty");
                }

            } else {
                //session check
                if (request.getSession().getAttribute("sessionCart") != null) {
                    HashMap<Integer, CartDTO> sessionCartMap = (HashMap<Integer, CartDTO>) request.getSession().getAttribute("sessionCart");

                    if (!sessionCartMap.isEmpty()) {
                        for (Integer itemId : sessionCartMap.keySet()) {
                            CartDTO cartDTO = sessionCartMap.get(itemId);

                            CartItemDTO cartItemDTO = new CartItemDTO();

                            cartItemDTO.setCartId(itemId);
                            cartItemDTO.setId(itemId);
                            cartItemDTO.setItemId(itemId);
                            cartItemDTO.setItemName(cartDTO.getItem().getTitle());
                            cartItemDTO.setItemQty(cartDTO.getItem().getQuantity());
                            cartItemDTO.setItemImagePath(cartDTO.getItem().getImagePath());
                            cartItemDTO.setPrice(cartDTO.getItem().getPrice());
                            cartItemDTO.setQty(cartDTO.getQty());

                            cartItemList.add(cartItemDTO);
                        }
                        responseDTO.setData(cartItemList);
                        responseDTO.setStatus(true);
                    } else {
                        responseDTO.setMessage("Cart is empty");
                    }
                } else {
                    responseDTO.setMessage("Cart is empty");
                }
            }
        } catch (Exception e) {
            responseDTO.setMessage("server error");
            responseDTO.setCode(500);
        }
        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseDTO));
    }

}
