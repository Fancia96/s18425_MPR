package aplikacja.projekt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Optional;

@SpringBootApplication
public class ProjektApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjektApplication.class, args);

	}

	public static String personNotFound = "~~Person not found~~";

	public static String relationNotFound = "~~Relation not found~~";

	public static String messageNotFound = "~~Message not found~~";

	public static String[] badWords = {"milk", "league", "hai", "java"};

}
