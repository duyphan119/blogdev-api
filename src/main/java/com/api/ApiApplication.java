package com.api;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		String userDir = System.getProperty("user.dir");
System.out.println(userDir+"-----------------");
        // Construct the file path
        String filePath = userDir + File.separator + ".env";

        // Create a File object with the constructed path
        File file = new File(filePath);

        // Check if the file exists
        if (file.exists()) {
            System.out.println("The .env file exists at: " + file.getAbsolutePath());
        } else {
            System.out.println("The .env file does not exist at: " + file.getAbsolutePath());
        }
		SpringApplication.run(ApiApplication.class, args);
	}

}
