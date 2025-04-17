package com.example.demo;

import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.stereotype.Component;

@Component
public class TenantIdentifierResolver implements CurrentTenantIdentifierResolver<String> {


    public void setCurrentTenant(String tenant) {
        TenantContext.setCurrentTenant(tenant);
    }

    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenant = TenantContext.getCurrentTenant();
        System.out.println("get connection "+ tenant);
        return tenant;

    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}