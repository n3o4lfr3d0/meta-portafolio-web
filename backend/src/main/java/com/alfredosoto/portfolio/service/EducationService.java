package com.alfredosoto.portfolio.service;

import com.alfredosoto.portfolio.dto.EducationDTO;
import java.util.List;

public interface EducationService {
    List<EducationDTO> getEducation();
    List<EducationDTO> getEducation(String lang);
}
