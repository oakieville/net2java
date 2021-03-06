
 /*
 * $Id: FacesListener.java,v 1.1 2007/01/05 01:23:11 dannyc Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.event;


import java.util.EventListener;


/**
 * <p>A generic base interface for event listeners for various types of
 * {@link FacesEvent}s.  All listener interfaces for specific
 * {@link FacesEvent} event types must extend this interface.</p>
 *
 * <p>Implementations of this interface must have a zero-args public
 * constructor.  If the class that implements this interface has state
 * that needs to be saved and restored between requests, the class must
 * also implement {@link javax.faces.component.StateHolder}.</p>
 */

public interface FacesListener extends EventListener {

}

 