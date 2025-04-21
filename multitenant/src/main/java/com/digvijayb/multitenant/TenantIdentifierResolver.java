package com.digvijayb.multitenant;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver, HibernatePropertiesCustomizer {

    private String tenant = "public";

    public void setCurrentTenant(String tenant) {
        TenantContext.setCurrentTenant(tenant);
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        String identifier = TenantContext.getCurrentTenant();
        System.out.println("identifier = " + identifier);
        return identifier;
    }

    @Override
    public void customize(Map<String, Object> hibernateProperties) {
        hibernateProperties.put(AvailableSettings.MULTI_TENANT_IDENTIFIER_RESOLVER, this);
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
