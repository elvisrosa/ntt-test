package nttdata.test.microservice_customer.domain.models;

import java.util.UUID;

public class Client extends Person {

    private UUID id;

    private String password;
    private Boolean status;

    public Client() {
        super();
    }

    public Client(String name, String gender, String identification, String address,
            String phone, String password, Boolean status) {
        super(name, gender, identification, address, phone);
        this.password = password;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
