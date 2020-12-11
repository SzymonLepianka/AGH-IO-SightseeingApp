package Server.Controllers;

import Server.Domain.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/Places")
public class PlacesController {
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping(path="/{id}")
    public @ResponseBody String getPlace(@PathVariable String id){
        return id;
    }
}
