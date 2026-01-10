package com.alfredosoto.portfolio.service;

import com.alfredosoto.portfolio.dto.ProfileDTO;
import com.alfredosoto.portfolio.service.impl.ProfileServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Test
    void shouldReturnProfileData() {
        ProfileDTO profile = profileService.getProfile();

        assertNotNull(profile);
        assertEquals("Alfredo Soto", profile.name());
        assertFalse(profile.socialLinks().isEmpty());
    }
}
