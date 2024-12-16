///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
// */
//package controller.item;
//
//import com.google.gson.Gson;
//import dto.response.ResponseDTO;
//import entity.Category;
//import entity.Shop;
//import entity.Item;
//import entity.ItemStatus;
//import java.io.File;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.PrintWriter;
//import java.nio.file.Files;
//import java.nio.file.StandardCopyOption;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.annotation.MultipartConfig;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import javax.servlet.http.Part;
//import model.MeasuringType;
//import org.hibernate.Session;
//import util.HibernateUtil;
//
///**
// *
// * @author yasithsandesh
// */
//@MultipartConfig
//@WebServlet(name = "ItemAdd", urlPatterns = {"/ItemAdd"})
//public class ItemAdd extends HttpServlet {
//
//    @Override
//    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//
//        Gson gson = new Gson();
//        ResponseDTO responseDTO = new ResponseDTO();
////        garden
//        try {
//
//            if (request.getSession().getAttribute("garden") != null) {
//                Shop garden = (Shop) request.getSession().getAttribute("garden");
//                String title = request.getParameter("title");
//                String measuringType = request.getParameter("measuringType");
//                String price = request.getParameter("price");
//                String description = request.getParameter("description");
//                String categoryId = request.getParameter("categoryId");
//                String qty = request.getParameter("qty");
//
//                Part image1 = request.getPart("image1");
////            Part image2 = request.getPart("image2");
////            Part image3 = request.getPart("image3");
//
//                if (title.isEmpty()) {
//                    responseDTO.setMessage("Please fill Title");
//                } else if (measuringType.isEmpty()) {
//                    responseDTO.setMessage("Please fill Measuring Type");
//                } else if (price.isEmpty()) {
//                    responseDTO.setMessage("Please fill Price");
//                } else if (description.isEmpty()) {
//                    responseDTO.setMessage("Please fill Discription");
//                } else if (categoryId.isEmpty()) {
//                    responseDTO.setMessage("Please Select Category");
//                } else if (qty.isEmpty()) {
//                    responseDTO.setMessage("Please fill Quantity");
//                } else {
//                    Session session = HibernateUtil.getSessionFactory().openSession();
//                    Category category = (Category) session.get(Category.class, Integer.valueOf(categoryId));
//
//                    if (category == null) {
//                        responseDTO.setMessage("Please Select Valid Category");
//                    } else {
//
//                        ItemStatus itemStatus = (ItemStatus) session.get(ItemStatus.class, 3);
//
//                        Item item = new Item();
//                        item.setTitle(title);
//                        item.setMeasuringType(MeasuringType.valueOf(measuringType));
//                        item.setPrice(Double.valueOf(price));
//                        item.setDescription(description);
//                        item.setShop(garden);
//                        item.setCategory(category);
//                        item.setQuantity(Integer.valueOf(qty));
//                        item.setItemStatus(itemStatus);
//
//                        int itemId = (int) session.save(item);
//                        session.beginTransaction().commit();
//
//                        //Product Image Upload
//                        String applicationPath = request.getServletContext().getRealPath("");
//
//                        File folder = new File(applicationPath + "//" + itemId);
//
//                        folder.mkdir();
//
//                        File file1 = new File(folder, "image1.png");
//                        InputStream inputStream1 = image1.getInputStream();
//                        Files.copy(inputStream1, file1.toPath(), StandardCopyOption.REPLACE_EXISTING);
//
////                    File file2 = new File(folder, "image2.png");
////                    InputStream inputStream2 = image2.getInputStream();
////                    Files.copy(inputStream2, file2.toPath(), StandardCopyOption.REPLACE_EXISTING);
////
////                    File file3 = new File(folder, "image3.png");
////                    InputStream inputStream3 = image3.getInputStream();
////                    Files.copy(inputStream3, file3.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                        responseDTO.setStatus(true);
//                        responseDTO.setMessage("New Item Added");
//
//                    }
//                }
//            } else {
//                responseDTO.setMessage("session time out");
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            responseDTO.setMessage("server error");
//
//        }
//
//        response.setContentType("application/json");
//        response.getWriter().write(gson.toJson(responseDTO));
//    }
//
//}
