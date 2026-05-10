package nttdata.test.microservice_customer.infrastructure.persistence.r2dbc;

import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@SuperBuilder
public class Person {

    private String name;

    private String gender;

    private String identification;

    private String address;

    private String phone;

}
