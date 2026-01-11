package com.alfredosoto.portfolio;

import com.alfredosoto.portfolio.controller.SkillController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PortfolioApplicationTests {

	@Autowired
	private SkillController skillController;

	@Test
	void contextLoads() {
		assertThat(skillController).isNotNull();
	}

}
