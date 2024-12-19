package controller.item;

import com.google.gson.Gson;
import dto.ItemDTO;
import dto.response.ResponseDTO;
import dto.response.SingleItemResponseDTO;
import entity.Category;
import entity.Item;
import entity.ItemStatus;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import util.HibernateUtil;

@WebServlet(name = "GetSingleItem", urlPatterns = {"/api/GetSingleItem"})
public class GetSingleItem extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int pid = Integer.parseInt(request.getParameter("pid"));
        ResponseDTO responseDTO = new ResponseDTO();
        Gson gson = new Gson();
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();

            Item singleItem = (Item) session.get(Item.class, pid);

            if (singleItem == null) {
                responseDTO.setMessage("Item not found");
                responseDTO.setStatus(false);
                response.setContentType("application/json");
                response.getWriter().write(gson.toJson(responseDTO));
                return;
            }
            
            singleItem.setShop(null);

            ItemStatus itemStatus = (ItemStatus) session.get(ItemStatus.class, 1); // Active
            Category category = singleItem.getCategory();

            Criteria criteria = session.createCriteria(Item.class);
            criteria.add(Restrictions.eq("category", category));
            criteria.add(Restrictions.eq("itemStatus", itemStatus));
            criteria.add(Restrictions.ne("id", singleItem.getId()));
            criteria.setMaxResults(5);

            List<Item> items = criteria.list();

            SingleItemResponseDTO singleItemResponseDTO = new SingleItemResponseDTO();
            singleItemResponseDTO.setSingleItem(convertToDTO(singleItem));
            //singleItemResponseDTO.setSingleItem(singleItem);
            singleItemResponseDTO.setSimilarItems(items.stream()
                    .map(this::convertToDTO)
                    .collect(Collectors.toList()));

            responseDTO.setData(singleItemResponseDTO);
            responseDTO.setMessage("success");
            responseDTO.setStatus(true);

            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(responseDTO));

        } catch (Exception e) {

            e.printStackTrace();
            responseDTO.setMessage("server error");
            responseDTO.setCode(500);
            response.setContentType("application/json");
            response.getWriter().write(gson.toJson(responseDTO));
        }

    }

    private ItemDTO convertToDTO(Item item) {
        ItemDTO dto = new ItemDTO();
        dto.setId(item.getId());
        dto.setTitle(item.getTitle());
        dto.setName(item.getName());
        dto.setPrice(item.getPrice());
        dto.setDescription(item.getDescription());
        dto.setImagePath(item.getImagePath());
        dto.setQuantity(item.getQuantity());
        return dto;
    }

}
