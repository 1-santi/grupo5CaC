
package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.BookDAO;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import model.Book;

@WebServlet("/books")
public class BookServletController extends HttpServlet{
    
    private List<Book> bookList = new ArrayList();
     ObjectMapper mapper = new ObjectMapper();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
        String route = req.getParameter("action");
        System.out.println("Parametro "+route);
        switch(route){
            case "getAll":{
                res.setContentType("application/json; charset=UTF-8");
                bookList = BookDAO.seleccionar();
                
                for(Book book : bookList){
                    byte[] imagenBytes = book.getImagen();
                    String imagenBase64 = Base64.getEncoder().encodeToString(imagenBytes);
                    book.setImagenBase64(imagenBase64);
                }
                
                mapper.writeValue(res.getWriter(), bookList);
            }
            default:{
                System.out.println("Parametro no valido.");
            }
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res){
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res){
    }
    
}
