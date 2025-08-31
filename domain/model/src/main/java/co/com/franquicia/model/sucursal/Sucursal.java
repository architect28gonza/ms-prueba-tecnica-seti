package co.com.franquicia.model.sucursal;

import co.com.franquicia.model.franquicia.Franquicia;
import lombok.Data;

@Data
public class Sucursal {

    private Long id;
    private String nombre;
    private Franquicia franquicia;
}
