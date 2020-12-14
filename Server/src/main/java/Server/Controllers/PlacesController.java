package Server.Controllers;

import Server.Domain.*;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.xml.stream.events.Comment;

@Controller
@RequestMapping(path="/places")
public class PlacesController {
    @Autowired
    private PlacesRepository placesRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private PlaceCommentsRepository placeCommentsRepository;

    //TODO Move it to model, better fit with documentation
    private JSONObject buildJsonPlace(Place place) {
        var placeJson = new JSONObject();
        placeJson
                .put("name", place.getName())
                .put("description", place.getDescription())
                .put("latitude", place.getLatitude())
                .put("longitude", place.getLongitude())
                .put("accumulatedScore",place.getAccumulatedScore())
                .put("usersVoted", place.getUsersVoted())
                .put("valid", place.getValidPlace());
        return placeJson;
    }

    //TODO Move it to model, better fit with documentation
    private JSONObject buildJsonComment(PlaceComment comment) {
        var commentJson = new JSONObject();
        commentJson
                .put("username", comment.getUser().getUsername())
                .put("content", comment.getContent());
        return commentJson;
    }

    @GetMapping(path = "")
    public @ResponseBody String getAllPlaces() {
        var places = placesRepository.findAll();
        var response = new JSONObject();
        for (var place : places) {
            response.put(place.getId().toString(), this.buildJsonPlace(place));
        }
        return response.toString();
    }

    @GetMapping(path="/{id}")
    public @ResponseBody String getPlace(@PathVariable String id){
        var dbResponse = placesRepository.findById(Long.parseLong(id));
        if(dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        var place = dbResponse.get();
        var response = new JSONObject();
        response.put(place.getId().toString(), this.buildJsonPlace(place));
        return response.toString();
    }

    //TODO What about Google Maps ID?
    @PostMapping(path = "")
    public @ResponseBody String addPlace(@RequestParam String name, @RequestParam String description, @RequestParam String latitude, @RequestParam String longitude) {
        var place = new Place();
        place.setName(name);
        place.setDescription(description);
        place.setLatitude(Float.parseFloat(latitude));
        place.setLongitude(Float.parseFloat(longitude));
        this.placesRepository.save(place);
        return "ok";
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody String deletePlace(@PathVariable String id) {
        var dbResponse = placesRepository.findById(Long.parseLong(id));
        if(dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        var place = dbResponse.get();
        placesRepository.delete(place);
        return "ok";
    }

    @GetMapping(path = "/{id}/comments")
    public @ResponseBody String getPlaceComments(@PathVariable String id) {
        var dbResponse = placesRepository.findById(Long.parseLong(id));
        if(dbResponse.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        var place = dbResponse.get();
        var response = new JSONObject();
        for(var comment : place.getPlaceComments()) {
            response.put(comment.getId().toString(), this.buildJsonComment(comment));
        }
        return response.toString();
    }

    @PostMapping(path = "/{placeId}/comments")
    public @ResponseBody String addPlaceComment(@PathVariable String placeId, @RequestParam String userId, @RequestParam String content) {
        var comment = new PlaceComment();
        var dbResponseUser = this.usersRepository.findById(Long.parseLong(userId));
        if(dbResponseUser.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        comment.setUser(dbResponseUser.get());
        var dbResponsePlace = this.placesRepository.findById(Long.parseLong(placeId));
        if(dbResponsePlace.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        comment.setPlace(dbResponsePlace.get());
        comment.setContent(content);
        placeCommentsRepository.save(comment);
        return "ok";
    }

    @DeleteMapping(path = "/{placeId}/comments/{commentId}")
    public @ResponseBody String deletePlaceComment(@PathVariable String commentId) {
        var dbResponse = this.placeCommentsRepository.findById(Long.parseLong(commentId));
        if(dbResponse.isEmpty()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        }
        var comment = dbResponse.get();
        this.placeCommentsRepository.delete(comment);
        return "ok";
    }
}
