package controller.auth;

import controller.item.*;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dto.CategoryDTO;
import dto.CityDTO;
import dto.response.ResponseDTO;
import entity.Category;
import entity.City;
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
@WebServlet(name = "GetCities", urlPatterns = {"/api/GetCities"})
public class GetCities extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResponseDTO responseDTO = new ResponseDTO();
        Gson gson = new Gson();
        
        JsonObject jsonObject = new JsonObject();
        
        try {
            System.out.println("Get Cities");
            Session session = HibernateUtil.getSessionFactory().openSession();

            //get category list from DB
            Criteria cityCriteria = session.createCriteria(City.class);
            List<City> cityList = cityCriteria.list();
            
            List<CityDTO> cityDTOs = cityList.stream()
                    .map(city -> new CityDTO(city.getId(), city.getName(), city.getId())
                    ).collect(Collectors.toList());
            jsonObject.add("cityList", gson.toJsonTree(cityDTOs));
            
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
