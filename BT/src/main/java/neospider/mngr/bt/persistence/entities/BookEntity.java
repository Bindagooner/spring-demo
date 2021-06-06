package neospider.mngr.bt.persistence.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BookEntity {

    private String id;
    private String name;
    private String author;
}
