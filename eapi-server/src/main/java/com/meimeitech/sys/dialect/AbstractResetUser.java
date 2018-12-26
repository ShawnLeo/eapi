package com.meimeitech.sys.dialect;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractResetUser implements Serializable {

	private static final long serialVersionUID = 1L;
//	private static final String SEQUENCE = Schema.Tables.REG_USER + Schema.SEQ_SUFFIX;

//	@SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
//	@Id
//	@GeneratedValue
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "ID",length = 40,unique = true,nullable = false)
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}