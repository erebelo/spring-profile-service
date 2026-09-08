package com.erebelo.springprofileservice.service;

import com.erebelo.springprofileservice.model.dto.request.ProfileRequest;
import com.erebelo.springprofileservice.model.dto.response.ProfileResponse;
import jakarta.validation.Valid;
import java.util.List;

public interface ProfileService {

    List<ProfileResponse> findAll();

    ProfileResponse findById(String id);

    ProfileResponse upsert(@Valid ProfileRequest request);

}
