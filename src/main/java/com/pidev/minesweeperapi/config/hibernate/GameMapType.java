package com.pidev.minesweeperapi.config.hibernate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pidev.minesweeperapi.model.GameMap;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class GameMapType implements UserType {

    @Override
    public int[] sqlTypes() {
        return new int[]{Types.JAVA_OBJECT};
    }

    @Override
    public Class<GameMap> returnedClass() {
        return GameMap.class;
    }

    @Override
    public boolean equals(Object o1, Object o2) throws HibernateException {
        if (o1 == null) {
            return o2 == null;
        }
        return o1.equals(o2);
    }

    @Override
    public int hashCode(Object o) throws HibernateException {
        return o.hashCode();
    }

    @Override
    public Object nullSafeGet(
            final ResultSet resultSet,
            final String[] names,
            final SharedSessionContractImplementor session,
            final Object owner
    ) throws HibernateException, SQLException {
        final String cellContent = resultSet.getString(names[0]);
        if (cellContent == null) {
            return null;
        }
        try {
            final ObjectMapper mapper = new ObjectMapper();
            return mapper.readValue(cellContent.getBytes("UTF-8"), returnedClass());
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert String to Invoice: " + ex.getMessage(), ex);
        }
    }

    @Override
    public void nullSafeSet(
            final PreparedStatement preparedStatement,
            final Object value,
            final int idx,
            final SharedSessionContractImplementor session
    ) throws HibernateException, SQLException {
        if (value == null) {
            preparedStatement.setNull(idx, Types.OTHER);
            return;
        }
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final StringWriter w = new StringWriter();
            mapper.writeValue(w, value);
            w.flush();
            preparedStatement.setObject(idx, w.toString(), Types.OTHER);
        } catch (final Exception ex) {
            throw new RuntimeException("Failed to convert Invoice to String: " + ex.getMessage(), ex);
        }
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        try {
            // use serialization to create a deep copy
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(bos);
            oos.writeObject(value);
            oos.flush();
            oos.close();
            bos.close();

            ByteArrayInputStream bais = new ByteArrayInputStream(bos.toByteArray());
            return new ObjectInputStream(bais).readObject();
        } catch (ClassNotFoundException | IOException ex) {
            throw new HibernateException(ex);
        }
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object o) throws HibernateException {
        return (Serializable) this.deepCopy(o);
    }

    @Override
    public Object assemble(Serializable serializable, Object o) throws HibernateException {
        return this.deepCopy(serializable);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return this.deepCopy(original);
    }
}
