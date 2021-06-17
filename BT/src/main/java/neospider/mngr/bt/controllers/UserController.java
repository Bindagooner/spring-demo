package neospider.mngr.bt.controllers;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import neospider.mngr.bt.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController extends BaseController {

    private UserService userService;

    private Gson gson;

    @Autowired
    UserController(Gson gson, UserService userService) {
        super(gson);
        this.gson = gson;
        this.userService = userService;
    }

    @GetMapping("/get-user-by-username")
    public ResponseEntity<?> getUserByUsername(@RequestParam Map<String, String> request) {
        String username = request.get("username");
        if (username == null) {
            return new ResponseEntity(null, HttpStatus.BAD_REQUEST);
        }
        log.info("get user by username request received, username={}", username);
        Map user = userService.getUserByUserName(username);
        if (null == user) {
            return new ResponseEntity("User not found", HttpStatus.NOT_FOUND);
        }
        return buildMapSuccessResponse(user);
    }
}
