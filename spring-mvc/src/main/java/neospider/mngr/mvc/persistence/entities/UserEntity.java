package neospider.mngr.mvc.persistence.entities;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserEntity {

    private String id;
    private String username;
    private String password;
    private String role;
}
