package com.customjobs.networking;

import com.customjobs.networking.utils.CommandExecutors;
import com.rabbitmq.client.Command;
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

	@Test
	void executeScript() {
		CommandExecutors commandExecutors = new CommandExecutors();
		String virtualPath = "C:\\withfloats\\project_ana\\customjobs_networking\\user_scripts\\tanmay\\venv";
		String scriptName = "C:\\withfloats\\project_ana\\customjobs_networking\\user_scripts\\tanmay\\387fe555-27ae-11ea-ba85-7129fc21ddc3.py";
		var output = commandExecutors.executeCommand(String.format("%s\\Scripts\\python.exe", virtualPath), scriptName);
	}

}
