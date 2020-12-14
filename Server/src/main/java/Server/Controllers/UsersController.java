package Server.Controllers;

import Server.Domain.*;
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
    //TODO Maybe move repositories to singleton/static class?
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PlacesRepository placesRepository;

    @Autowired
    private UserPlaceVotesRepository userPlaceVotesRepository;

    //TODO Change return type from String to whatever type there should be. (All controllers)

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
    public @ResponseBody JSONObject getUsers(@PathVariable String id){
        var dbResponse = this.usersRepository.findById(Long.parseLong(id));
        if(dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponse.get();
        var response = new JSONObject();
        response.put(user.getId().toString(), this.buildJsonUser(user));
        return  response;
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
            user.setBirthDate(new SimpleDateFormat("dd.MM.yyyy").parse(birthdate));
        } catch (ParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Date parse error");
        }
        user.setPassword(this.hashPassword(password));
        this.usersRepository.save(user);
        return "ok";
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody HttpStatus deleteUser(@PathVariable String id) {
        var dbResponse = this.usersRepository.findById(Long.parseLong(id));
        if(dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponse.get();
        this.usersRepository.delete(user);
        return HttpStatus.OK;
    }

    @GetMapping(path = "/{user_id}/placeVotes/{place_id}")
    public @ResponseBody Boolean didUserVoteForPlace(@PathVariable String user_id, @PathVariable String place_id) {
        var dbResponseUser = this.usersRepository.findById(Long.parseLong(user_id));
        if(dbResponseUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponseUser.get();
        var dbResponsePlace = this.placesRepository.findById(Long.parseLong(place_id));
        if(dbResponsePlace.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        var place = dbResponsePlace.get();
        return  user.getUserPlaceVotes().contains(place);
    }

    @PostMapping(path = "/{user_id}/placeVotes/{place_id}")
    public @ResponseBody HttpStatus userVoteForPlace(@PathVariable String user_id, @PathVariable String place_id, @RequestParam int vote){
        var dbResponseUser = this.usersRepository.findById(Long.parseLong(user_id));
        if(dbResponseUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponseUser.get();
        var dbResponsePlace = this.placesRepository.findById(Long.parseLong(place_id));
        if(dbResponsePlace.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        var place = dbResponsePlace.get();
        if(user.getUserPlaceVotes().contains(place)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already voted for that place");
        }
        var placeUserVotedFor = new UserPlaceVote();
        place.setAccumulatedScore(place.getAccumulatedScore() + vote);
        place.setUsersVoted(place.getUsersVoted() + 1);
        placeUserVotedFor.setPlace(place);
        placeUserVotedFor.setUser(user);
        this.userPlaceVotesRepository.save(placeUserVotedFor);
        return HttpStatus.OK;
    }
    //TODO user voted for something : boolean, database table: UserRouteVotes
    //Can not undo vote


}
