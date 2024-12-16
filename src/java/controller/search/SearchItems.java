/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller.search;

import com.google.gson.Gson;
import dto.request.ProductSearchDTO;
import dto.response.ProductSearchResponse;
import entity.Category;
import entity.Item;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;


@WebServlet(name = "SearchItems", urlPatterns = {"/SearchItems"})
public class SearchItems extends HttpServlet {

    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {

            ProductSearchDTO productSearchDTO = (ProductSearchDTO) gson.fromJson(request.getReader(), ProductSearchDTO.class);

            Session session = HibernateUtil.getSessionFactory().openSession();

            //get all items from DB
            Criteria criteria1 = session.createCriteria(Item.class);

            //add category fillter
            if (!productSearchDTO.getCategoryName().isEmpty()) {
                // category selected
                String categoryName = productSearchDTO.getCategoryName();

                //get category list DB
                Criteria criteria2 = session.createCriteria(Category.class);
                criteria2.add(Restrictions.eq("name", categoryName));
                Category category = (Category) criteria2.uniqueResult();

                //fillter products by category
                criteria1.add(Restrictions.eq("category", category));
            }

            //add freshness fillter
            if (productSearchDTO.getFreshness() != 0) {

                if (productSearchDTO.getFreshness() <= 10) {

                    // freshness selected
                    int freshness = productSearchDTO.getFreshness();

                    //fillter products by freshness
                    criteria1.add(Restrictions.eq("freshness", freshness));

                } else {

                    // not valid freshness days
                }

            }

            ////sort section start
            double priceRangeStart = productSearchDTO.getPriceRangeStart();
            double priceRangeEnd = productSearchDTO.getPriceRangeEnd();

            criteria1.add(Restrictions.ge("price", priceRangeStart));
            criteria1.add(Restrictions.le("price", priceRangeEnd));

            String sortText = productSearchDTO.getSortText();

            if (sortText.equals("Sort by Latest")) {
                criteria1.addOrder(Order.desc("id"));
            } else if (sortText.equals("Sort by Oldest")) {
                criteria1.addOrder(Order.asc("id"));
            } else if (sortText.equals("Sort by Name")) {
                criteria1.addOrder(Order.asc("title"));
            } else if (sortText.equals("Sort by Price")) {
                criteria1.addOrder(Order.asc("price"));
            }

            ////sort section end
            ProductSearchResponse productSearchResponse = new ProductSearchResponse();
            productSearchResponse.setAllItemCount(criteria1.list().size());

            //set item range
            criteria1.setFirstResult(0);
            criteria1.setMaxResults(6);

            // get item list
            List<Item> itemList = criteria1.list();

            for (Item item : itemList) {

                item.setShop(null);

            }

            productSearchResponse.setItems(itemList);

            response.setContentType("application/json");
            response.getWriter().print(gson.toJson(productSearchResponse));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {

            String text = request.getParameter("text");
            Session session = HibernateUtil.getSessionFactory().openSession();
            if (!text.isEmpty()) {

                Criteria criteria1 = session.createCriteria(Item.class);
                criteria1.add(Restrictions.like("title", "%" + text + "%"));

                List<Item> itemList = criteria1.list();

                response.setContentType("application/json");
                response.getWriter().print(gson.toJson(itemList));

            } else {

                Criteria criteria = session.createCriteria(Category.class);
                List<Category> categoryList = criteria.list();

                for (Category category : categoryList) {

                    for (Item item : category.getItemList()) {

                        item.setShop(null);

                    }

                }

                response.setContentType("application/json");
                response.getWriter().print(gson.toJson(categoryList));

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
