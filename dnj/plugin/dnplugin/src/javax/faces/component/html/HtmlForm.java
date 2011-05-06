/*
 * $Id: HtmlForm.java,v 1.1 2007/01/05 01:23:00 dannyc Exp $
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
 * <p>Represents an HTML <code>form</code> element.  Child input components
 *       will be submitted unless they have been disabled.</p>
 * <p>By default, the <code>rendererType</code> property must be set to "<code>javax.faces.Form</code>" This value can be changed by calling the <code>setRendererType()</code> method.</p>
*/
public class HtmlForm extends javax.faces.component.UIForm {


  public HtmlForm() {
    super();
    setRendererType("javax.faces.Form");
  }


  /*
   * <p>The standard component type for this component.</p>
   */
   public static final String COMPONENT_TYPE = "javax.faces.HtmlForm";


  private java.lang.String accept;

  /**
   * <p>Return the value of the <code>accept</code> property.  Contents:</p><p>
   * List of content types that a server processing this form
   *         will handle correctly
   * </p>
   */
  public java.lang.String getAccept() {
    if (null != this.accept) {
      return this.accept;
    }
    ValueBinding _vb = getValueBinding("accept");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>accept</code> property.</p>
   */
  public void setAccept(java.lang.String accept) {
    this.accept = accept;
  }


  private java.lang.String acceptcharset;

  /**
   * <p>Return the value of the <code>acceptcharset</code> property.  Contents:</p><p>
   * List of character encodings for input data
   *         that are accepted by the server processing
   *         this form.
   * </p>
   */
  public java.lang.String getAcceptcharset() {
    if (null != this.acceptcharset) {
      return this.acceptcharset;
    }
    ValueBinding _vb = getValueBinding("acceptcharset");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>acceptcharset</code> property.</p>
   */
  public void setAcceptcharset(java.lang.String acceptcharset) {
    this.acceptcharset = acceptcharset;
  }


  private java.lang.String dir;

  /**
   * <p>Return the value of the <code>dir</code> property.  Contents:</p><p>
   * Direction indication for text that does not inherit directionality.
   *           Valid values are "LTR" (left-to-right) and "RTL" (right-to-left).
   * </p>
   */
  public java.lang.String getDir() {
    if (null != this.dir) {
      return this.dir;
    }
    ValueBinding _vb = getValueBinding("dir");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>dir</code> property.</p>
   */
  public void setDir(java.lang.String dir) {
    this.dir = dir;
  }


  private java.lang.String enctype = "application/x-www-form-urlencoded";

  /**
   * <p>Return the value of the <code>enctype</code> property.  Contents:</p><p>
   * Content type used to submit the form to the server.  If not
   *         specified, the default value is
   *         "application/x-www-form-urlencoded".
   * </p>
   */
  public java.lang.String getEnctype() {
    if (null != this.enctype) {
      return this.enctype;
    }
    ValueBinding _vb = getValueBinding("enctype");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>enctype</code> property.</p>
   */
  public void setEnctype(java.lang.String enctype) {
    this.enctype = enctype;
  }


  private java.lang.String lang;

  /**
   * <p>Return the value of the <code>lang</code> property.  Contents:</p><p>
   * Code describing the language used in the generated markup
   *           for this component.
   * </p>
   */
  public java.lang.String getLang() {
    if (null != this.lang) {
      return this.lang;
    }
    ValueBinding _vb = getValueBinding("lang");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>lang</code> property.</p>
   */
  public void setLang(java.lang.String lang) {
    this.lang = lang;
  }


  private java.lang.String onclick;

  /**
   * <p>Return the value of the <code>onclick</code> property.  Contents:</p><p>
   * Javascript code executed when a pointer button is
   *           clicked over this element.
   * </p>
   */
  public java.lang.String getOnclick() {
    if (null != this.onclick) {
      return this.onclick;
    }
    ValueBinding _vb = getValueBinding("onclick");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onclick</code> property.</p>
   */
  public void setOnclick(java.lang.String onclick) {
    this.onclick = onclick;
  }


  private java.lang.String ondblclick;

  /**
   * <p>Return the value of the <code>ondblclick</code> property.  Contents:</p><p>
   * Javascript code executed when a pointer button is
   *           double clicked over this element.
   * </p>
   */
  public java.lang.String getOndblclick() {
    if (null != this.ondblclick) {
      return this.ondblclick;
    }
    ValueBinding _vb = getValueBinding("ondblclick");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>ondblclick</code> property.</p>
   */
  public void setOndblclick(java.lang.String ondblclick) {
    this.ondblclick = ondblclick;
  }


  private java.lang.String onkeydown;

  /**
   * <p>Return the value of the <code>onkeydown</code> property.  Contents:</p><p>
   * Javascript code executed when a key is
   *           pressed down over this element.
   * </p>
   */
  public java.lang.String getOnkeydown() {
    if (null != this.onkeydown) {
      return this.onkeydown;
    }
    ValueBinding _vb = getValueBinding("onkeydown");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onkeydown</code> property.</p>
   */
  public void setOnkeydown(java.lang.String onkeydown) {
    this.onkeydown = onkeydown;
  }


  private java.lang.String onkeypress;

  /**
   * <p>Return the value of the <code>onkeypress</code> property.  Contents:</p><p>
   * Javascript code executed when a key is
   *           pressed and released over this element.
   * </p>
   */
  public java.lang.String getOnkeypress() {
    if (null != this.onkeypress) {
      return this.onkeypress;
    }
    ValueBinding _vb = getValueBinding("onkeypress");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onkeypress</code> property.</p>
   */
  public void setOnkeypress(java.lang.String onkeypress) {
    this.onkeypress = onkeypress;
  }


  private java.lang.String onkeyup;

  /**
   * <p>Return the value of the <code>onkeyup</code> property.  Contents:</p><p>
   * Javascript code executed when a key is
   *           released over this element.
   * </p>
   */
  public java.lang.String getOnkeyup() {
    if (null != this.onkeyup) {
      return this.onkeyup;
    }
    ValueBinding _vb = getValueBinding("onkeyup");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onkeyup</code> property.</p>
   */
  public void setOnkeyup(java.lang.String onkeyup) {
    this.onkeyup = onkeyup;
  }


  private java.lang.String onmousedown;

  /**
   * <p>Return the value of the <code>onmousedown</code> property.  Contents:</p><p>
   * Javascript code executed when a pointer button is
   *           pressed down over this element.
   * </p>
   */
  public java.lang.String getOnmousedown() {
    if (null != this.onmousedown) {
      return this.onmousedown;
    }
    ValueBinding _vb = getValueBinding("onmousedown");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onmousedown</code> property.</p>
   */
  public void setOnmousedown(java.lang.String onmousedown) {
    this.onmousedown = onmousedown;
  }


  private java.lang.String onmousemove;

  /**
   * <p>Return the value of the <code>onmousemove</code> property.  Contents:</p><p>
   * Javascript code executed when a pointer button is
   *           moved within this element.
   * </p>
   */
  public java.lang.String getOnmousemove() {
    if (null != this.onmousemove) {
      return this.onmousemove;
    }
    ValueBinding _vb = getValueBinding("onmousemove");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onmousemove</code> property.</p>
   */
  public void setOnmousemove(java.lang.String onmousemove) {
    this.onmousemove = onmousemove;
  }


  private java.lang.String onmouseout;

  /**
   * <p>Return the value of the <code>onmouseout</code> property.  Contents:</p><p>
   * Javascript code executed when a pointer button is
   *           moved away from this element.
   * </p>
   */
  public java.lang.String getOnmouseout() {
    if (null != this.onmouseout) {
      return this.onmouseout;
    }
    ValueBinding _vb = getValueBinding("onmouseout");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onmouseout</code> property.</p>
   */
  public void setOnmouseout(java.lang.String onmouseout) {
    this.onmouseout = onmouseout;
  }


  private java.lang.String onmouseover;

  /**
   * <p>Return the value of the <code>onmouseover</code> property.  Contents:</p><p>
   * Javascript code executed when a pointer button is
   *           moved onto this element.
   * </p>
   */
  public java.lang.String getOnmouseover() {
    if (null != this.onmouseover) {
      return this.onmouseover;
    }
    ValueBinding _vb = getValueBinding("onmouseover");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onmouseover</code> property.</p>
   */
  public void setOnmouseover(java.lang.String onmouseover) {
    this.onmouseover = onmouseover;
  }


  private java.lang.String onmouseup;

  /**
   * <p>Return the value of the <code>onmouseup</code> property.  Contents:</p><p>
   * Javascript code executed when a pointer button is
   *           released over this element.
   * </p>
   */
  public java.lang.String getOnmouseup() {
    if (null != this.onmouseup) {
      return this.onmouseup;
    }
    ValueBinding _vb = getValueBinding("onmouseup");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onmouseup</code> property.</p>
   */
  public void setOnmouseup(java.lang.String onmouseup) {
    this.onmouseup = onmouseup;
  }


  private java.lang.String onreset;

  /**
   * <p>Return the value of the <code>onreset</code> property.  Contents:</p><p>
   * Javascript code executed when this form is reset.
   * </p>
   */
  public java.lang.String getOnreset() {
    if (null != this.onreset) {
      return this.onreset;
    }
    ValueBinding _vb = getValueBinding("onreset");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onreset</code> property.</p>
   */
  public void setOnreset(java.lang.String onreset) {
    this.onreset = onreset;
  }


  private java.lang.String onsubmit;

  /**
   * <p>Return the value of the <code>onsubmit</code> property.  Contents:</p><p>
   * Javascript code executed when this form is submitted.
   * </p>
   */
  public java.lang.String getOnsubmit() {
    if (null != this.onsubmit) {
      return this.onsubmit;
    }
    ValueBinding _vb = getValueBinding("onsubmit");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onsubmit</code> property.</p>
   */
  public void setOnsubmit(java.lang.String onsubmit) {
    this.onsubmit = onsubmit;
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


  private java.lang.String target;

  /**
   * <p>Return the value of the <code>target</code> property.  Contents:</p><p>
   * Name of a frame where the response
   *         retrieved after this form submit is to
   *         be displayed.
   * </p>
   */
  public java.lang.String getTarget() {
    if (null != this.target) {
      return this.target;
    }
    ValueBinding _vb = getValueBinding("target");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>target</code> property.</p>
   */
  public void setTarget(java.lang.String target) {
    this.target = target;
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
    Object _values[] = new Object[22];
    _values[0] = super.saveState(_context);
    _values[1] = accept;
    _values[2] = acceptcharset;
    _values[3] = dir;
    _values[4] = enctype;
    _values[5] = lang;
    _values[6] = onclick;
    _values[7] = ondblclick;
    _values[8] = onkeydown;
    _values[9] = onkeypress;
    _values[10] = onkeyup;
    _values[11] = onmousedown;
    _values[12] = onmousemove;
    _values[13] = onmouseout;
    _values[14] = onmouseover;
    _values[15] = onmouseup;
    _values[16] = onreset;
    _values[17] = onsubmit;
    _values[18] = style;
    _values[19] = styleClass;
    _values[20] = target;
    _values[21] = title;
    return _values;
  }


  public void restoreState(FacesContext _context, Object _state) {
    Object _values[] = (Object[]) _state;
    super.restoreState(_context, _values[0]);
    this.accept = (java.lang.String) _values[1];
    this.acceptcharset = (java.lang.String) _values[2];
    this.dir = (java.lang.String) _values[3];
    this.enctype = (java.lang.String) _values[4];
    this.lang = (java.lang.String) _values[5];
    this.onclick = (java.lang.String) _values[6];
    this.ondblclick = (java.lang.String) _values[7];
    this.onkeydown = (java.lang.String) _values[8];
    this.onkeypress = (java.lang.String) _values[9];
    this.onkeyup = (java.lang.String) _values[10];
    this.onmousedown = (java.lang.String) _values[11];
    this.onmousemove = (java.lang.String) _values[12];
    this.onmouseout = (java.lang.String) _values[13];
    this.onmouseover = (java.lang.String) _values[14];
    this.onmouseup = (java.lang.String) _values[15];
    this.onreset = (java.lang.String) _values[16];
    this.onsubmit = (java.lang.String) _values[17];
    this.style = (java.lang.String) _values[18];
    this.styleClass = (java.lang.String) _values[19];
    this.target = (java.lang.String) _values[20];
    this.title = (java.lang.String) _values[21];
  }


}

