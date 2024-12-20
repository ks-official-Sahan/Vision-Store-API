package controller.checkout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.request.UserDTO;
import dto.response.GetCheckoutDTO;
import dto.response.ResponseDTO;
import entity.Address;
import entity.Cart;
import entity.City;
import entity.Item;
import entity.OrderItem;
import entity.OrderStatus;
import entity.Orders;
import entity.User;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;
import util.PayHere;
import util.Validations;

@WebServlet(name = "ProcessCheckout", urlPatterns = {"/api/Checkout"})
public class ProcessCheckout extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Gson gson = new Gson();
        ResponseDTO<GetCheckoutDTO> responseDTO = new ResponseDTO<>();

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
            System.out.println(email);

            if (isUser) {
                //DB user
                Session session = HibernateUtil.getSessionFactory().openSession();

                Transaction transaction = session.beginTransaction();

                Criteria criteria1 = session.createCriteria(User.class);
                criteria1.add(Restrictions.eq("email", email));
                User user = (User) criteria1.uniqueResult();

                boolean isCurrentAddress = requestJsonObject.get("isCurrentAddress").getAsBoolean();
                String fname = requestJsonObject.get("firstName").getAsString();
                String lname = requestJsonObject.get("lastName").getAsString();
                String cityId = requestJsonObject.get("cityId").getAsString();
                String line1 = requestJsonObject.get("line1").getAsString();
                String line2 = requestJsonObject.get("line2").getAsString();
                String pcode = requestJsonObject.get("postalCode").getAsString();
                String mobile = requestJsonObject.get("mobileNumber").getAsString();

                //DB user last address
                if (isCurrentAddress) {

                    //get current address
                    Criteria criteria2 = session.createCriteria(Address.class);
                    criteria2.add(Restrictions.eq("user", user));
                    criteria2.addOrder(org.hibernate.criterion.Order.desc("id"));
                    criteria2.setMaxResults(1);

                    if (criteria2.list().isEmpty()) {
                        //current address not found
                        responseDTO.setMessage("Current address not found. Please create a new address");
                    } else {
                        //current address found
                        //complete
                        Address address = (Address) criteria2.list().get(0);

                        //complete checkout process
                        saveOrders(session, transaction, address, user, responseDTO);
                    }

                } else {

                    //create new address
                    if (fname.isEmpty()) {

                        responseDTO.setMessage("Please fill first name");

                    } else if (lname.isEmpty()) {

                        responseDTO.setMessage("Please fill last name");

                    } else if (!Validations.isInteger(cityId)) {

                        responseDTO.setMessage("Invalid city");

                    } else {

                        //check city from db
                        Criteria criteria3 = session.createCriteria(City.class);
                        criteria3.add(Restrictions.eq("id", Integer.valueOf(cityId)));

                        if (criteria3.list().isEmpty()) {

                            responseDTO.setMessage("Invalid city");

                        } else {

                            //city found
                            City city = (City) criteria3.list().get(0);

                            if (line1.isEmpty()) {

                                responseDTO.setMessage("Please fill address line 1");

                            } else if (line2.isEmpty()) {

                                responseDTO.setMessage("Please fill address line 2");

                            } else if (pcode.isEmpty()) {

                                responseDTO.setMessage("Please fill postal code");

                            } else if (pcode.length() != 5) {

                                responseDTO.setMessage("Invalid postal code");

                            } else if (!Validations.isInteger(pcode)) {

                                responseDTO.setMessage("Invalid postal code");

                            } else if (mobile.isEmpty()) {

                                responseDTO.setMessage("Please fill mobile number");

                            } else if (!Validations.VALIDATE_MOBILE(mobile)) {

                                responseDTO.setMessage("Invalid mobile number");

                            } else {

                                //create new address
                                Address address = new Address();
                                address.setCity(city);
                                address.setFirstName(fname);
                                address.setLastName(lname);
                                address.setLine1(line1);
                                address.setLine2(line2);
                                address.setMobile(mobile);
                                address.setPostalCode(pcode);
                                address.setUser(user);

                                session.save(address);

                                //complete checkout process
                                saveOrders(session, transaction, address, user, responseDTO);

                            }

                        }

                    }

                }

                // responseDTO.setMessage("success");
            } else {
                responseDTO.setMessage("Not signed in");
            }

        } catch (Exception e) {
            responseDTO.setMessage("server error");
            responseDTO.setCode(500);
        }

        res.setContentType("application/json");
        res.getWriter().write(gson.toJson(responseDTO));
    }

        private void saveOrders(Session session, Transaction transaction, Address address, User user, ResponseDTO responseDTO) {

        try {

            //create order to db
            Orders orders = new Orders();
            orders.setAddress(address);
            orders.setUser(user);

            int order_id = (int) session.save(orders);

            //get cart items
            Criteria criteria4 = session.createCriteria(Cart.class);
            criteria4.add(Restrictions.eq("user", user));
            List<Cart> cartList = criteria4.list();

            //get ordrer_status (5 - paid) from db
            OrderStatus order_Status = (OrderStatus) session.get(OrderStatus.class, 1); //1 - payment proccesing

            //create order item in db
            double amount = 0;
            String items = "";

            for (Cart cartItem : cartList) {

                //calculate amount
                amount += cartItem.getQty() * cartItem.getItem().getPrice();
                if (address.getCity().getId() == 1) {
                    amount += 350;
                } else {
                    amount += 500;
                }

                //get item details
                items += cartItem.getItem().getTitle() + "x" + cartItem.getQty();

                //get product
                Item product = cartItem.getItem();

                OrderItem order_Item = new OrderItem();
                order_Item.setOrder(orders);
                order_Item.setOrder_status(order_Status);
                order_Item.setProduct(product);
                order_Item.setQty(cartItem.getQty());

                session.save(order_Item);

                //update product qty in db
                product.setQuantity(product.getQuantity()- cartItem.getQty());
                session.update(product);

                //delete cart item in db
                session.delete(cartItem);
            }

            transaction.commit();
            
            //set payment data
            String merchant_id = "1227426";
            String formatedAmount = new DecimalFormat("0.00").format(amount);
            String currency = "LKR";
            String merchantSecret = PayHere.generateMD5("NTE2MzQxMjkyMzIyMDE4NzAzMTM0ODcxODIyMjE3NzIwNTIyMQ==");

            JsonObject payhere = new JsonObject();
            payhere.addProperty("merchant_id", merchant_id);

            payhere.addProperty("return_url", "");
            payhere.addProperty("cancel_url", "");
            payhere.addProperty("notify_url", "VerifyPayments");

            payhere.addProperty("first_name", address.getFirstName());
            payhere.addProperty("last_name", address.getLastName());
            payhere.addProperty("email", user.getEmail());
            payhere.addProperty("phone", address.getMobile());
            payhere.addProperty("address", address.getLine1() + ", " + address.getLine2());
            payhere.addProperty("city", address.getCity().getName());
            payhere.addProperty("country", "Sri-Lanka");
            payhere.addProperty("order_id", String.valueOf(order_id));
            payhere.addProperty("items", items);
            payhere.addProperty("currency", currency);
            payhere.addProperty("amount", formatedAmount);
            payhere.addProperty("sandbox", true);

            //generate md5
            String md5Hash = PayHere.generateMD5(merchant_id + order_id + formatedAmount + currency + merchantSecret);
            payhere.addProperty("hash", md5Hash);

            responseDTO.setStatus(true);
            responseDTO.setMessage("Checkout Completed");

            Gson gson = new Gson();
            responseDTO.setData(gson.toJsonTree(payhere)); //payhereJson

        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            responseDTO.setMessage("An error occurred. Please try again.");
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
