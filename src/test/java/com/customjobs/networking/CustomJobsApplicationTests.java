package com.customjobs.networking;

import com.customjobs.networking.utils.CommandExecutors;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CustomJobsApplicationTests {

	@Test
	void contextLoads() {
		//CommandExecutors.executeCommand("help");
	}

	@Test
	void navigateToDirectory() {
		//CommandExecutors.executeCommand(String.format("cd %s", "c:\\withfloats"));
	}

}
