/*
 * Copyright 2004-2011 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package com.codefollower.yourbase.dbobject.index;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.codefollower.yourbase.dbobject.table.Column;
import com.codefollower.yourbase.dbobject.table.TableLink;
import com.codefollower.yourbase.engine.Session;
import com.codefollower.yourbase.message.DbException;
import com.codefollower.yourbase.result.Row;
import com.codefollower.yourbase.result.SearchRow;
import com.codefollower.yourbase.value.DataType;
import com.codefollower.yourbase.value.Value;

/**
 * The cursor implementation for the linked index.
 */
public class LinkedCursor implements Cursor {

    private final TableLink tableLink;
    private final PreparedStatement prep;
    private final String sql;
    private final Session session;
    private final ResultSet rs;
    private Row current;

    LinkedCursor(TableLink tableLink, ResultSet rs, Session session, String sql, PreparedStatement prep) {
        this.session = session;
        this.tableLink = tableLink;
        this.rs = rs;
        this.sql = sql;
        this.prep = prep;
    }

    public Row get() {
        return current;
    }

    public SearchRow getSearchRow() {
        return current;
    }

    public boolean next() {
        try {
            boolean result = rs.next();
            if (!result) {
                rs.close();
                tableLink.reusePreparedStatement(prep, sql);
                current = null;
                return false;
            }
        } catch (SQLException e) {
            throw DbException.convert(e);
        }
        current = tableLink.getTemplateRow();
        for (int i = 0; i < current.getColumnCount(); i++) {
            Column col = tableLink.getColumn(i);
            Value v = DataType.readValue(session, rs, i + 1, col.getType());
            current.setValue(i, v);
        }
        return true;
    }

    public boolean previous() {
        throw DbException.throwInternalError();
    }

}
