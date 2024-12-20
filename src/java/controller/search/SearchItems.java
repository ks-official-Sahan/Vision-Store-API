package controller.search;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.ItemDTO;
import dto.request.ProductSearchDTO;
import dto.response.ProductSearchResponse;
import dto.response.ResponseDTO;
import entity.Category;
import entity.Item;
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
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

@WebServlet(name = "SearchItems", urlPatterns = {"/api/SearchItems"})
public class SearchItems extends HttpServlet {

    private Gson gson;

    @Override
    public void init() throws ServletException {
        this.gson = new Gson();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
//            System.out.println(request.getReader().readLine());
            ProductSearchDTO productSearchDTO = gson.fromJson(request.getReader(), ProductSearchDTO.class);

            Session session = HibernateUtil.getSessionFactory().openSession();

            //get all items from DB
            Criteria itemCriteria = session.createCriteria(Item.class);

            if (!productSearchDTO.getSearchText().isEmpty()) {
                itemCriteria.add(Restrictions.like("title", "%" + productSearchDTO.getSearchText() + "%"));
            }

            //add category fillter
            if (!productSearchDTO.getCategoryName().isEmpty()) {
                // category selected
                String categoryName = productSearchDTO.getCategoryName();

                //get category list DB
                Criteria criteria2 = session.createCriteria(Category.class);
                criteria2.add(Restrictions.eq("name", categoryName));
                Category category = (Category) criteria2.uniqueResult();

                //fillter products by category
                itemCriteria.add(Restrictions.eq("category", category));
            }

            ////sort section start
            double priceRangeStart = productSearchDTO.getPriceRangeStart();
            double priceRangeEnd = productSearchDTO.getPriceRangeEnd();

            if (productSearchDTO.getPriceRangeStart() > 0) {
                itemCriteria.add(Restrictions.ge("price", priceRangeStart));
            }
            if (productSearchDTO.getPriceRangeEnd() > 0) {
                itemCriteria.add(Restrictions.le("price", priceRangeEnd));
            }

            String sortText = productSearchDTO.getSortText();

            if (sortText.equals("Sort by Latest")) {
                System.out.println("Sort Text L here");
                itemCriteria.addOrder(Order.desc("id"));
            } else if (sortText.equals("Sort by Oldest")) {
                itemCriteria.addOrder(Order.asc("id"));
            } else if (sortText.equals("Sort by Name ASC")) {
                itemCriteria.addOrder(Order.asc("title"));
            } else if (sortText.equals("Sort by Name DESC")) {
                itemCriteria.addOrder(Order.desc("title"));
            } else if (sortText.equals("Sort by Price ASC")) {
                itemCriteria.addOrder(Order.asc("price"));
            } else if (sortText.equals("Sort by Price DESC")) {
                itemCriteria.addOrder(Order.desc("price"));
            }
            ////sort section end

            ProductSearchResponse productSearchResponse = new ProductSearchResponse();
            productSearchResponse.setAllItemCount(itemCriteria.list().size());

            //set item range
            itemCriteria.setFirstResult(0);
            itemCriteria.setMaxResults(6);

            // get item list
            List<Item> itemList = itemCriteria.list();

            for (Item item : itemList) {
                item.setShop(null);
            }

            List<ItemDTO> itemDTOs = itemList.stream()
                    .map(item -> new ItemDTO(
                    item.getId(),
                    item.getTitle(),
                    item.getName(),
                    item.getPrice(),
                    item.getDescription(),
                    item.getImagePath(),
                    item.getQuantity(),
                    item.getCategory().getName()))
                    .collect(Collectors.toList());

            // productSearchResponse.setItems(itemList);
            productSearchResponse.setItemList(itemDTOs);

            // response.getWriter().print(gson.toJson(productSearchResponse));
            responseDTO.setData(productSearchResponse);
            responseDTO.setStatus(true);
        } catch (Exception e) {
            responseDTO.setMessage("server error: " + e.getMessage());
            responseDTO.setCode(500);
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseDTO));
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResponseDTO responseDTO = new ResponseDTO();

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

                // response.getWriter().print(gson.toJson(categoryList));
                responseDTO.setData(categoryList);
                responseDTO.setStatus(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            responseDTO.setMessage("server error");
            responseDTO.setCode(500);
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseDTO));
    }
}
