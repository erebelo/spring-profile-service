package com.erebelo.springprofileservice.service.impl;

import com.erebelo.springprofileservice.exception.model.NotFoundException;
import com.erebelo.springprofileservice.mapper.ProfileMapper;
import com.erebelo.springprofileservice.model.dto.request.ProfileRequest;
import com.erebelo.springprofileservice.model.dto.response.ProfileResponse;
import com.erebelo.springprofileservice.model.entity.Profile;
import com.erebelo.springprofileservice.service.ProfileService;
import com.erebelo.springprofileservice.support.DeepObjectComparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.JsonNode;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final MongoTemplate mongoTemplate;
    private final DeepObjectComparator comparator;
    private final ProfileMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<ProfileResponse> findAll() {
        log.info("Fetching all profiles.");

        return mongoTemplate.findAll(Profile.class).stream().map(mapper::toResponse).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProfileResponse findById(String id) {
        log.info("Fetching profile with ID={}.", id);

        Profile profile = mongoTemplate.findById(id, Profile.class);

        if (profile == null) {
            throw new NotFoundException("Profile not found with ID: " + id);
        }

        return mapper.toResponse(profile);
    }

    @Override
    @Transactional
    public ProfileResponse upsert(ProfileRequest request) {
        log.info("Upserting profile. firstName={}, lastName={}", request.firstName(), request.lastName());

        Query query = new Query(
                Criteria.where("firstName").is(request.firstName()).and("lastName").is(request.lastName()));
        Profile existingProfile = mongoTemplate.findOne(query, Profile.class);
        Profile profile;

        if (existingProfile != null) {
            log.info("Found existing profile, updating with ID={}.", existingProfile.getId());

            JsonNode original = comparator.toTypedTree(existingProfile);

            mapper.updateEntity(request, existingProfile);
            profile = existingProfile;

            if (!comparator.deepEquals(original, comparator.toTypedTree(profile))) {
                mongoTemplate.save(profile);

                log.info("Successfully updated profile with ID={}.", profile.getId());
            } else {
                log.info("No changes detected for profile with ID={}.", profile.getId());
            }
        } else {
            log.info("No existing profile found, creating new one.");

            profile = mapper.toEntity(request);
            mongoTemplate.insert(profile);

            log.info("Successfully inserted profile with ID={}.", profile.getId());
        }

        return mapper.toResponse(profile);
    }
}
