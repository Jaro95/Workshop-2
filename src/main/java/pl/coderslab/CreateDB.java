package pl.coderslab;

public class CreateDB {

    private final String QUERY1 = """
            CREATE TABLE USERS (
            id INT(11) NOT NULL AUTO_INCREMENT PRIMARY KEY,
            email VARCHAR(255) NOT NULL UNIQUE,
            username VARCHAR(255) NOT NULL,
            password VARCHAR(60) NOT NULL
            );
            """;
}
