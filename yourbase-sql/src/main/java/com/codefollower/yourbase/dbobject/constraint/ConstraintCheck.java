/*
 * Copyright 2004-2011 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package com.codefollower.yourbase.dbobject.constraint;

import java.util.HashSet;
import java.util.Iterator;

import com.codefollower.yourbase.constant.ErrorCode;
import com.codefollower.yourbase.dbobject.Schema;
import com.codefollower.yourbase.dbobject.index.Index;
import com.codefollower.yourbase.dbobject.table.Column;
import com.codefollower.yourbase.dbobject.table.Table;
import com.codefollower.yourbase.dbobject.table.TableFilter;
import com.codefollower.yourbase.engine.Session;
import com.codefollower.yourbase.expression.Expression;
import com.codefollower.yourbase.expression.ExpressionVisitor;
import com.codefollower.yourbase.message.DbException;
import com.codefollower.yourbase.result.ResultInterface;
import com.codefollower.yourbase.result.Row;
import com.codefollower.yourbase.util.New;
import com.codefollower.yourbase.util.StringUtils;

/**
 * A check constraint.
 */
public class ConstraintCheck extends Constraint {

    private TableFilter filter;
    private Expression expr;

    public ConstraintCheck(Schema schema, int id, String name, Table table) {
        super(schema, id, name, table);
    }

    public String getConstraintType() {
        return Constraint.CHECK;
    }

    public void setTableFilter(TableFilter filter) {
        this.filter = filter;
    }

    public void setExpression(Expression expr) {
        this.expr = expr;
    }

    public String getCreateSQLForCopy(Table forTable, String quotedName) {
        StringBuilder buff = new StringBuilder("ALTER TABLE ");
        buff.append(forTable.getSQL()).append(" ADD CONSTRAINT ");
        if (forTable.isHidden()) {
            buff.append("IF NOT EXISTS ");
        }
        buff.append(quotedName);
        if (comment != null) {
            buff.append(" COMMENT ").append(StringUtils.quoteStringSQL(comment));
        }
        buff.append(" CHECK").append(StringUtils.enclose(expr.getSQL())).append(" NOCHECK");
        return buff.toString();
    }

    private String getShortDescription() {
        return getName() + ": " + expr.getSQL();
    }

    public String  getCreateSQLWithoutIndexes() {
        return getCreateSQL();
    }

    public String getCreateSQL() {
        return getCreateSQLForCopy(table, getSQL());
    }

    public void removeChildrenAndResources(Session session) {
        table.removeConstraint(this);
        database.removeMeta(session, getId());
        filter = null;
        expr = null;
        table = null;
        invalidate();
    }

    public void checkRow(Session session, Table t, Row oldRow, Row newRow) {
        if (newRow == null) {
            return;
        }
        filter.set(newRow);
        // Both TRUE and NULL are ok
        if (Boolean.FALSE.equals(expr.getValue(session).getBoolean())) {
            throw DbException.get(ErrorCode.CHECK_CONSTRAINT_VIOLATED_1, getShortDescription());
        }
    }

    public boolean usesIndex(Index index) {
        return false;
    }

    public void setIndexOwner(Index index) {
        DbException.throwInternalError();
    }

    public HashSet<Column> getReferencedColumns(Table table) {
        HashSet<Column> columns = New.hashSet();
        expr.isEverything(ExpressionVisitor.getColumnsVisitor(columns));
        for (Iterator<Column> it = columns.iterator(); it.hasNext();) {
            if (it.next().getTable() != table) {
                it.remove();
            }
        }
        return columns;
    }

    public Expression getExpression() {
        return expr;
    }

    public boolean isBefore() {
        return true;
    }

    public void checkExistingData(Session session) {
        if (session.getDatabase().isStarting()) {
            // don't check at startup
            return;
        }
        String sql = "SELECT 1 FROM " + filter.getTable().getSQL() + " WHERE NOT(" + expr.getSQL() + ")";
        ResultInterface r = session.prepare(sql).query(1);
        if (r.next()) {
            throw DbException.get(ErrorCode.CHECK_CONSTRAINT_VIOLATED_1, getName());
        }
    }

    public Index getUniqueIndex() {
        return null;
    }

    public void rebuild() {
        // nothing to do
    }

    public boolean isEverything(ExpressionVisitor visitor) {
        return expr.isEverything(visitor);
    }

}
