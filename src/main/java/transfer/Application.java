package transfer;

import io.micronaut.runtime.Micronaut;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@OpenAPIDefinition(
        info = @Info(
                title = "Transfer service",
                version = "1.0",
                description = "RESTful API for money transfer between accounts",
                license = @License(name = "Apache 2.0", url = "max.kilin@gmail.com"),
                contact = @Contact(name = "Massita", email = "max.kilin@gmail.com")
        )
)
public class Application {

    public static void main(String[] args) {
        Micronaut.run(Application.class);
    }
}