package com.alfredosoto.portfolio.service;

import com.alfredosoto.portfolio.entity.ProjectInfoEntity;

import java.util.List;

public interface ProjectInfoService {
    List<ProjectInfoEntity> getProjectInfo(String language);
}
