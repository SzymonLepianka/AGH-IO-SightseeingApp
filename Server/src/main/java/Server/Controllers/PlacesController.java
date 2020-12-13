package Server.Controllers;

import Server.Domain.Place;
import Server.Domain.PlacesRepository;
import Server.Domain.UsersRepository;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.config.ConfigDataResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Set;

@Controller
@RequestMapping(path="/places")
public class PlacesController {
    @Autowired
    private PlacesRepository placesRepositoryRepository;

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

    @GetMapping(path = "")
    public @ResponseBody String getAllPlaces() {
        var places = placesRepositoryRepository.findAll();
        var response = new JSONObject();
        for (var place : places) {
            response.put(place.getId().toString(), this.buildJsonPlace(place));
        }
        return response.toString();
    }

    @GetMapping(path="/{id}")
    public @ResponseBody String getPlace(@PathVariable String id){
        var dbResponse = placesRepositoryRepository.findById(Long.parseLong(id));
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
        this.placesRepositoryRepository.save(place);
        return "ok";
    }

    @DeleteMapping(path = "/{id}")
    public @ResponseBody String deletePlace(@PathVariable String id) {
        var dbResponse = placesRepositoryRepository.findById(Long.parseLong(id));
        if(dbResponse.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Place not found");
        }
        var place = dbResponse.get();
        placesRepositoryRepository.delete(place);
        return "ok";
    }

    //TODO Add /{id}/comments get, post, delete
}
