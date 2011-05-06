/*
 * $Id: HtmlInputHidden.java,v 1.1 2007/01/05 01:23:00 dannyc Exp $
 */

/*
 * Copyright 2003-2004 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package javax.faces.component.html;


import java.io.IOException;
import javax.faces.context.FacesContext;
import javax.faces.el.MethodBinding;
import javax.faces.el.ValueBinding;


/**
 * <p>Represents an HTML <code>input</code> element
 *       of type <code>hidden</code>.</p>
 * <p>By default, the <code>rendererType</code> property must be set to "<code>javax.faces.Hidden</code>" This value can be changed by calling the <code>setRendererType()</code> method.</p>
*/
public class HtmlInputHidden extends javax.faces.component.UIInput {


  public HtmlInputHidden() {
    super();
    setRendererType("javax.faces.Hidden");
  }


  /*
   * <p>The standard component type for this component.</p>
   */
   public static final String COMPONENT_TYPE = "javax.faces.HtmlInputHidden";


  public Object saveState(FacesContext _context) {
    Object _values[] = new Object[1];
    _values[0] = super.saveState(_context);
    return _values;
  }


  public void restoreState(FacesContext _context, Object _state) {
    Object _values[] = (Object[]) _state;
    super.restoreState(_context, _values[0]);
  }


}

