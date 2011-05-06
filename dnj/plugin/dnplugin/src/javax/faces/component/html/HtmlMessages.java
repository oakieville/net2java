/*
 * $Id: HtmlMessages.java,v 1.1 2007/01/05 01:23:01 dannyc Exp $
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
 * <p>By default, the <code>rendererType</code> property must be set to "<code>javax.faces.Messages</code>" This value can be changed by calling the <code>setRendererType()</code> method.</p>
*/
public class HtmlMessages extends javax.faces.component.UIMessages {


  public HtmlMessages() {
    super();
    setRendererType("javax.faces.Messages");
  }


  /*
   * <p>The standard component type for this component.</p>
   */
   public static final String COMPONENT_TYPE = "javax.faces.HtmlMessages";


  private java.lang.String errorClass;

  /**
   * <p>Return the value of the <code>errorClass</code> property.  Contents:</p><p>
   * CSS style class to apply to any message
   *           with a severity class of "ERROR".
   * </p>
   */
  public java.lang.String getErrorClass() {
    if (null != this.errorClass) {
      return this.errorClass;
    }
    ValueBinding _vb = getValueBinding("errorClass");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>errorClass</code> property.</p>
   */
  public void setErrorClass(java.lang.String errorClass) {
    this.errorClass = errorClass;
  }


  private java.lang.String errorStyle;

  /**
   * <p>Return the value of the <code>errorStyle</code> property.  Contents:</p><p>
   * CSS style(s) to apply to any message
   *           with a severity class of "ERROR".
   * </p>
   */
  public java.lang.String getErrorStyle() {
    if (null != this.errorStyle) {
      return this.errorStyle;
    }
    ValueBinding _vb = getValueBinding("errorStyle");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>errorStyle</code> property.</p>
   */
  public void setErrorStyle(java.lang.String errorStyle) {
    this.errorStyle = errorStyle;
  }


  private java.lang.String fatalClass;

  /**
   * <p>Return the value of the <code>fatalClass</code> property.  Contents:</p><p>
   * CSS style class to apply to any message
   *           with a severity class of "FATAL".
   * </p>
   */
  public java.lang.String getFatalClass() {
    if (null != this.fatalClass) {
      return this.fatalClass;
    }
    ValueBinding _vb = getValueBinding("fatalClass");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>fatalClass</code> property.</p>
   */
  public void setFatalClass(java.lang.String fatalClass) {
    this.fatalClass = fatalClass;
  }


  private java.lang.String fatalStyle;

  /**
   * <p>Return the value of the <code>fatalStyle</code> property.  Contents:</p><p>
   * CSS style(s) to apply to any message
   *           with a severity class of "FATAL".
   * </p>
   */
  public java.lang.String getFatalStyle() {
    if (null != this.fatalStyle) {
      return this.fatalStyle;
    }
    ValueBinding _vb = getValueBinding("fatalStyle");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>fatalStyle</code> property.</p>
   */
  public void setFatalStyle(java.lang.String fatalStyle) {
    this.fatalStyle = fatalStyle;
  }


  private java.lang.String infoClass;

  /**
   * <p>Return the value of the <code>infoClass</code> property.  Contents:</p><p>
   * CSS style class to apply to any message
   *           with a severity class of "INFO".
   * </p>
   */
  public java.lang.String getInfoClass() {
    if (null != this.infoClass) {
      return this.infoClass;
    }
    ValueBinding _vb = getValueBinding("infoClass");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>infoClass</code> property.</p>
   */
  public void setInfoClass(java.lang.String infoClass) {
    this.infoClass = infoClass;
  }


  private java.lang.String infoStyle;

  /**
   * <p>Return the value of the <code>infoStyle</code> property.  Contents:</p><p>
   * CSS style(s) to apply to any message
   *           with a severity class of "INFO".
   * </p>
   */
  public java.lang.String getInfoStyle() {
    if (null != this.infoStyle) {
      return this.infoStyle;
    }
    ValueBinding _vb = getValueBinding("infoStyle");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>infoStyle</code> property.</p>
   */
  public void setInfoStyle(java.lang.String infoStyle) {
    this.infoStyle = infoStyle;
  }


  private java.lang.String layout = "list";

  /**
   * <p>Return the value of the <code>layout</code> property.  Contents:</p><p>
   * The type of layout markup to use when rendering
   *           error messages.  Valid values are "table" (an HTML
   *           table) and "list" (an HTML list).  If not specified,
   *           the default value is "list".
   * </p>
   */
  public java.lang.String getLayout() {
    if (null != this.layout) {
      return this.layout;
    }
    ValueBinding _vb = getValueBinding("layout");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>layout</code> property.</p>
   */
  public void setLayout(java.lang.String layout) {
    this.layout = layout;
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


  private boolean tooltip = false;
  private boolean tooltip_set = false;

  /**
   * <p>Return the value of the <code>tooltip</code> property.  Contents:</p><p>
   * Flag indicating whether the detail portion of the
   *           message should be displayed as a tooltip.
   * </p>
   */
  public boolean isTooltip() {
    if (this.tooltip_set) {
      return this.tooltip;
    }
    ValueBinding _vb = getValueBinding("tooltip");
    if (_vb != null) {
      Object _result = _vb.getValue(getFacesContext());
      if (_result == null) {
        return false;
      } else {
        return ((Boolean) _result).booleanValue();
      }
    } else {
        return this.tooltip;
    }
  }

  /**
   * <p>Set the value of the <code>tooltip</code> property.</p>
   */
  public void setTooltip(boolean tooltip) {
    this.tooltip = tooltip;
    this.tooltip_set = true;
  }


  private java.lang.String warnClass;

  /**
   * <p>Return the value of the <code>warnClass</code> property.  Contents:</p><p>
   * CSS style class to apply to any message
   *           with a severity class of "WARN".
   * </p>
   */
  public java.lang.String getWarnClass() {
    if (null != this.warnClass) {
      return this.warnClass;
    }
    ValueBinding _vb = getValueBinding("warnClass");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>warnClass</code> property.</p>
   */
  public void setWarnClass(java.lang.String warnClass) {
    this.warnClass = warnClass;
  }


  private java.lang.String warnStyle;

  /**
   * <p>Return the value of the <code>warnStyle</code> property.  Contents:</p><p>
   * CSS style(s) to apply to any message
   *           with a severity class of "WARN".
   * </p>
   */
  public java.lang.String getWarnStyle() {
    if (null != this.warnStyle) {
      return this.warnStyle;
    }
    ValueBinding _vb = getValueBinding("warnStyle");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>warnStyle</code> property.</p>
   */
  public void setWarnStyle(java.lang.String warnStyle) {
    this.warnStyle = warnStyle;
  }


  public Object saveState(FacesContext _context) {
    Object _values[] = new Object[15];
    _values[0] = super.saveState(_context);
    _values[1] = errorClass;
    _values[2] = errorStyle;
    _values[3] = fatalClass;
    _values[4] = fatalStyle;
    _values[5] = infoClass;
    _values[6] = infoStyle;
    _values[7] = layout;
    _values[8] = style;
    _values[9] = styleClass;
    _values[10] = title;
    _values[11] = this.tooltip ? Boolean.TRUE : Boolean.FALSE;
    _values[12] = this.tooltip_set ? Boolean.TRUE : Boolean.FALSE;
    _values[13] = warnClass;
    _values[14] = warnStyle;
    return _values;
  }


  public void restoreState(FacesContext _context, Object _state) {
    Object _values[] = (Object[]) _state;
    super.restoreState(_context, _values[0]);
    this.errorClass = (java.lang.String) _values[1];
    this.errorStyle = (java.lang.String) _values[2];
    this.fatalClass = (java.lang.String) _values[3];
    this.fatalStyle = (java.lang.String) _values[4];
    this.infoClass = (java.lang.String) _values[5];
    this.infoStyle = (java.lang.String) _values[6];
    this.layout = (java.lang.String) _values[7];
    this.style = (java.lang.String) _values[8];
    this.styleClass = (java.lang.String) _values[9];
    this.title = (java.lang.String) _values[10];
    this.tooltip = ((Boolean) _values[11]).booleanValue();
    this.tooltip_set = ((Boolean) _values[12]).booleanValue();
    this.warnClass = (java.lang.String) _values[13];
    this.warnStyle = (java.lang.String) _values[14];
  }


}

