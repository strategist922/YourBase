/*
 * Copyright 2004-2011 H2 Group. Multiple-Licensed under the H2 License,
 * Version 1.0, and under the Eclipse Public License, Version 1.0
 * (http://h2database.com/html/license.html).
 * Initial Developer: H2 Group
 */
package com.codefollower.yourbase.command.ddl;

import java.util.ArrayList;

import com.codefollower.yourbase.command.CommandInterface;
import com.codefollower.yourbase.command.Prepared;
import com.codefollower.yourbase.dbobject.Procedure;
import com.codefollower.yourbase.engine.Session;
import com.codefollower.yourbase.expression.Parameter;
import com.codefollower.yourbase.util.New;

/**
 * This class represents the statement
 * PREPARE
 */
public class PrepareProcedure extends DefineCommand {

    private String procedureName;
    private Prepared prepared;

    public PrepareProcedure(Session session) {
        super(session);
    }

    public void checkParameters() {
        // no not check parameters
    }

    public int update() {
        Procedure proc = new Procedure(procedureName, prepared);
        prepared.setParameterList(parameters);
        prepared.setPrepareAlways(prepareAlways);
        prepared.prepare();
        session.addProcedure(proc);
        return 0;
    }

    public void setProcedureName(String name) {
        this.procedureName = name;
    }

    public void setPrepared(Prepared prep) {
        this.prepared = prep;
    }

    public ArrayList<Parameter> getParameters() {
        return New.arrayList();
    }

    public int getType() {
        return CommandInterface.PREPARE;
    }

}
