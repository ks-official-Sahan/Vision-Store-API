package controller.checkout;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import entity.OrderStatus;
import entity.Orders;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.PayHere;

/**
 *
 * @author ksoff
 */
@WebServlet(name = "VerifyPayments", urlPatterns = {"/api/VerifyPayments"})
public class VerifyPayments extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        Gson gson = new Gson();
        JsonObject requestJson = gson.fromJson(req.getReader(), JsonObject.class);

        String merchantSecret = "NTE2MzQxMjkyMzIyMDE4NzAzMTM0ODcxODIyMjE3NzIwNTIyMQ==";
        String merchant_id = "1227426"; // Replace with environment variable
        String order_id = requestJson.get("order_id").getAsString();
        String amount = requestJson.get("amount").getAsString();
        String currency = requestJson.get("currency").getAsString();
        String receivedHash = requestJson.get("hash").getAsString();

        // Verify hash
        String generatedHash = PayHere.generateMD5(merchant_id + order_id + amount + currency + merchantSecret);

        if (generatedHash.equals(receivedHash)) {
            // Update order status in the database
            Session session = HibernateUtil.getSessionFactory().openSession();
            Transaction transaction = session.beginTransaction();

            Orders order = (Orders) session.get(Orders.class, Integer.parseInt(order_id));
            if (order != null) {
                OrderStatus status = (OrderStatus) session.get(OrderStatus.class, 5); // Paid status
                //order.setOrderStatus(status);
                session.update(order);
                transaction.commit();

                res.setStatus(HttpServletResponse.SC_OK);
                res.getWriter().write("Payment verified successfully.");
            } else {
                res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                res.getWriter().write("Invalid order.");
            }
        } else {
            res.setStatus(HttpServletResponse.SC_FORBIDDEN);
            res.getWriter().write("Invalid hash.");
        }
    }
}
