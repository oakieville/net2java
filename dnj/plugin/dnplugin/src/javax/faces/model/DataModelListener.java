
 /*
 * $Id: DataModelListener.java,v 1.1 2007/01/05 01:23:14 dannyc Exp $
 */

/*
 * Copyright 2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.model;


import java.util.EventListener;


/**
 * <p><strong>DataModelListener</strong> represents an event listener that
 * wishes to be notified of {@link DataModelEvent}s occurring on a
 * particular {@link DataModel} instance.</p>
 */

public interface DataModelListener extends EventListener {


    /**
     * <p>Notification that a particular row index, with the associated
     * row data, has been selected for processing.</p>
     *
     * @param event The {@link DataModelEvent} we are processing
     */
    public void rowSelected(DataModelEvent event);


}

 