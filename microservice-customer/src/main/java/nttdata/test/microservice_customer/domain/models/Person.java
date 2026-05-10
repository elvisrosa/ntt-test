package nttdata.test.microservice_customer.domain.models;

public class Person {
    
    private String name;
    private String gender;
    private String identification;
    private String address;
    private String phone;

    public Person() {
    }

    public Person(String name, String gender, String identification, String address, String phone) {
        this.name = name;
        this.gender = gender;
        this.identification = identification;
        this.address = address;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getIdentification() {
        return identification;
    }

    public void setIdentification(String identification) {
        this.identification = identification;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
