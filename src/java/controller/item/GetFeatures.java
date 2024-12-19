package controller.item;


import com.google.gson.Gson;
import dto.CategoryDTO;
import dto.ItemStatusDTO;
import dto.response.FeaturesResponseDTO;
import entity.Category;
import entity.City;
import entity.ItemStatus;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.Session;
import util.HibernateUtil;


@WebServlet(name = "GetFeatures", urlPatterns = {"/GetFeatures"})
public class GetFeatures extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            
            Criteria criteria = session.createCriteria(Category.class);
            List<Category> categoryList = criteria.list();

            Criteria criteria1 = session.createCriteria(ItemStatus.class);
            List<ItemStatus> itemStatusList = criteria1.list();

            List<CategoryDTO> categoryDTOList = new ArrayList<>();

            for (Category category : categoryList) {
                CategoryDTO categoryDTO = new CategoryDTO();
                categoryDTO.setId(category.getId());
                categoryDTO.setName(category.getName());
                categoryDTOList.add(categoryDTO);
            }

            List<ItemStatusDTO> itemStatusesDTOList = new ArrayList<>();

            for (ItemStatus itemStatus : itemStatusList) {
                ItemStatusDTO itemStatusDTO = new ItemStatusDTO();
                itemStatusDTO.setId(itemStatus.getId());
                itemStatusDTO.setStatus(itemStatus.getStatus());

                itemStatusesDTOList.add(itemStatusDTO);
            }
            
            Criteria criteria3 = session.createCriteria(City.class);
            List<City> cityList = criteria3.list();

            FeaturesResponseDTO featuresResponseDTO = new FeaturesResponseDTO();
            featuresResponseDTO.setCategoryList(categoryDTOList);
            featuresResponseDTO.setItemStatusList(itemStatusesDTOList);
            featuresResponseDTO.setCityList(cityList);
            
            Gson gson = new Gson();
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(featuresResponseDTO));

        } catch (Exception e) {
            e.printStackTrace();

        }

    }


}
