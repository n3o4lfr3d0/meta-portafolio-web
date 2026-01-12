package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.entity.LanguageEntity;
import com.alfredosoto.portfolio.repository.LanguageRepository;
import com.alfredosoto.portfolio.service.LanguageService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository languageRepository;

    public LanguageServiceImpl(LanguageRepository languageRepository) {
        this.languageRepository = languageRepository;
    }

    @Override
    public List<LanguageEntity> getAllLanguages() {
        return getAllLanguages("es");
    }

    @Override
    public List<LanguageEntity> getAllLanguages(String lang) {
        return languageRepository.findAll(lang).stream()
                .sorted(Comparator.comparing(LanguageEntity::getPercentage).reversed())
                .collect(Collectors.toList());
    }
}
