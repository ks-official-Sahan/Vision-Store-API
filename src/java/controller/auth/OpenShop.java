//package controller.auth;
//
//import com.google.gson.Gson;
//import dto.OwnerDTO;
//import dto.request.GardenAddressDTO;
//import dto.request.GradenDTO;
//import dto.request.OpenShopDTO;
//import dto.response.ResponseDTO;
//import entity.City;
//import entity.Shop;
//import entity.ShopAddress;
//import entity.Owner;
//import java.io.IOException;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import org.hibernate.Criteria;
//import org.hibernate.Session;
//import org.hibernate.Transaction;
//import org.hibernate.criterion.Restrictions;
//import util.GenerateKey;
//import util.HibernateUtil;
//import util.Validations;
//import util.threads.MailSender;
//
//@WebServlet(name = "OpenShop", urlPatterns = {"/OpenShop"})
//public class OpenShop extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        ResponseDTO responseDTO = new ResponseDTO();
//        Gson gson = new Gson();
//        OpenShopDTO openShopDTO = gson.fromJson(request.getReader(), OpenShopDTO.class);
//
//        OwnerDTO ownerDTO = openShopDTO.getOwner();
//        GradenDTO gradenDTO = openShopDTO.getGraden();
//        GardenAddressDTO gardenAddressDTO = openShopDTO.getGradenAddress();
//
//        if (ownerDTO.getFirstName().isEmpty()) {
//
//           responseDTO.setMessage("Please enter your First Name");
//           
//        } else if (ownerDTO.getLastName().isEmpty()) {
//
//            responseDTO.setMessage("Please enter your Last Name");
//        } else if (ownerDTO.getEmail().isEmpty()) {
//
//            responseDTO.setMessage("");
//
//        } else if (!Validations.isEmailValid(ownerDTO.getEmail())) {
//            responseDTO.setMessage("");
//        } else if (ownerDTO.getMobile().isEmpty()) {
//            responseDTO.setMessage("");
//        } else if (gradenDTO.getGardenName().isEmpty()) {
//            responseDTO.setMessage("");
//        } else if (gradenDTO.getGardenName().isEmpty()) {
//            responseDTO.setMessage("");
//        } else if (gardenAddressDTO.getAddress().isEmpty()) {
//            responseDTO.setMessage("");
//        } else if (gardenAddressDTO.getPostalCode().isEmpty()) {
//            responseDTO.setMessage("");
//        } else if (gardenAddressDTO.getCityId().isEmpty()) {
//            responseDTO.setMessage("");
//        } else {
//
//            Session session = HibernateUtil.getSessionFactory().openSession();
//            Criteria criteria1 = session.createCriteria(Owner.class);
//            criteria1.add(Restrictions.eq("email", ownerDTO.getEmail()));
//
//            if (!criteria1.list().isEmpty()) {
//                responseDTO.setMessage("already register");
//            } else {
//
//                City city = (City) session.get(City.class, Integer.valueOf(gardenAddressDTO.getCityId()));
//
//                if (city == null) {
//
//                    responseDTO.setMessage("");
//
//                } else {
//
//                    int code = (int) (Math.random() * 1000000);
//
//                    Transaction transaction = session.beginTransaction();
//
//                    try {
//                        Owner owner = new Owner();
//                        owner.setFirstName(ownerDTO.getFirstName());
//                        owner.setLastName(ownerDTO.getLastName());
//                        owner.setEmail(ownerDTO.getEmail());
//                        owner.setVerification(String.valueOf(code));
//                        owner.setStatus(1);
//                        owner.setMobile(ownerDTO.getMobile());
//                        owner.setOwnerCode(GenerateKey.genetate(owner.getFirstName(), owner.getLastName()));
//
//                        int ownerId = (int) session.save(owner);
//                        
//                        owner = (Owner) session.get(Owner.class, ownerId);
//                        
//                        MailSender mailSender = new MailSender(owner.getEmail(), "Hello", owner.getVerification()+"=>Your Key"+owner.getOwnerCode());
//                        mailSender.start();
//                        
//                        ShopAddress gardenAddress = new ShopAddress();
//                        gardenAddress.setAddress(gardenAddressDTO.getAddress());
//                        gardenAddress.setPostalCode(gardenAddressDTO.getPostalCode());
//                        gardenAddress.setCity(city);
//                        int gD = (int) session.save(gardenAddress);
//                        gardenAddress = (ShopAddress) session.get(ShopAddress.class, gD);
//
//                        Shop garden = new Shop();
//                        garden.setShopName(gradenDTO.getGardenName());
//                        garden.setDescription(gradenDTO.getDescription());
//                        garden.setUser(owner);
//                        garden.setShopAddress(gardenAddress);
//
//                        session.save(garden);
//
//                        transaction.commit();
//
//                        request.getSession().setAttribute("email", owner.getEmail());
//                        request.getSession().setAttribute("type", "owner");
//                        
//                        responseDTO.setCode(200);
//                        responseDTO.setMessage("success");
//                        responseDTO.setStatus(true);
//                        response.getWriter().write(gson.toJson(responseDTO));
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        transaction.rollback();
//
//                    }
//                    session.close();
//
//               
//                }
//
//            }
//
//        }
//        
//
//
//    }
//
//}
