package com.erebelo.springprofileservice.mapper;

import static org.mapstruct.ReportingPolicy.WARN;

import com.erebelo.springprofileservice.model.dto.request.ProfileRequest;
import com.erebelo.springprofileservice.model.dto.response.ProfileResponse;
import com.erebelo.springprofileservice.model.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring", unmappedTargetPolicy = WARN)
public interface ProfileMapper {

    Profile toEntity(ProfileRequest request);

    void updateEntity(ProfileRequest request, @MappingTarget Profile entity);

    ProfileResponse toResponse(Profile entity);

}
