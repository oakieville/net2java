
 /*
 * $Id: ReferenceSyntaxException.java,v 1.1 2007/01/05 01:23:10 dannyc Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */


package javax.faces.el;


/**
 * <p>An exception reporting a syntax error in a method binding expression
 * or value binding expression.</p>
 */

public class ReferenceSyntaxException extends EvaluationException {


    /**
     * <p>Construct a new exception with no detail message or root cause.</p>
     */
    public ReferenceSyntaxException() {

        super();

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * no root cause.</p>
     *
     * @param message The detail message for this exception
     */
    public ReferenceSyntaxException(String message) {

        super(message);

    }


    /**
     * <p>Construct a new exception with the specified root cause.  The detail
     * message will be set to <code>(cause == null ? null :
     * cause.toString()</code>
     *
     * @param cause The root cause for this exception
     */
    public ReferenceSyntaxException(Throwable cause) {

        super(cause);

    }


    /**
     * <p>Construct a new exception with the specified detail message and
     * root cause.</p>
     *
     * @param message The detail message for this exception
     * @param cause The root cause for this exception
     */
    public ReferenceSyntaxException(String message, Throwable cause) {

        super(message, cause);

    }



}

 