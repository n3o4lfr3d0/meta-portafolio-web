package com.alfredosoto.portfolio.service.impl;

import com.alfredosoto.portfolio.entity.ProjectInfoEntity;
import com.alfredosoto.portfolio.repository.ProjectInfoRepository;
import com.alfredosoto.portfolio.service.ProjectInfoService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectInfoServiceImpl implements ProjectInfoService {

    private final ProjectInfoRepository projectInfoRepository;

    public ProjectInfoServiceImpl(ProjectInfoRepository projectInfoRepository) {
        this.projectInfoRepository = projectInfoRepository;
    }

    @Override
    public List<ProjectInfoEntity> getProjectInfo(String language) {
        return projectInfoRepository.findByLanguage(language);
    }
}
