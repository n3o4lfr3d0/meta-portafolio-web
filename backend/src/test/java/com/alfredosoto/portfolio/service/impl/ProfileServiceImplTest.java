package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.ProfileDTO;
import com.alfredosoto.portfolio.entity.ProfileEntity;
import com.alfredosoto.portfolio.repository.ProfileRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProfileServiceImplTest {

    @Mock
    private ProfileRepository profileRepository;

    @InjectMocks
    private ProfileServiceImpl profileService;

    @Test
    void shouldReturnProfileWhenExists() {
        // Arrange
        ProfileEntity entity = new ProfileEntity();
        entity.setName("John Doe");
        entity.setTitle("Developer");
        entity.setSummary("Summary text");
        entity.setLocation("City");
        entity.setExperienceYears("5 years");
        entity.setSpecialization("Java");
        
        ProfileEntity.SocialLinkEntity link = new ProfileEntity.SocialLinkEntity();
        link.setName("GitHub");
        link.setUrl("http://github.com");
        link.setIcon("github");
        
        entity.setSocialLinks(List.of(link));

        when(profileRepository.getProfile("es")).thenReturn(entity);

        // Act
        ProfileDTO result = profileService.getProfile();

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.name());
        assertEquals("Developer", result.title());
        assertEquals(1, result.socialLinks().size());
    }

    @Test
    void shouldReturnFallbackProfileWhenRepositoryReturnsNull() {
        // Arrange
        when(profileRepository.getProfile("es")).thenReturn(null);

        // Act
        ProfileDTO result = profileService.getProfile();

        // Assert
        assertNotNull(result);
        assertEquals("Alfredo Soto", result.name()); // Fallback name
    }

    @Test
    void shouldReturnFallbackProfileOnException() {
        // Arrange
        when(profileRepository.getProfile("es")).thenThrow(new RuntimeException("DB Error"));

        // Act
        ProfileDTO result = profileService.getProfile();

        // Assert
        assertNotNull(result);
        assertEquals("Alfredo Soto", result.name()); // Fallback name
    }
}
