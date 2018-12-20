package com.meimeitech.sys.dialect;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractUser implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String SEQUENCE = Schema.Tables.USER + Schema.SEQ_SUFFIX;

//	@SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE)
//	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
//	@Id
//	@GeneratedValue
	@Id
	@Column(name = "ID",length = 40,unique = true,nullable = false)
	protected Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}