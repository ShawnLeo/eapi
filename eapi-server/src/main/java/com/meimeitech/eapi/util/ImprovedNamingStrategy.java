package com.meimeitech.eapi.util;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;
import org.springframework.util.StringUtils;

public class ImprovedNamingStrategy implements PhysicalNamingStrategy {

	@Override
	public Identifier toPhysicalCatalogName(Identifier identifier, JdbcEnvironment jdbcEnv) {
		return convert(identifier);
	}

	@Override
	public Identifier toPhysicalColumnName(Identifier identifier, JdbcEnvironment jdbcEnv) {
		return convert(identifier);
	}

	@Override
	public Identifier toPhysicalSchemaName(Identifier identifier, JdbcEnvironment jdbcEnv) {
		return convert(identifier);
	}

	@Override
	public Identifier toPhysicalSequenceName(Identifier identifier, JdbcEnvironment jdbcEnv) {
		return convert(identifier);
	}

	@Override
	public Identifier toPhysicalTableName(Identifier identifier, JdbcEnvironment jdbcEnv) {
		return convert(identifier);
	}

	private Identifier convert(Identifier identifier) {
		if (identifier == null || StringUtils.isEmpty(identifier.getText())) {
			return identifier;
		}

		String regex = "([a-z])([A-Z])";
		String replacement = "$1_$2";
		String newName = identifier.getText().replaceAll(regex, replacement).toLowerCase();
		return Identifier.toIdentifier(newName);
	}
}
