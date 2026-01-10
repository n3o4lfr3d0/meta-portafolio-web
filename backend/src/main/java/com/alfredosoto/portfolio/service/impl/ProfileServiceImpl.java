package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.ProfileDTO;
import com.alfredosoto.portfolio.service.ProfileService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProfileServiceImpl implements ProfileService {

    @Override
    public ProfileDTO getProfile() {
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
}
