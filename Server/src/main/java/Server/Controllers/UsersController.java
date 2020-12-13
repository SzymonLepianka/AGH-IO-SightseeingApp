package Server.Controllers;

import Server.Domain.User;
import Server.Domain.UsersRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Controller
@RequestMapping(path="/users")
public class UsersController {
    @Autowired
    private UsersRepository usersRepository;

    private JSONObject buildJsonUser(User user) {
        var userJson = new JSONObject();
        userJson
                .put("username",user.getUsername())
                .put("firstname", user.getFirstName())
                .put("surname", user.getSurname())
                .put("email", user.getEmail())
                .put("birthdate", user.getBirthDate());
        return userJson;
    }

    @GetMapping(path="/{id}")
    public @ResponseBody String getUsers(@PathVariable String id){
        var dbResponse = this.usersRepository.findById(Long.parseLong(id));
        if(dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponse.get();
        var response = new JSONObject();
        response.put(user.getId().toString(), this.buildJsonUser(user));
        return  response.toString();
    }

    //TODO Move it to model?
    private String hashPassword(String password) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Password hasing problem");
        }
    }

    @PostMapping(path="")
    public @ResponseBody String addUser(@RequestParam String username, @RequestParam String firstname, @RequestParam String surname, @RequestParam String birthdate, @RequestParam String password) {
        var user = new User();
        user.setUsername(username);
        user.setFirstName(firstname);
        user.setSurname(surname);
        try {
            user.setBirthDate(new SimpleDateFormat("05.02.1999").parse(birthdate));
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date parse error");
        }
        user.setPassword(this.hashPassword(password));
        this.usersRepository.save(user);
        return "ok";
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody String deleteUser(@PathVariable String id) {
        var dbResponse = this.usersRepository.findById(Long.parseLong(id));
        if(dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponse.get();
        this.usersRepository.delete(user);
        return "ok";
    }

    //TODO user voted for something : boolean, database table: UserPlaceVotes
    //TODO user voted for something : boolean, database table: UserRouteVotes
    //Can not undo vote


}
