package com.greencross.lims.entity.readonly;

import lombok.Getter;
import lombok.experimental.Accessors;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@SuppressWarnings({"serial", "unchecked"})
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "state")
@Table(schema="public", name = "\"user\"")
@Getter
@Accessors(fluent = true)
public class User implements Serializable {
	@Id
	@Column(name="id")
	private String id;
	@Column(name="name")
	private String name;
	@Column(name="password")
	private String password;
	@Column(name="key")
	private UUID key = UUID.randomUUID();
	@Column(name="department")
	private String department;
	@Column(name="email")
	private String email;
	@Column(name="serial")
	private String serial;
}
