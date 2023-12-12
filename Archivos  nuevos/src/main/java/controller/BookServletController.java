
package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.BookDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import model.Book;
import org.apache.commons.io.IOUtils;

@WebServlet("/books")
@MultipartConfig(
        location = "/Media/Temp",
        fileSizeThreshold = 1024 * 1024,
        maxFileSize = 1024 * 1024 * 5,
        maxRequestSize = 1024 * 1024 * 10
)
public class BookServletController extends HttpServlet{
    
    private List<Book> bookList = new ArrayList();
     ObjectMapper mapper = new ObjectMapper();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
        String route = req.getParameter("action");
        System.out.println("Parametro "+route);
        switch(route){
            case "getAll":
                res.setContentType("application/json; charset=UTF-8");
                bookList = BookDAO.seleccionar();
                
                for(Book book : bookList){
                    byte[] imagenBytes = book.getImagen();
                    String imagenBase64 = Base64.getEncoder().encodeToString(imagenBytes);
                    book.setImagenBase64(imagenBase64);
                }
                
                mapper.writeValue(res.getWriter(), bookList);
                break;
            
            case "getDetails":
                String bookId = req.getParameter("id");
                
                Book bookDetails = BookDAO.seleccionarPorId(Integer.parseInt(bookId));
                
                res.setContentType("application/json");
                
                mapper.writeValue(res.getWriter(),bookDetails);
                break;
            
            case "getById":
                int id = Integer.parseInt(req.getParameter("id"));
                res.setContentType("application/json");
                Book bookDetail = BookDAO.seleccionarPorId(id);
                
                byte[] imagenBytes = bookDetail.getImagen();
                String imagenBase64 = Base64.getEncoder().encodeToString(imagenBytes);
                bookDetail.setImagenBase64(imagenBase64);
                
                mapper.writeValue(res.getWriter(), bookDetail);
                break;
            
            default:
                System.out.println("Parametro no valido.");
                break;
                        
        }
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
        String route = req.getParameter("action");
        
        switch(route){
            case "add":{
                String nombre = req.getParameter("nombre");
                String autor = req.getParameter("autor");
                int cantidadPaginas = Integer.parseInt(req.getParameter("cantidadPaginas"));
                int copias = Integer.parseInt(req.getParameter("copias"));
                String sinopsis = req.getParameter("sinopsis");
                double precio = Double.parseDouble(req.getParameter("precio"));
                
                Part filePart = req.getPart("imagen");
                byte [] imagenBytes = IOUtils.toByteArray(filePart.getInputStream());
                
                Book newBook = new Book(nombre, autor, cantidadPaginas, precio, sinopsis, copias, imagenBytes);
                
                BookDAO.insertar(newBook);
                
                res.setContentType("application/json");
                Map <String, String> response = new HashMap();
                response.put("message", "Libro guardado exitosamente!!!");
                mapper.writeValue(res.getWriter(), response);
            }
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res){
    }
    
}
