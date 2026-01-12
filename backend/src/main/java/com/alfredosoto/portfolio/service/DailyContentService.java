package com.alfredosoto.portfolio.service;

import com.alfredosoto.portfolio.dto.DailyContentDTO;

public interface DailyContentService {
    DailyContentDTO getDailyContent(String lang);
    
    // Overload for backward compatibility
    default DailyContentDTO getDailyContent() {
        return getDailyContent("es");
    }
}
