package controller.auth;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.AddressDTO;
import dto.CategoryDTO;
import dto.request.UserDTO;
import dto.response.ResponseDTO;
import entity.Address;
import entity.Category;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

/**
 *
 * @author ksoff
 */
@WebServlet(name = "GetAddress", urlPatterns = {"/api/GetAddress"})
public class GetAddress extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResponseDTO responseDTO = new ResponseDTO();
        Gson gson = new Gson();

        // JsonObject jsonObject = new JsonObject();
        try {

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

            if (isUser) {
                Session session = HibernateUtil.getSessionFactory().openSession();

                Criteria userCriteria = session.createCriteria(User.class);
                userCriteria.add(Restrictions.eq("email", email));
                User user = (User) userCriteria.uniqueResult();

                if (user != null) {

                    //get category list from DB
                    Criteria addressCriteria = session.createCriteria(Address.class);
                    addressCriteria.add(Restrictions.eq("user", user));
                    addressCriteria.addOrder(org.hibernate.criterion.Order.desc("id"));
                    addressCriteria.setMaxResults(1);

//                    List<Address> addressList = addressCriteria.list();
//                    List<AddressDTO> addressDTOs = addressList.stream()
//                            .map(address -> {
//                                return new AddressDTO(address.getId(), address.getFirstName(), address.getLastName(), address.getMobile(), address.getLine1(), address.getLine2(), address.getPostalCode(), address.getCity().getId());
//                            })
//                            .collect(Collectors.toList());
//                    jsonObject.add("addressList", gson.toJsonTree(addressDTOs));
                    if (addressCriteria.list().isEmpty()) {
                        //current address not found
                        responseDTO.setMessage("Current address not found. Please create a new address");
                    } else {
                        //complete
                        Address address = (Address) addressCriteria.list().get(0);
                        AddressDTO addressDTO = new AddressDTO(address.getId(), address.getFirstName(), address.getLastName(), address.getMobile(), address.getLine1(), address.getLine2(), address.getPostalCode(), address.getCity().getId());
                        // jsonObject.add("address", addressDTO);

                        responseDTO.setData(addressDTO);
                        responseDTO.setMessage("success");
                        responseDTO.setStatus(true);
                    }

                } else {
                    responseDTO.setMessage("User Not Found");
                }

                session.close();
            } else {
                responseDTO.setMessage("Not Signed In");
            }

        } catch (Exception e) {
            responseDTO.setMessage("server error");
            responseDTO.setCode(500);
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseDTO));

    }

}
