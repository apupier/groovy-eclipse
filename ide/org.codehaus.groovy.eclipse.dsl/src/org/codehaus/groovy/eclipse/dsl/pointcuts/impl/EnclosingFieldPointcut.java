/*******************************************************************************
 * Copyright (c) 2011 Codehaus.org, SpringSource, and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Andrew Eisenberg - Initial implemenation
 *******************************************************************************/
package org.codehaus.groovy.eclipse.dsl.pointcuts.impl;

import org.codehaus.groovy.ast.FieldNode;
import org.codehaus.groovy.eclipse.dsl.pointcuts.AbstractPointcut;
import org.codehaus.groovy.eclipse.dsl.pointcuts.BindingSet;
import org.codehaus.groovy.eclipse.dsl.pointcuts.GroovyDSLDContext;
import org.codehaus.groovy.eclipse.dsl.pointcuts.IPointcut;

/**
 * Tests that the type being analyzed matches.  The match can
 * either be a string match (ie - the type name),
 * or it can pass the current type to a containing pointcut
 * @author andrew
 * @created Feb 10, 2011
 */
public class EnclosingFieldPointcut extends AbstractPointcut {

    public EnclosingFieldPointcut(String containerIdentifier) {
        super(containerIdentifier);
    }

    @Override
    public BindingSet matches(GroovyDSLDContext pattern) {
        FieldNode enclosing = pattern.getCurrentScope().getEnclosingFieldDeclaration();
        if (enclosing == null) {
            return null;
        }
        
        Object firstArgument = getFirstArgument();
        if (firstArgument instanceof String) {
            if (enclosing.getName().equals(firstArgument)) {
                return new BindingSet().addDefaultBinding(enclosing);
            } else {
                return null;
            }
        } else {
            pattern.setOuterPointcutBinding(enclosing);
            BindingSet matches = matchOnPointcutArgument((IPointcut) firstArgument, pattern);
            if (matches != null) {
                matches.addDefaultBinding(enclosing);
            }
            return matches;
        }
    }

    /**
     * expecting one arg that is either a string or a pointcut or a class
     */
    @Override
    public String verify() {
        String oneStringOrOnePointcutArg = oneStringOrOnePointcutArg();
        if (oneStringOrOnePointcutArg == null) {
            return super.verify();
        }
        return oneStringOrOnePointcutArg;
    }
}