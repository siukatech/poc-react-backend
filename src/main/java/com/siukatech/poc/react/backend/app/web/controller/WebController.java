package com.siukatech.poc.react.backend.app.web.controller;

import com.siukatech.poc.react.backend.parent.web.annotation.v1.ProtectedApiV1Controller;
import com.siukatech.poc.react.backend.parent.web.annotation.v1.PublicApiV1Controller;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
//@PublicApiV1Controller
@RestController
public class WebController {

    @GetMapping(path = PublicApiV1Controller.REQUEST_MAPPING_URI_PREFIX)
    public String index() {
        return "external (react-backend-app)";
    }

    @RequestMapping(path = ProtectedApiV1Controller.REQUEST_MAPPING_URI_PREFIX + "/authorized", method = {RequestMethod.GET, RequestMethod.POST})
    public String authorized(Principal principal, Model model) {
        //addCustomers();
        //model.addAttribute("customers", customerDAO.findAll());
        model.addAttribute("username", principal.getName());
        return principal.getName() + " (react-backend-app)";
    }

}
