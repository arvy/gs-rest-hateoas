package hello;

import com.codahale.metrics.annotation.Gauge;
import com.codahale.metrics.annotation.Metered;
import com.codahale.metrics.annotation.Timed;
import com.ryantenney.metrics.annotation.Counted;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Created by Arvydas on 3/16/15.
 */
@Controller
public class GreetingController {

    public static final String TEMPLATE = "Hello, %s";

//    @Metered
//    @Gauge
    @Timed
//    @Counted(name = "greetingCount")

    @RequestMapping("/greeting")
    public HttpEntity<Greeting> greeting(
            @RequestParam(value="name", required = false, defaultValue = "World") String name
    ){
        Greeting greeting = new Greeting(String.format(TEMPLATE, name));
        greeting.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(GreetingController.class).greeting(name)).withSelfRel());
        return new ResponseEntity<Greeting>(greeting, HttpStatus.OK);
    }

}
