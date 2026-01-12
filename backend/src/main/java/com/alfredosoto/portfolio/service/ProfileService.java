package com.alfredosoto.portfolio.service;

import com.alfredosoto.portfolio.dto.ProfileDTO;
import org.springframework.core.io.Resource;

public interface ProfileService {
    ProfileDTO getProfile();
    ProfileDTO getProfile(String lang);
    /**
     * Obtiene el CV en el idioma especificado.
     * @param lang Idioma ("es" o "en")
     * @return Recurso del CV
     */
    Resource getCv(String lang);
}
