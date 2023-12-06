
package data;

import static data.Conexion.close;
import static data.Conexion.getConexion;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Book;

public class BookDAO {
    
    private static final String SQL_SELECT = "SELECT * FROM libros";
    
    private static final String SQL_SELECT_BY_ID = "SELECT * FROM libros WHERE idlibros = ?";
   
    private static final String SQL_INSERT = "INSERT INTO libros(nombre, autor, cantidadPaginas, precio, imagen, copias, sinopsis) VALUES(?, ?, ?, ?, ?, ?, ?)";    
    
    private static final String SQL_UPDATE = "UPDATE libros SET nombre = ?, autor = ?, cantidadPaginas = ?, precio = ?, copias = ? WHERE idlibros = ?";
    
    private static final String SQL_DELETE = "DELETE FROM libros WHERE idlibros = ?";
    
    public static List<Book> seleccionar() {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Book libro = null;
        List<Book> libros = new ArrayList();

        try {
            conn = getConexion();
            stmt = conn.prepareStatement(SQL_SELECT);
            rs = stmt.executeQuery();
            while (rs.next()) {
                int idlibro = rs.getInt(1);
                String nombre = rs.getString("nombre");
                String autor = rs.getString("autor");
                int cantidadPaginas = rs.getInt("cantidadPaginas");
                double precio = rs.getDouble("precio");
                String sinopsis = rs.getString("sinopsis");
                int copias = rs.getInt("copias");
                            
                Blob blob = rs.getBlob("imagen");
                byte[] imagenBytes = blob.getBytes(1,(int)blob.length());

                libro = new Book(idlibro, nombre, autor, cantidadPaginas, precio, sinopsis, copias, imagenBytes);

                libros.add(libro);
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.out);
        }finally {
            try {
                close(rs);
                close(stmt);
                close(conn);
            } catch (SQLException ex) {
                ex.printStackTrace(System.out);
            }
        }

        return libros;
    }
    
    public static int insertar(Book libro){
        Connection conn = null;
        PreparedStatement stmt = null;
        int registros = 0;
        try {
            conn = getConexion();
            stmt = conn.prepareStatement(SQL_INSERT);
            stmt.setString(1, libro.getNombre());
            stmt.setString(2, libro.getAutor());
            stmt.setInt(3, libro.getCantidadPaginas());
            stmt.setDouble(4, libro.getPrecio());
            stmt.setInt(5, libro.getCopias());
            
            Blob imagenBlob = conn.createBlob();
            imagenBlob.setBytes(1, libro.getImagen());
            stmt.setBlob(6, imagenBlob);
            
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
    
    public static Book seleccionarPorId(int id) {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Book libro = null;

        try {
            conn = getConexion();
            stmt = conn.prepareStatement(SQL_SELECT_BY_ID);
            stmt.setInt(1, id);
            rs = stmt.executeQuery();
            
            while (rs.next()) {
                int idlibros = rs.getInt("idlibros");
                String nombre = rs.getString("nombre");
                String autor = rs.getString("genero");
                int cantidadPaginas = rs.getInt("cantidadPaginas");
                String sinopsis = rs.getString("sinopsis");
                double precio = rs.getDouble("precio");
                int copias = rs.getInt("copias");
                
                Blob blob = rs.getBlob("imagen");
                byte[] imagenBytes = blob.getBytes(1,(int)blob.length());

                libro = new Book (idlibros, nombre, autor, cantidadPaginas, precio, sinopsis, copias, imagenBytes);

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

        return libro;
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
}
