package com.example.backend;

import jakarta.persistence.*;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String email;

		public User(){}
		public User (String _name, String _email){
			this.name = _name;
			this.email = _email;
		}
    // Getter & Setter
		public String getName() {
			return name;
		}

		public String getEmail() {
			return email;
		}
		
		public Long getId() {
			return id;
		}
}
