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
    private RoutesRepository routesRepository;

    @Autowired
    private UserPlaceVotesRepository userPlaceVotesRepository;

    @Autowired
    private UserRouteVotesRepository userRouteVotesRepository;

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
    public @ResponseBody String getUser(@PathVariable String id){
        var dbResponse = this.usersRepository.findById(Long.parseLong(id));
        if(dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponse.get();
        var response = new JSONObject();
        response.put(user.getId().toString(), this.buildJsonUser(user));
        return response.toString();
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
    public @ResponseBody String deleteUser(@PathVariable String id) {
        var dbResponse = this.usersRepository.findById(Long.parseLong(id));
        if(dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponse.get();
        this.usersRepository.delete(user);
        return "ok";
    }

    @GetMapping(path = "/{user_id}/placeVotes/{place_id}")
    public @ResponseBody String didUserVoteForPlace(@PathVariable String user_id, @PathVariable String place_id) {
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
        return  String.valueOf(user.getUserPlaceVotes().contains(place));
    }

    @PostMapping(path = "/{user_id}/placeVotes/{place_id}")
    public @ResponseBody String userVoteForPlace(@PathVariable String user_id, @PathVariable String place_id, @RequestParam String vote){
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
        place.setAccumulatedScore(place.getAccumulatedScore() + Integer.parseInt(vote));
        place.setUsersVoted(place.getUsersVoted() + 1);
        placeUserVotedFor.setPlace(place);
        placeUserVotedFor.setUser(user);
        this.placesRepository.save(place);
        this.userPlaceVotesRepository.save(placeUserVotedFor);
        return "ok";
    }

    //TODO user voted for something : boolean, database table: UserRouteVotes
    @GetMapping(path = "/{user_id}/routeVotes/{route_id}")
    public @ResponseBody String didUserVoteForRoute(@PathVariable String user_id, @PathVariable String route_id) {
        var dbResponseUser = this.usersRepository.findById(Long.parseLong(user_id));
        if(dbResponseUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponseUser.get();
        var dbResponseRoute = this.placesRepository.findById(Long.parseLong(route_id));
        if(dbResponseRoute.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
        }
        var route = dbResponseRoute.get();
        return  String.valueOf(user.getUserRouteVotes().contains(route));
    }

    @PostMapping(path = "/{user_id}/routeVotes/{route_id}")
    public @ResponseBody String userVoteForRoute(@PathVariable String user_id, @PathVariable String route_id, @RequestParam String vote){
        var dbResponseUser = this.usersRepository.findById(Long.parseLong(user_id));
        if(dbResponseUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponseUser.get();
        var dbResponseRoute = this.routesRepository.findById(Long.parseLong(route_id));
        if(dbResponseRoute.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
        }
        var route = dbResponseRoute.get();
        if(user.getUserRouteVotes().contains(route)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already voted for that route");
        }
        var routeUserVotedFor = new UserRouteVote();
        route.setAccumulatedScore(route.getAccumulatedScore() + Integer.parseInt(vote));
        route.setUsersVoted(route.getUsersVoted() + 1);
        routeUserVotedFor.setRoute(route);
        routeUserVotedFor.setUser(user);
        this.routesRepository.save(route);
        this.userRouteVotesRepository.save(routeUserVotedFor);
        return "ok";
    }


}
