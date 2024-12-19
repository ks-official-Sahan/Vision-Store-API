package controller.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.CategoryDTO;
import dto.ItemDTO;
import dto.response.ResponseDTO;
import entity.Category;
import entity.Item;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import util.HibernateUtil;

@WebServlet(name = "LoadItems", urlPatterns = {"/api/LoadItems"})
public class LoadItems extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        ResponseDTO responseDTO = new ResponseDTO();
        Gson gson = new Gson();

        JsonObject jsonObject = new JsonObject();

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            //get category list from DB
            Criteria criteria1 = session.createCriteria(Category.class);
            List<Category> categoryList = criteria1.list();

            List<CategoryDTO> categoryDTOs = categoryList.stream()
                    .map(category -> new CategoryDTO(category.getId(), category.getName()))
                    .collect(Collectors.toList());
            jsonObject.add("categoryList", gson.toJsonTree(categoryDTOs));

            //get product list from DB
            Criteria criteria2 = session.createCriteria(Item.class);

            //Get latest product
            criteria2.addOrder(Order.desc("id"));
            jsonObject.addProperty("allProductCount", criteria2.list().size());

            //set product range
            criteria2.setFirstResult(0);
            criteria2.setMaxResults(10);

            List<Item> itemList = criteria2.list();

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

            jsonObject.add("itemList", gson.toJsonTree(itemDTOs));

            responseDTO.setData(jsonObject);
            responseDTO.setMessage("success");
            responseDTO.setStatus(true);

            session.close();

        } catch (Exception e) {
            responseDTO.setMessage("server error");
            responseDTO.setCode(500);
        }

        response.setContentType("application/json");
        response.getWriter().write(gson.toJson(responseDTO));
    }
}
