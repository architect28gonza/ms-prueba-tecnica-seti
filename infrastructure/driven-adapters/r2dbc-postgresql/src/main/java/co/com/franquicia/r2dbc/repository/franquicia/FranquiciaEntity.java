package co.com.franquicia.r2dbc.repository.franquicia;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("tbl_franquicia")
public class FranquiciaEntity {

    @Id
    private Long id;
    private String nombre;
}
