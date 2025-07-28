package com.tinder.tinderservice.mapper;

import com.tinder.tinderservice.dto.*;
import com.tinder.tinderservice.entity.Address;
import com.tinder.tinderservice.entity.Geolocation;
import com.tinder.tinderservice.entity.User;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {

    public static User getProfileEntity(CreateProfileRequest profileRequest) {
        AddressResponse addressResponse = profileRequest.getAddress();
        GeolocationResponse geolocationResponse = addressResponse.getGeolocation();

        Geolocation geolocation = GeolocationMapper.getGeoLocationEntity(geolocationResponse);
        Address address = AddressMapper.getAddressEntity(addressResponse, geolocation);

        User user = User.builder()
                .name(profileRequest.getName())
                .age(profileRequest.getAge())
                .gender(profileRequest.getGender())
                .sexualPreference(profileRequest.getSexualPreference())
                .job(profileRequest.getJob())
                .bio(profileRequest.getBio())
                .address(address)
                .build();
        return user;
    }

    public static ProfileResponse getProfileResponse(User userProfile) {
        GeolocationResponse geolocationResponse = GeolocationMapper.getGeolocationResponse(userProfile.getAddress().getGeoLocation());
        AddressResponse addressResponse = AddressMapper.getAddressResponse(userProfile.getAddress(), geolocationResponse);

        ProfileResponse profileResponse = ProfileResponse.builder()
                .id(userProfile.getId())
                .name(userProfile.getName())
                .age(userProfile.getAge())
                .gender(userProfile.getGender())
                .sexualPreference(userProfile.getSexualPreference())
                .job(userProfile.getJob())
                .bio(userProfile.getBio())
                .address(addressResponse)
                .build();
        return profileResponse;
    }

    public static CreateProfileRequest getCreateProfileRequestDTO(UpdateProfileRequest updateProfileRequest) {
        CreateProfileRequest createProfileRequest = CreateProfileRequest.builder()
                .name(updateProfileRequest.getName())
                .age(updateProfileRequest.getAge())
                .gender(updateProfileRequest.getGender())
                .sexualPreference(updateProfileRequest.getSexualPreference())
                .job(updateProfileRequest.getJob())
                .bio(updateProfileRequest.getBio())
                .address(updateProfileRequest.getAddress())
                .build();
        return createProfileRequest;
    }


    public static void updateUserFromDto(User user, UpdateProfileRequest dto) {
        if (dto.getName() != null) {
            user.setName(dto.getName());
        }
        if (dto.getAge() != null) {
            user.setAge(dto.getAge());
        }
        if (dto.getGender() != null) {
            user.setGender(dto.getGender());
        }
        if (dto.getSexualPreference() != null) {
            user.setSexualPreference(dto.getSexualPreference());
        }
        if (dto.getJob() != null) {
            user.setJob(dto.getJob());
        }
        if (dto.getBio() != null) {
            user.setBio(dto.getBio());
        }

        // Handle nested Address
        AddressResponse addressResponse = dto.getAddress();
        if (addressResponse != null) {
            Address address = user.getAddress();
            if (address == null) {
                address = new Address();
                user.setAddress(address);
            }

            if (addressResponse.getStreet() != null) {
                address.setStreet(addressResponse.getStreet());
            }
            if (addressResponse.getCity() != null) {
                address.setCity(addressResponse.getCity());
            }
            if (addressResponse.getState() != null) {
                address.setState(addressResponse.getState());
            }
            if (addressResponse.getCountry() != null) {
                address.setCountry(addressResponse.getCountry());
            }
            if (addressResponse.getPinCode() != null) {
                address.setPinCode(addressResponse.getPinCode());
            }

            // Handle nested Geolocation
            GeolocationResponse geolocationResponse = addressResponse.getGeolocation();
            if (geolocationResponse != null) {
                Geolocation geo = address.getGeoLocation();
                if (geo == null) {
                    geo = new Geolocation();
                    address.setGeoLocation(geo);
                }

                if (geolocationResponse.getLatitude() != null) {
                    geo.setLatitude(geolocationResponse.getLatitude());
                }
                if (geolocationResponse.getLongitude() != null) {
                    geo.setLongitude(geolocationResponse.getLongitude());
                }
            }
        }
    }


    public static UserDTO getUserDTO(User user) {
        GeolocationResponse geolocationResponse = GeolocationMapper.getGeolocationResponse(user.getAddress().getGeoLocation());
        AddressResponse addressResponse = AddressMapper.getAddressResponse(user.getAddress(), geolocationResponse);
        addressResponse.setGeolocation(geolocationResponse);

        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .age(user.getAge())
                .gender(user.getGender())
                .sexualPreference(user.getSexualPreference())
                .job(user.getJob())
                .bio(user.getBio())
                .address(addressResponse)
                .build();
        return userDTO;
    }
}
