package com.example.backend.controllers;

import com.example.backend.models.Country;
import com.example.backend.models.Painting;
import com.example.backend.repositories.CountryRepository;
import com.example.backend.repositories.PaintingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.Column;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class PaintingController {
    @Autowired
    PaintingRepository paintingRepository;
    @GetMapping("/paintings")
    public List
    getAllCPaintings() {
        return paintingRepository.findAll();
    }
    @Column(name = "name", nullable = false, unique = true)
    public String name;
    @PostMapping("/paintings")
    public ResponseEntity<Object> createPainting(@RequestBody Painting painting)
            throws Exception {
        try {
            Painting nc = paintingRepository.save(painting);
            return new ResponseEntity<Object>(nc, HttpStatus.OK);
        } catch (Exception ex) {
            String error;
            if (ex.getMessage().contains("paintingss.name_UNIQUE"))
                error = "paintingalreadyexists";
            else
                error = "undefinederror";
            Map<String, String>
                    map = new HashMap<>();
            map.put("error", error);
            return new ResponseEntity<Object>(map, HttpStatus.OK);
        }
    }

    @PutMapping("/paintings/{id}")
    public ResponseEntity<Painting> updatePainting(@PathVariable(value = "id") Long paintId,
                                                   @Validated @RequestBody Painting paintingDetalis) {
        Painting paint = null;
        Optional<Painting> cc = paintingRepository.findById(paintId);
        if (cc.isPresent()) {
            paint = cc.get();
            paint.name = paintingDetalis.name;
            paintingRepository.save(paint);
        } else
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "museum not found"
            );
        return ResponseEntity.ok(paint);
    }

    @DeleteMapping("/paintings/{id}")
    public Map<String, Boolean> deletePainting(@PathVariable(value = "id") Long paintId) {
        Optional<Painting> painting = paintingRepository.findById(paintId);
        Map<String, Boolean> response = new HashMap<>();
        if (painting.isPresent()) {
            paintingRepository.delete(painting.get());
            response.put("deleted", Boolean.TRUE);
        }
        else
            response.put("deleted", Boolean.FALSE);
        return response;
    }
}
