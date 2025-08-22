package com.tinder.tinderservice.controller;

import com.tinder.tinderservice.dto.CreateProfileRequest;
import com.tinder.tinderservice.dto.ProfileResponse;
import com.tinder.tinderservice.dto.UpdateProfileRequest;
import com.tinder.tinderservice.dto.UserImageURLResponse;
import com.tinder.tinderservice.exception.ErrorResponse;
import com.tinder.tinderservice.service.IProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.net.URL;

@RestController
@RequestMapping("/api/v1/profile")
@Tag(name = "Profile API", description = "Operations related to user profile")
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

    @PostMapping("/image/{id}")
    @Operation(
            summary = "Upload user profile image",
            description = "Uploads a profile image for the given user ID, stores it in S3, and saves the image URL in the database"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image uploaded successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID or file format", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<URL> uploadImage(@PathVariable("id") Long id, @RequestParam("file") MultipartFile file) throws Exception {
        String imageUrl = this.profileService.uploadImage(id, file);
        URL url = new URL(imageUrl);
        return ResponseEntity.ok(url);
    }


    @DeleteMapping("/image/{id}")
    @Operation(
            summary = "Delete user profile image",
            description = "Deletes a profile image for the given user ID. Removes both the image record from the database and the actual file from S3."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image deleted successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID or file name", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User or image not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<String> deleteImage(@PathVariable("id") Long id, @RequestParam String fileName) throws Exception {
        profileService.deleteImage(id, fileName);
        return new ResponseEntity<>("Successfully deleted image of profile: " + id, HttpStatus.OK);
    }

    @GetMapping("/image/{id}")
    @Operation(
            summary = "Get user images",
            description = "Fetches all profile images associated with the given user ID from the database."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Images retrieved successfully", content = @Content(schema = @Schema(implementation = UserImageURLResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID or file name", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User not found or no images exist", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<UserImageURLResponse> getUserImages(@PathVariable("id") Long id) throws Exception {
       UserImageURLResponse userImageURLResponse = this.profileService.getAllUserImages(id);
       return ResponseEntity.ok(userImageURLResponse);
    }

    @PutMapping("/image/{id}")
    @Operation(
            summary = "Update user profile image",
            description = "Updates a specific profile image for the given user ID. The old image will be deleted from S3 and the database, and the new image will be uploaded."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Image updated successfully", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "400", description = "Invalid user ID or file format", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "User or image not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<String> updateImage(
            @Parameter(description = "User ID for whom the image should be updated", example = "101")
            @PathVariable("id") Long id,

            @Parameter(description = "Old file name of the image to be replaced", example = "old_profile.jpg")
            @RequestParam String oldFileName,

            @Parameter(description = "New image file to upload")
            @RequestPart("file") MultipartFile newFile
    ) throws Exception {
        profileService.updateImage(id, oldFileName, newFile);
        return ResponseEntity.ok("Image updated successfully for userId=" + id);
    }

}
