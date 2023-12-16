package model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Movie {
    private int idmovies;
    private String nombre;
    private String autor;
    private int duracion;
    private double precio;
    private String sinopsis;
    private int copias;
    private byte[] imagen;
    private String imagenBase64;

    public Movie(String nombre, String autor, int duracion, double precio, String sinopsis, int copias, byte[] imagen) {
        this.nombre = nombre;
        this.autor = autor;
        this.duracion = duracion;
        this.precio = precio;
        this.sinopsis = sinopsis;
        this.copias = copias;
        this.imagen = imagen;
    }

    public Movie(int idmovies, String nombre, String autor, int duracion, double precio, String sinopsis, int copias, byte[] imagen) {
        this.idmovies = idmovies;
        this.nombre = nombre;
        this.autor = autor;
        this.duracion = duracion;
        this.precio = precio;
        this.sinopsis = sinopsis;
        this.copias = copias;
        this.imagen = imagen;
    }
    
    
}
