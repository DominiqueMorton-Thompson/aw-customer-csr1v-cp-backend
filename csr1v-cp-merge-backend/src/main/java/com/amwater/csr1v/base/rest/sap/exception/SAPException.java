package com.amwater.csr1v.base.rest.sap.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

/**
 *  Base SAP Exception class
 * @author Rama P
 *
 */

public class SAPException extends Exception {

	private static final long serialVersionUID = 1L;

    public SAPException(String message)
    {
        super(message);
    }

    public SAPException(String message, Throwable t)
    {
        super(message);
        _baseThrowable = t;
    }

    public void printStackTrace(PrintWriter s)
    {
        super.printStackTrace(s);

        if ( _baseThrowable != null )
        {
            s.print("Underlying Throwable stack trace follows:");
            _baseThrowable.printStackTrace(s);
        }
    }

    public void printStackTrace()
    {
        this.printStackTrace(System.err);
    }

    public void printStackTrace(PrintStream s)
    {
        super.printStackTrace(s);

        if ( _baseThrowable != null )
        {
            s.print("Underlying Throwable stack trace follows:");
            _baseThrowable.printStackTrace(s);
        }
    }
    private Throwable _baseThrowable = null;

}
