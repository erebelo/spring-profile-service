package com.erebelo.springprofileservice.controller;

import com.erebelo.springprofileservice.model.dto.request.ProfileRequest;
import com.erebelo.springprofileservice.model.dto.response.ProfileResponse;
import com.erebelo.springprofileservice.service.ProfileService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/profiles")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService service;

    @GetMapping
    public List<ProfileResponse> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProfileResponse findById(@PathVariable String id) {
        return service.findById(id);
    }

    @PostMapping
    public ProfileResponse insert(@Valid @RequestBody ProfileRequest request) {
        return service.upsert(request);
    }
}
