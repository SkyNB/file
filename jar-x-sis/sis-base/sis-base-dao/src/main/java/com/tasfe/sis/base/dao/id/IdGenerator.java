package com.tasfe.sis.base.dao.id;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;

public class IdGenerator implements IdentifierGenerator {
    public static final String TYPE = "com.tasfe.sis.base.dao.id.IdGenerator";

    private static final IdWorker idWorker = new IdWorker();

    @Override
    public Serializable generate(SharedSessionContractImplementor sharedSessionContractImplementor, Object o) throws HibernateException {
        return idWorker.getId();
    }
}