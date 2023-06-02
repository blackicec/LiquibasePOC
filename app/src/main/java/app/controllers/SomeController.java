package app.controllers;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("some")
public class SomeController {
    @GetMapping
    public ResponseEntity<List<String>> getAll() {
        return new ResponseEntity<>(List.of("String1", "String2"), HttpStatus.OK);
    }
}
