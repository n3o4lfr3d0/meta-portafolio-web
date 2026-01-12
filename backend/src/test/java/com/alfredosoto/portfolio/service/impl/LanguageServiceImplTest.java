package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.entity.LanguageEntity;
import com.alfredosoto.portfolio.repository.LanguageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class LanguageServiceImplTest {

    @Mock
    private LanguageRepository languageRepository;

    private LanguageServiceImpl languageService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        languageService = new LanguageServiceImpl(languageRepository);
    }

    @Test
    void shouldReturnAllLanguagesSortedByPercentage() {
        LanguageEntity lang1 = new LanguageEntity();
        lang1.setName("Java");
        lang1.setPercentage(80);

        LanguageEntity lang2 = new LanguageEntity();
        lang2.setName("Python");
        lang2.setPercentage(90);

        when(languageRepository.findAll("es")).thenReturn(Arrays.asList(lang1, lang2));

        List<LanguageEntity> result = languageService.getAllLanguages("es");

        assertEquals(2, result.size());
        assertEquals("Python", result.get(0).getName()); // Sorted descending
        assertEquals("Java", result.get(1).getName());
        verify(languageRepository, times(1)).findAll("es");
    }

    @Test
    void shouldReturnAllLanguagesDefaultToEs() {
        when(languageRepository.findAll("es")).thenReturn(List.of());

        languageService.getAllLanguages();

        verify(languageRepository, times(1)).findAll("es");
    }
}
