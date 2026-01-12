package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.ProfileDTO;
import com.alfredosoto.portfolio.entity.ProfileEntity;
import com.alfredosoto.portfolio.repository.ProfileRepository;
import com.alfredosoto.portfolio.service.ProfileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileServiceImpl implements ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileServiceImpl.class);

    private final ProfileRepository profileRepository;

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public ProfileDTO getProfile() {
        return getProfile("es");
    }

    @Override
    public ProfileDTO getProfile(String lang) {
        try {
            ProfileEntity entity = profileRepository.getProfile(lang);
            if (entity != null) {
                return mapToDTO(entity);
            }
        } catch (Exception e) {
            logger.error("Error fetching profile from DynamoDB: {}", e.getMessage(), e);
        }

        // Fallback to mock data if DB is empty or fails
        return new ProfileDTO(
            "Alfredo Soto",
            "Ingeniero de Software y Desarrollador Full Stack",
            "+3 años de experiencia. Especializado en el ecosistema Angular & Spring Boot.",
            "Lima, Perú 🇵🇪",
            "+3 años",
            "Angular & Spring Boot",
            List.of(
                new ProfileDTO.SocialLink("LinkedIn", "https://linkedin.com/in/alfredosotonolazco", "linkedin"),
                new ProfileDTO.SocialLink("Email", "mailto:alfredo.soto@example.com", "mail")
            )
        );
    }

    @Override
    public Resource getCv(String lang) {
        String filename = "cv_alfredo_soto_nolazco_2026_spanish.pdf";
        if ("en".equalsIgnoreCase(lang)) {
            filename = "cv_alfredo_soto_nolazco_2026_english.pdf";
        }
        return new ClassPathResource("files/" + filename);
    }

    private ProfileDTO mapToDTO(ProfileEntity entity) {
        List<ProfileDTO.SocialLink> links = entity.getSocialLinks() != null
            ? entity.getSocialLinks().stream()
                .map(l -> new ProfileDTO.SocialLink(l.getName(), l.getUrl(), l.getIcon()))
                .collect(Collectors.toList())
            : Collections.emptyList();

        return new ProfileDTO(
            entity.getName(),
            entity.getTitle(),
            entity.getSummary(),
            entity.getLocation(),
            entity.getExperienceYears(),
            entity.getSpecialization(),
            links
        );
    }
}
