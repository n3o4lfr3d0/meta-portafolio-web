package com.alfredosoto.portfolio.service;

import com.alfredosoto.portfolio.entity.LanguageEntity;
import java.util.List;

public interface LanguageService {
    List<LanguageEntity> getAllLanguages();
    List<LanguageEntity> getAllLanguages(String lang);
}
