package controller.cart;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.request.UserDTO;
import dto.response.ResponseDTO;
import entity.Cart;
import entity.User;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

/**
 *
 * @author ksoff
 */
@WebServlet(name = "ProcessCart", urlPatterns = {"/api/ProcessCart"})
public class ProcessCart extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new Gson();

        ResponseDTO responseDTO = new ResponseDTO();

        try {
            JsonObject requestJsonObject = gson.fromJson(req.getReader(), JsonObject.class);

            String email = "";
            boolean isUser = false;

            if (req.getSession().getAttribute("user") != null) {
                UserDTO userDTO = (UserDTO) req.getSession().getAttribute("user");
                email = userDTO.getEmail();
                isUser = true;
                System.out.println("Session User");
            } else if (requestJsonObject.get("email") != null) {
                email = requestJsonObject.get("email").getAsString();
                isUser = true;
                System.out.println("Param User");
            }

            if (isUser) {

                //DB user
                Session session = HibernateUtil.getSessionFactory().openSession();

                Transaction transaction = session.beginTransaction();

                Criteria userCriteria = session.createCriteria(User.class);
                userCriteria.add(Restrictions.eq("email", email));
                User user = (User) userCriteria.uniqueResult();

                if (user != null) {
                    List<Cart> cartItemsFromRequest = gson.fromJson(requestJsonObject.get("cartItems").getAsJsonArray(),
                            new com.google.gson.reflect.TypeToken<List<Cart>>() {
                            }.getType());

                    // Fetch current cart items from the database
                    Criteria cartCriteria = session.createCriteria(Cart.class);
                    cartCriteria.add(Restrictions.eq("user", user));
                    List<Cart> cartListFromDb = cartCriteria.list();

                    // Create a set of IDs from the client cart list for quick lookup
                    Set<Integer> clientCartIds = cartItemsFromRequest.stream()
                            .map(Cart::getId)
                            .collect(Collectors.toSet());

                    // Process database cart items
                    for (Cart cartFromDb : cartListFromDb) {
                        if (clientCartIds.contains(cartFromDb.getId())) {
                            // Update quantity if item exists in client cart
                            Cart matchingCartFromRequest = cartItemsFromRequest.stream()
                                    .filter(cart -> cart.getId() == cartFromDb.getId())
                                    .findFirst()
                                    .orElse(null); 

                            if (matchingCartFromRequest != null) {
                                cartFromDb.setQty(matchingCartFromRequest.getQty());
                                session.update(cartFromDb);
                            }
                        } else {
                            // Remove items not present in the client cart
                            session.delete(cartFromDb);
                        }
                    }

                    transaction.commit();

                    responseDTO.setStatus(true);
                    responseDTO.setMessage("Cart updated successfully");
                    responseDTO.setCode(200);
                } else {
                    responseDTO.setMessage("User not found");
                    responseDTO.setCode(404);
                }

                session.close();
            } else {
                responseDTO.setMessage("User not signed in");
                responseDTO.setCode(401);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO.setMessage("server error: "+ e.getMessage());
            responseDTO.setCode(500);
        }

        resp.setContentType("application/json");
        resp.getWriter().write(gson.toJson(responseDTO));
    }
}
