package com.digvijayb.multitenant;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.spi.MultiTenantConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ConnectionProvider implements MultiTenantConnectionProvider<String>, HibernatePropertiesCustomizer {

	@Autowired
	DataSource dataSource;

	@Override
	public Connection getAnyConnection() throws SQLException {
		log.debug("getAnyConnection()");
		return getConnection("PUBLIC");
	}

	@Override
	public void releaseAnyConnection(Connection connection) throws SQLException {
		connection.close();
	}

	@Override
	public Connection getConnection(String schema) throws SQLException {
		log.debug("Get Connection for {}", schema);
		Connection connection = dataSource.getConnection();
		connection.setSchema(schema);
		return connection;
	}

	@Override
	public void releaseConnection(String s, Connection connection) throws SQLException {
		connection.setSchema("PUBLIC");
		connection.close();
	}

	@Override
	public boolean supportsAggressiveRelease() {
		return false;
	}

	@Override
	public void customize(Map<String, Object> hibernateProperties) {
		hibernateProperties.put(AvailableSettings.MULTI_TENANT_CONNECTION_PROVIDER, this);
	}

	@Override
	public boolean isUnwrappableAs(Class<?> unwrapType) {
		return false;
	}

	@Override
	public <T> T unwrap(Class<T> unwrapType) {
		return null;
	}


}
