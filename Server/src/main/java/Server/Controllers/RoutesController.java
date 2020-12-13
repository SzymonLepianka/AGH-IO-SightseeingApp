package Server.Controllers;

import Server.Domain.RoutesRepository;
import Server.Domain.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/routes")
public class RoutesController {
    @Autowired
    private RoutesRepository routesRepository;

    @GetMapping(path="/{id}")
    public @ResponseBody String getUsers(@PathVariable String id){

        return id;
    }

    //TODO /routes/{id}/pointsOfRoute get, post, delete

    //TODO /routes/{id}/comments get, post, delete
}
