package com.tinder.tinderservice.controller;

import com.tinder.tinderservice.dto.CreateProfileRequest;
import com.tinder.tinderservice.dto.ProfileResponse;
import com.tinder.tinderservice.dto.UpdateProfileRequest;
import com.tinder.tinderservice.exception.ErrorResponse;
import com.tinder.tinderservice.service.IProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {

    private final IProfileService profileService;

    public ProfileController(IProfileService profileService) {
        this.profileService = profileService;
    }

    @PostMapping
    @Operation(
            summary = "Create a new profile",
            description = "Creates a new user profile based on the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile created successfully", content = @Content(schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProfileResponse> createProfile(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Profile request body",
            required = true) @Valid @RequestBody CreateProfileRequest profileRequest)throws Exception{
        ProfileResponse response = this.profileService.createProfile(profileRequest);
        return ResponseEntity.ok(response);
    }

    @PutMapping
    @Operation(
            summary = "Update existing profile",
            description = "Update existing user profile based on the provided information"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully", content = @Content(schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProfileResponse> editProfile(@io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Profile request body",
            required = true)
            @Valid @RequestBody UpdateProfileRequest updateProfileRequest)throws Exception{
        ProfileResponse response = this.profileService.updateProfile(updateProfileRequest);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete existing profile",
            description = "Deletes a user profile based on the provided user ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile deleted successfully",content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID provided", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<String> deleteProfile(@PathVariable("id") Long id) throws Exception {
        profileService.deleteProfile(id);
        return new ResponseEntity<>("Successfully deleted profileId: " + id, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get user profile by ID",
            description = "Retrieves the user profile details for the given user ID"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile retrieved successfully",content = @Content(schema = @Schema(implementation = ProfileResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Profile not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<ProfileResponse> getProfile(@PathVariable("id") Long id) throws Exception {
        ProfileResponse response = this.profileService.getUserProfileBYId(id);
        return ResponseEntity.ok(response);
    }


}
