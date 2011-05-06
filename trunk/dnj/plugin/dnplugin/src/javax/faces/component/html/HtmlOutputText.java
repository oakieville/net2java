/*
 * $Id: HtmlOutputText.java,v 1.1 2007/01/05 01:23:02 dannyc Exp $
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
 * <p>Renders the component value as text, optionally
 *       wrapping in a <code>span</code> element if CSS styles
 *       or style classes are specified.</p>
 * <p>By default, the <code>rendererType</code> property must be set to "<code>javax.faces.Text</code>" This value can be changed by calling the <code>setRendererType()</code> method.</p>
*/
public class HtmlOutputText extends javax.faces.component.UIOutput {


  public HtmlOutputText() {
    super();
    setRendererType("javax.faces.Text");
  }


  /*
   * <p>The standard component type for this component.</p>
   */
   public static final String COMPONENT_TYPE = "javax.faces.HtmlOutputText";


  private boolean escape = true;
  private boolean escape_set = false;

  /**
   * <p>Return the value of the <code>escape</code> property.  Contents:</p><p>
   * Flag indicating that characters that are sensitive
   *         in HTML and XML markup must be escaped.  This flag
   *         is set to "true" by default.
   * </p>
   */
  public boolean isEscape() {
    if (this.escape_set) {
      return this.escape;
    }
    ValueBinding _vb = getValueBinding("escape");
    if (_vb != null) {
      Object _result = _vb.getValue(getFacesContext());
      if (_result == null) {
        return false;
      } else {
        return ((Boolean) _result).booleanValue();
      }
    } else {
        return this.escape;
    }
  }

  /**
   * <p>Set the value of the <code>escape</code> property.</p>
   */
  public void setEscape(boolean escape) {
    this.escape = escape;
    this.escape_set = true;
  }


  private java.lang.String style;

  /**
   * <p>Return the value of the <code>style</code> property.  Contents:</p><p>
   * CSS style(s) to be applied when this component is rendered.
   * </p>
   */
  public java.lang.String getStyle() {
    if (null != this.style) {
      return this.style;
    }
    ValueBinding _vb = getValueBinding("style");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>style</code> property.</p>
   */
  public void setStyle(java.lang.String style) {
    this.style = style;
  }


  private java.lang.String styleClass;

  /**
   * <p>Return the value of the <code>styleClass</code> property.  Contents:</p><p>
   * Space-separated list of CSS style class(es) to be applied when
   *           this element is rendered.  This value must be passed through
   *           as the "class" attribute on generated markup.
   * </p>
   */
  public java.lang.String getStyleClass() {
    if (null != this.styleClass) {
      return this.styleClass;
    }
    ValueBinding _vb = getValueBinding("styleClass");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>styleClass</code> property.</p>
   */
  public void setStyleClass(java.lang.String styleClass) {
    this.styleClass = styleClass;
  }


  private java.lang.String title;

  /**
   * <p>Return the value of the <code>title</code> property.  Contents:</p><p>
   * Advisory title information about markup elements generated
   *           for this component.
   * </p>
   */
  public java.lang.String getTitle() {
    if (null != this.title) {
      return this.title;
    }
    ValueBinding _vb = getValueBinding("title");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>title</code> property.</p>
   */
  public void setTitle(java.lang.String title) {
    this.title = title;
  }


  public Object saveState(FacesContext _context) {
    Object _values[] = new Object[6];
    _values[0] = super.saveState(_context);
    _values[1] = this.escape ? Boolean.TRUE : Boolean.FALSE;
    _values[2] = this.escape_set ? Boolean.TRUE : Boolean.FALSE;
    _values[3] = style;
    _values[4] = styleClass;
    _values[5] = title;
    return _values;
  }


  public void restoreState(FacesContext _context, Object _state) {
    Object _values[] = (Object[]) _state;
    super.restoreState(_context, _values[0]);
    this.escape = ((Boolean) _values[1]).booleanValue();
    this.escape_set = ((Boolean) _values[2]).booleanValue();
    this.style = (java.lang.String) _values[3];
    this.styleClass = (java.lang.String) _values[4];
    this.title = (java.lang.String) _values[5];
  }


}

