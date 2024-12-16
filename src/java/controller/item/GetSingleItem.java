/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.item;

import com.google.gson.Gson;
import dto.response.ResponseDTO;
import dto.response.SingleItemResponseDTO;
import entity.Category;
import entity.Item;
import entity.ItemStatus;
import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet(name = "GetSingleItem", urlPatterns = {"/GetSingleItem"})
public class GetSingleItem extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pid = Integer.parseInt(request.getParameter("pid"));
        ResponseDTO responseDTO = new ResponseDTO();
        Gson gson = new Gson();
        try {

            Session session = HibernateUtil.getSessionFactory().openSession();

            Item singleItem = (Item) session.get(Item.class, pid);
            singleItem.getShop().setOwner(null);
            ItemStatus itemStatus =(ItemStatus) session.get(ItemStatus.class, 3);

            Category category = singleItem.getCategory();
            
            Criteria criteria = session.createCriteria(Item.class);
            criteria.add(Restrictions.eq("category", category));
            criteria.add(Restrictions.eq("itemStatus", itemStatus));
            criteria.add(Restrictions.ne("id", singleItem.getId()));
            criteria.setMaxResults(5);
            
            List<Item> items = criteria.list();
            
            for(Item item : items){
            
                item.getShop().setOwner(null);
            
            }
            
            SingleItemResponseDTO singleItemResponseDTO = new SingleItemResponseDTO();
            singleItemResponseDTO.setSingleItem(singleItem);
            singleItemResponseDTO.setSimilarItems(items);
            
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(singleItemResponseDTO));

        } catch (Exception e) {

            e.printStackTrace();
            responseDTO.setMessage("server error");
            responseDTO.setCode(500);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(responseDTO));
        }

    }

}
