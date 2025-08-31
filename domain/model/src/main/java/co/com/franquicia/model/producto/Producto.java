package co.com.franquicia.model.producto;

import co.com.franquicia.model.sucursal.Sucursal;
import lombok.Data;

@Data
public class Producto {

    private Long id;
    private String nombre;
    private int stock;
    private Sucursal sucursal;

    public boolean tieneStock() {
        return stock > 0;
    }
}
