package controller.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.CategoryDTO;
import dto.response.ResponseDTO;
import entity.Category;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import util.HibernateUtil;

/**
 *
 * @author ksoff
 */
@WebServlet(name = "GetCategories", urlPatterns = {"/api/GetCategories"})
public class GetCategories extends HttpServlet {

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
                    .map(category -> {
                        if (category.getParent() != null) {
                            return new CategoryDTO(category.getId(), category.getName(), category.getParent().getName());
                        }
                        return new CategoryDTO(category.getId(), category.getName());
                    })
                    .collect(Collectors.toList());
            jsonObject.add("categoryList", gson.toJsonTree(categoryDTOs));

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
