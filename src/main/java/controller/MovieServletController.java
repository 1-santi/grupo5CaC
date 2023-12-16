package controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import data.MovieDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.*;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.*;
import model.Movie;
import org.apache.commons.io.IOUtils;

@WebServlet("/movies")
@MultipartConfig(
        location = "/media/temp",
        fileSizeThreshold = 1024 * 1024, //Tamaño umbral 1MB
        maxFileSize = 1024 * 1024 * 5, //Tamaño maximo de archivo en bytes 5MB
        maxRequestSize = 1024 * 1024 * 10 // Tamaño maximo de request en bytes 10MB
)
public class MovieServletController extends HttpServlet{
    
    List<Movie> movieList = new ArrayList();
    ObjectMapper mapper = new ObjectMapper();
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException{
        req.setCharacterEncoding("UTF-8");
        String route = req.getParameter("action");
        System.out.println("parametro: "+ route);
        switch (route){
            case "getAll"->{
                res.setContentType("application/json; charset=UTF-8");
                movieList = MovieDAO.seleccionar();
                
                for(Movie movie : movieList){
                    byte[] imagenBytes = movie.getImagen();
                    String imagenBase64 = Base64.getEncoder().encodeToString(imagenBytes);
                    movie.setImagenBase64(imagenBase64);
                }
                
                mapper.writeValue(res.getWriter(), movieList);
            }
            
            case "getDetails"->{
                String movieId = req.getParameter("id");
                
                Movie movieDetails = MovieDAO.seleccionarPorId(Integer.parseInt(movieId));
                
                res.setContentType("application/json");
                mapper.writeValue(res.getWriter(), movieDetails);
            }
            
            case "getById"->{
                
                int id = Integer.parseInt(req.getParameter("id"));
                
                res.setContentType("application/json");
                Movie movieDetails = MovieDAO.seleccionarPorId(id);
                
                byte[] imagenBytes = movieDetails.getImagen();
                String imagenBase64 = Base64.getEncoder().encodeToString(imagenBytes);
                movieDetails.setImagenBase64(imagenBase64);
                
                mapper.writeValue(res.getWriter(), movieDetails);
            }
            
            default->{
                    System.out.println("parametro no valido.");
            }
            
            
                
        }   
    }
    
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException{
        String route = req.getParameter("action");
        
        switch(route){
            case "add" ->{
                String nombre = req.getParameter("nombre");
                String autor = req.getParameter("autor");
                int duracion = Integer.parseInt(req.getParameter("duracion"));
                int copias = Integer.parseInt(req.getParameter("copias"));
                String sinopsis = req.getParameter("sinopsis");
                double precio = Double.parseDouble(req.getParameter("precio"));
                
                Part filePart = req.getPart("imagen");
                byte [] imagenBytes = IOUtils.toByteArray(filePart.getInputStream());
                
                Movie newMovie = new Movie(nombre,autor,duracion,precio,sinopsis, copias, imagenBytes);
                
                MovieDAO.insertar(newMovie);
                
                res.setContentType("application/json");
                Map <String, String> response = new HashMap();
                response.put("message", "Pelicula guardada de forma correcta");
                mapper.writeValue(res.getWriter(), response);
            }
            
            case "update"->{
                
                try{
                    int id= Integer.parseInt(req.getParameter("id"));
                    String nombre = req.getParameter("nombre");
                    String autor = req.getParameter("autor");
                    int duracion = Integer.parseInt(req.getParameter("duracion"));
                    int copias = Integer.parseInt(req.getParameter("copias"));
                    String sinopsis = req.getParameter("sinopsis");
                    double precio = Double.parseDouble(req.getParameter("precio"));

                    Part filePart = req.getPart("imagen");
                    byte[] imageBytes = IOUtils.toByteArray(filePart.getInputStream());

                    Movie movie = new Movie(id,nombre, autor, duracion, precio, sinopsis, copias, imageBytes);

                    MovieDAO.actualizar(movie);

                    res.setContentType("application/json");
                    res.setCharacterEncoding("UTF-8");

                    Map <String, String> response = new HashMap<>();
                    response.put("success", "true");
                    mapper.writeValue(res.getWriter(), response);
                }catch(Exception e){
                    res.setContentType("application/json");
                    res.setCharacterEncoding("UTF-8");
                    
                    Map <String, String> responseFail = new HashMap<>();
                    responseFail.put("success", "false");
                    responseFail.put("message", e.getMessage());
                    mapper.writeValue(res.getWriter(), responseFail);
                }
                
            }
        }
    }
    
    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse res) throws IOException{
        String route = req.getParameter("action");
        System.out.println("route = " + route);
        
        switch(route){
            case "delete"->{
                try{
                    int id = Integer.parseInt(req.getParameter("id"));
                    System.out.println("id:" + id);
                    
                    int result = MovieDAO.eliminar(id);
                    res.setContentType("application/json");
                    res.setCharacterEncoding("UTF-8");
                    res.getWriter().write("{\"success\": true}");
                }catch(Exception e){
                    res.setContentType("application/json");
                    res.setCharacterEncoding("UTF-8");
                    res.getWriter().write("{\"success\": false, \"message\": \"Error al borrar el registro.\"}");
                }
            }
            
            default->{
                System.out.println("error en parametro.");
            }
        }
    }
}
