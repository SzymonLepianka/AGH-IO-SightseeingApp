package Server.Controllers;

import Server.Domain.*;
import Server.Model.Authorization;
import Server.Model.GetScopes;
import org.json.JSONObject;
import org.riversun.okhttp3.OkHttp3CookieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigInteger;
import java.net.*;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Controller
@RequestMapping(path = "/users")
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



    private JSONObject buildJsonUser(User user) {
        var userJson = new JSONObject();
        userJson
                .put("user_id", user.getId())
                .put("username", user.getUsername())
                .put("firstname", user.getFirstName())
                .put("surname", user.getSurname())
                .put("email", user.getEmail());
        return userJson;
    }

    @GetMapping(path = "/{username}")
    public @ResponseBody
    String getUser(@PathVariable String username, HttpServletResponse httpServletResponse) throws SQLException, IOException, InterruptedException, ParseException {
        Authorization authorization = new Authorization();

        String accessToken = authorization.Authorize(username, httpServletResponse);
        List<User> allUsersFromDataBase = (List<User>) this.usersRepository.findAll();
        User userFound = allUsersFromDataBase.stream()
                .filter(x -> username.equals(x.getUsername()))
                .findFirst()
                .orElse(null);

        List<String> scopesFromAccessToken = GetScopes.getScopes(accessToken);
        User user;
        if (userFound != null) {
            var dbResponse = this.usersRepository.findById(userFound.getId());
            if (dbResponse.isEmpty()) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found in data base");
            }
            System.out.println("User istnieje już w bazie");
            user = dbResponse.get();
        } else {
            System.out.println("User nie istnieje już w bazie, wykonywany jest request");
            String url = "http://localhost:8081/api/getUserData?clientID=2&accessToken=" + accessToken;
            OkHttp3CookieHelper cookieHelper = new OkHttp3CookieHelper();
            cookieHelper.setCookie(url, "AccessToken", accessToken);
            HttpClient client = HttpClient.newBuilder().cookieHandler(new CookieManager()).build();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            CookieStore cookieStore = ((CookieManager) (client.cookieHandler().get())).getCookieStore();
            cookieStore.add(URI.create(url), new HttpCookie("AccessToken", accessToken));
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            JSONObject jsonObject1 = new JSONObject(response.body());

            String user_email = null;
            String user_firstname = null;
            String user_username = null;
            String user_surname = null;
            Date user_birthdate = null;
            if (scopesFromAccessToken.contains("user_email")) {
                user_email = (String) jsonObject1.get("user_email");
            }
            if (scopesFromAccessToken.contains("user_firstname")) {
                user_firstname = (String) jsonObject1.get("user_firstname");
            }
            if (scopesFromAccessToken.contains("user_username")) {
                user_username = (String) jsonObject1.get("user_username");
            }
            if (scopesFromAccessToken.contains("user_surname")) {
                user_surname = (String) jsonObject1.get("user_surname");
            }
            if (scopesFromAccessToken.contains("user_birthdate")) {
                user_birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(jsonObject1.get("user_birthdate").toString());
            }
            user = new User();
            user.setEmail(user_email);
            user.setFirstName(user_firstname);
            user.setUsername(user_username);
            user.setSurname(user_surname);
            user.setBirthDate(user_birthdate);
            usersRepository.save(user);
        }
        var response = new JSONObject();
        response.put("user_id", user.getId());
        response.put("email", user.getEmail());
        response.put("first_name", user.getFirstName());
        response.put("surname", user.getSurname());
        response.put("username", user.getUsername());
//        response.put(user.getId().toString(), this.buildJsonUser(user).put("email", user.getEmail()));
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
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Password hashing problem");
        }
    }

    @PostMapping(path = "")
    public @ResponseBody
    String addUser(@RequestParam String username, @RequestParam String firstname, @RequestParam String surname, @RequestParam String birthdate, @RequestParam String password) {
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
    public @ResponseBody
    String deleteUser(@PathVariable String id) {
        var dbResponse = this.usersRepository.findById(Long.parseLong(id));
        if (dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponse.get();
        this.usersRepository.delete(user);
        return "ok";
    }

    @GetMapping(path = "/{user_id}/placeVotes/{place_id}")
    public @ResponseBody
    String didUserVoteForPlace(@PathVariable String user_id, @PathVariable String place_id) {
        var dbResponseUser = this.usersRepository.findById(Long.parseLong(user_id));
        if (dbResponseUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponseUser.get();
        var dbResponsePlace = this.placesRepository.findById(Long.parseLong(place_id));
        if (dbResponsePlace.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        var place = dbResponsePlace.get();
        var result = false;
        for (var votePlace : user.getUserPlaceVotes()) {
            if (votePlace.getPlace() == place) {
                result = true;
                break;
            }
        }
        return String.valueOf(result);
    }

    @PostMapping(path = "/{user_id}/placeVotes/{place_id}")
    public @ResponseBody
    String userVoteForPlace(@PathVariable String user_id, @PathVariable String place_id, @RequestParam String vote) {
        var dbResponseUser = this.usersRepository.findById(Long.parseLong(user_id));
        if (dbResponseUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponseUser.get();
        var dbResponsePlace = this.placesRepository.findById(Long.parseLong(place_id));
        if (dbResponsePlace.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        var place = dbResponsePlace.get();
        for (var votePlace : user.getUserPlaceVotes()) {
            if (votePlace.getPlace() == place) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already voted for that place");
            }
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
    public @ResponseBody
    String didUserVoteForRoute(@PathVariable String user_id, @PathVariable String route_id) {
        var dbResponseUser = this.usersRepository.findById(Long.parseLong(user_id));
        if (dbResponseUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponseUser.get();
        var dbResponseRoute = this.routesRepository.findById(Long.parseLong(route_id));
        if (dbResponseRoute.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
        }
        var route = dbResponseRoute.get();
        var result = false;
        for (var voteRoute : user.getUserRouteVotes()) {
            if (voteRoute.getRoute() == route) {
                result = true;
                break;
            }
        }
        return String.valueOf(result);
    }

    @PostMapping(path = "/{user_id}/routeVotes/{route_id}")
    public @ResponseBody
    String userVoteForRoute(@PathVariable String user_id, @PathVariable String route_id, @RequestParam String vote) {
        var dbResponseUser = this.usersRepository.findById(Long.parseLong(user_id));
        if (dbResponseUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        var user = dbResponseUser.get();
        var dbResponseRoute = this.routesRepository.findById(Long.parseLong(route_id));
        if (dbResponseRoute.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Route not found");
        }
        var route = dbResponseRoute.get();
        for (var voteRoute : user.getUserRouteVotes()) {
            if (voteRoute.getRoute() == route) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "User already voted for that route");
            }
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
