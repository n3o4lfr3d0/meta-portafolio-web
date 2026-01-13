package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.entity.LanguageEntity;
import com.alfredosoto.portfolio.repository.LanguageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LanguageServiceImplTest {

    @Mock
    private LanguageRepository languageRepository;

    @InjectMocks
    private LanguageServiceImpl languageService;

    private LanguageEntity lang1;
    private LanguageEntity lang2;

    @BeforeEach
    void setUp() {
        lang1 = new LanguageEntity();
        lang1.setId("1");
        lang1.setName("Java");
        lang1.setPercentage(90);
        lang1.setLanguage("es");

        lang2 = new LanguageEntity();
        lang2.setId("2");
        lang2.setName("Python");
        lang2.setPercentage(80);
        lang2.setLanguage("es");
    }

    @Test
    void shouldGetAllLanguagesDefault() {
        when(languageRepository.findAll("es")).thenReturn(Arrays.asList(lang1, lang2));

        List<LanguageEntity> result = languageService.getAllLanguages();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Java", result.get(0).getName());
        verify(languageRepository).findAll("es");
    }

    @Test
    void shouldGetAllLanguagesByLang() {
        when(languageRepository.findAll("en")).thenReturn(Arrays.asList(lang1, lang2));

        List<LanguageEntity> result = languageService.getAllLanguages("en");

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(languageRepository).findAll("en");
    }

    @Test
    void shouldSortLanguagesByPercentageDesc() {
        // lang2 has 80, lang1 has 90. Order should be lang1, lang2.
        // Let's modify percentages to be sure
        lang1.setPercentage(50);
        lang2.setPercentage(100);

        when(languageRepository.findAll("es")).thenReturn(Arrays.asList(lang1, lang2));

        List<LanguageEntity> result = languageService.getAllLanguages("es");

        assertEquals("Python", result.get(0).getName()); // 100%
        assertEquals("Java", result.get(1).getName());   // 50%
    }
}
