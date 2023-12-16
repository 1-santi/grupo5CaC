package data;

import static data.Conexion.*;
import java.sql.*;
import java.util.*;
import model.Movie;

public class MovieDAO {
    
    private static final String SQL_SELECT = "SELECT * FROM movies";
    
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM movies WHERE idmovies = ?";
   
    private static final String SQL_INSERT = "INSERT INTO movies(nombre, autor, duracion, precio, copias, imagen, sinopsis) VALUES(?, ?, ?, ?, ?, ?, ?)";
    
    private static final String SQL_UPDATE = "UPDATE movies SET nombre = ?, autor = ?, duracion = ?, precio= ?, copias= ?, sinopsis= ? WHERE idmovies = ?";

    private static final String SQL_DELETE = "DELETE FROM movies WHERE idmovies = ?";
    
    public static List<Movie> seleccionar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Movie movie = null;
        List<Movie> movies = new ArrayList();

        try {
            conn = getConexion();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int idmovies = rs.getInt(1);
                String nombre = rs.getString("nombre");
                String autor = rs.getString("autor");
                int duracion = rs.getInt("duracion");
                double precio = rs.getDouble("precio");
                String sinopsis = rs.getString("sinopsis");
                int copias = rs.getInt("copias");
                
                Blob blob = rs.getBlob("imagen");
                byte[] imagenBytes = blob.getBytes(1, (int)blob.length());

                movie = new Movie(idmovies, nombre, autor, duracion, precio, sinopsis, copias, imagenBytes);

                movies.add(movie);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                close(rs);
                close(stmt);
                close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }

        return movies;
    }
    
    public static int insertar(Movie movie){
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;
        try {
            conn = getConexion();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, movie.getNombre());
            stmt.setString(2, movie.getAutor());
            stmt.setInt(3, movie.getDuracion());
            stmt.setDouble(4, movie.getPrecio());
            stmt.setInt(5, movie.getCopias());
            
            Blob imagenBlob = conn.createBlob();
            imagenBlob.setBytes(1, movie.getImagen());
            stmt.setBlob(6, imagenBlob);
            
            stmt.setString(7,movie.getSinopsis());
            
            registros = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally{
            try {
                close(stmt);
                close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return registros;
    }
   
    public static Movie seleccionarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Movie movie = null;

        try {
            conn = getConexion();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                int idmovies = rs.getInt("idmovies");
                String nombre = rs.getString("nombre");
                String autor = rs.getString("autor");
                int duracion = rs.getInt("duracion");
                String sinopsis = rs.getString("sinopsis");
                double precio = rs.getDouble("precio");
                int copias = rs.getInt("copias");
                
                Blob blob = rs.getBlob("imagen");
                byte[] imagenBytes = blob.getBytes(1, (int)blob.length());

                movie = new Movie(idmovies, nombre, autor,duracion,precio, sinopsis,copias,imagenBytes);

                
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        } finally {
            try {
                close(rs);
                close(stmt);
                close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }

        return movie;
    }
    
    public static int eliminar(int id){
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;
        try {
            conn = getConexion();
            
            stmt = conn.prepareStatement(SQL_DELETE);
            stmt.setInt(1, id);
            
            registros = stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally{
            try {
                close(stmt);
                close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return registros;
    }
    
    public static int actualizar(Movie movie){
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;
        try {
            conn = getConexion();
            stmt = conn.prepareStatement(SQL_UPDATE);
            stmt.setString(1, movie.getNombre());
            stmt.setString(2, movie.getAutor());
            stmt.setInt(3, movie.getDuracion());
            stmt.setDouble(4, movie.getPrecio());
            stmt.setInt(5, movie.getCopias());
            stmt.setString(6,movie.getSinopsis());
            stmt.setInt(7, movie.getIdmovies());
            
            registros = stmt.executeUpdate();
            
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }
        finally{
            try {
                close(stmt);
                close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }
        return registros;
    }
}
