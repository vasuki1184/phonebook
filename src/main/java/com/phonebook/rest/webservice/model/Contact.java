package com.phonebook.rest.webservice.model;

public class Contact {

	private Integer id; 
    private String name;
    private String phone;

    public Contact(Integer id, String name, String phone) {
		super();
		this.id = id;
		this.name = name;
		this.phone = phone;
	}

	public Contact(Contact contact) {
        this.name = contact.getName();
        this.phone = contact.getPhone();
    }

    public Contact() {
        super();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}
    

}
