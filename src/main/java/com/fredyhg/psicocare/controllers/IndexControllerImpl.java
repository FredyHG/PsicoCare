package com.fredyhg.psicocare.controllers;

import com.fredyhg.psicocare.controllers.interfaces.IndexController;
import com.fredyhg.psicocare.exceptions.utils.ResponseMessage;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class IndexControllerImpl implements IndexController {

    @GetMapping("/")
    @Override
    public void redirectToDocumentation(HttpServletResponse httpServletResponse) {
        httpServletResponse.setHeader("Location", "/swagger-ui/index.html");
        httpServletResponse.setStatus(HttpStatus.MOVED_PERMANENTLY.value());
    }

    @GetMapping("/api/status")
    public ResponseEntity<ResponseMessage> checkStatus(){
        return ResponseEntity.status(HttpStatus.OK).body(ResponseMessage
                .builder()
                .statusCode(200)
                .timestamp(new Date())
                .message("Success")
                .description("Up")
                .build()
        );
    }
}
