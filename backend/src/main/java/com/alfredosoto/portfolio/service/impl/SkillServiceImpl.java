package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.dto.SkillDTO;
import com.alfredosoto.portfolio.entity.SkillEntity;
import com.alfredosoto.portfolio.repository.SkillRepository;
import com.alfredosoto.portfolio.service.SkillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SkillServiceImpl implements SkillService {

    private static final Logger logger = LoggerFactory.getLogger(SkillServiceImpl.class);

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public List<SkillDTO> getAllSkills() {
        return getAllSkills("es");
    }

    @Override
    public List<SkillDTO> getAllSkills(String lang) {
        try {
            List<SkillEntity> entities = skillRepository.findAll(lang);
            if (!entities.isEmpty()) {
                return entities.stream()
                        .map(this::mapToDTO)
                        .collect(Collectors.toList());
            }
        } catch (Exception e) {
            logger.error("Error fetching skills from DynamoDB: {}", e.getMessage(), e);
        }

        // Return empty list if no data found or error occurs
        return List.of();
    }

    private SkillDTO mapToDTO(SkillEntity entity) {
        return new SkillDTO(
            entity.getName(),
            entity.getCategory(),
            entity.getLevel(),
            entity.getIcon()
        );
    }
}
