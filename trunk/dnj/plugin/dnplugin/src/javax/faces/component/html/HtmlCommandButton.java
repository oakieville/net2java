/*
 * $Id: HtmlCommandButton.java,v 1.1 2007/01/05 01:22:59 dannyc Exp $
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
 *       for a button of type <code>submit</code> or <code>reset</code>.
 *       The label text is specified by the component value.</p>
 * <p>By default, the <code>rendererType</code> property must be set to "<code>javax.faces.Button</code>" This value can be changed by calling the <code>setRendererType()</code> method.</p>
*/
public class HtmlCommandButton extends javax.faces.component.UICommand {


  public HtmlCommandButton() {
    super();
    setRendererType("javax.faces.Button");
  }


  /*
   * <p>The standard component type for this component.</p>
   */
   public static final String COMPONENT_TYPE = "javax.faces.HtmlCommandButton";


  private java.lang.String accesskey;

  /**
   * <p>Return the value of the <code>accesskey</code> property.  Contents:</p><p>
   * Access key that, when pressed, transfers focus
   *           to this element.
   * </p>
   */
  public java.lang.String getAccesskey() {
    if (null != this.accesskey) {
      return this.accesskey;
    }
    ValueBinding _vb = getValueBinding("accesskey");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>accesskey</code> property.</p>
   */
  public void setAccesskey(java.lang.String accesskey) {
    this.accesskey = accesskey;
  }


  private java.lang.String alt;

  /**
   * <p>Return the value of the <code>alt</code> property.  Contents:</p><p>
   * Alternate textual description of the
   *         element rendered by this component.
   * </p>
   */
  public java.lang.String getAlt() {
    if (null != this.alt) {
      return this.alt;
    }
    ValueBinding _vb = getValueBinding("alt");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>alt</code> property.</p>
   */
  public void setAlt(java.lang.String alt) {
    this.alt = alt;
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


  private boolean disabled = false;
  private boolean disabled_set = false;

  /**
   * <p>Return the value of the <code>disabled</code> property.  Contents:</p><p>
   * Flag indicating that this element must never
   *           receive focus or be included in a subsequent
   *           submit.
   * </p>
   */
  public boolean isDisabled() {
    if (this.disabled_set) {
      return this.disabled;
    }
    ValueBinding _vb = getValueBinding("disabled");
    if (_vb != null) {
      Object _result = _vb.getValue(getFacesContext());
      if (_result == null) {
        return false;
      } else {
        return ((Boolean) _result).booleanValue();
      }
    } else {
        return this.disabled;
    }
  }

  /**
   * <p>Set the value of the <code>disabled</code> property.</p>
   */
  public void setDisabled(boolean disabled) {
    this.disabled = disabled;
    this.disabled_set = true;
  }


  private java.lang.String image;

  /**
   * <p>Return the value of the <code>image</code> property.  Contents:</p><p>
   * Absolute or relative URL of the image
   *         to be displayed for this button.  If
   *         specified, this "input" element will be
   *         of type "image".  Otherwise, it will be
   *         of the type specified by the "type"
   *         property with a label specified by the
   *         "value" property.
   * </p>
   */
  public java.lang.String getImage() {
    if (null != this.image) {
      return this.image;
    }
    ValueBinding _vb = getValueBinding("image");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>image</code> property.</p>
   */
  public void setImage(java.lang.String image) {
    this.image = image;
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


  private java.lang.String onblur;

  /**
   * <p>Return the value of the <code>onblur</code> property.  Contents:</p><p>
   * Javascript code executed when this element loses focus.
   * </p>
   */
  public java.lang.String getOnblur() {
    if (null != this.onblur) {
      return this.onblur;
    }
    ValueBinding _vb = getValueBinding("onblur");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onblur</code> property.</p>
   */
  public void setOnblur(java.lang.String onblur) {
    this.onblur = onblur;
  }


  private java.lang.String onchange;

  /**
   * <p>Return the value of the <code>onchange</code> property.  Contents:</p><p>
   * Javascript code executed when this element loses focus
   *           and its value has been modified since gaining focus.
   * </p>
   */
  public java.lang.String getOnchange() {
    if (null != this.onchange) {
      return this.onchange;
    }
    ValueBinding _vb = getValueBinding("onchange");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onchange</code> property.</p>
   */
  public void setOnchange(java.lang.String onchange) {
    this.onchange = onchange;
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


  private java.lang.String onfocus;

  /**
   * <p>Return the value of the <code>onfocus</code> property.  Contents:</p><p>
   * Javascript code executed when this element receives focus.
   * </p>
   */
  public java.lang.String getOnfocus() {
    if (null != this.onfocus) {
      return this.onfocus;
    }
    ValueBinding _vb = getValueBinding("onfocus");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onfocus</code> property.</p>
   */
  public void setOnfocus(java.lang.String onfocus) {
    this.onfocus = onfocus;
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


  private java.lang.String onselect;

  /**
   * <p>Return the value of the <code>onselect</code> property.  Contents:</p><p>
   * Javascript code executed when text within this
   *           element is selected by the user.
   * </p>
   */
  public java.lang.String getOnselect() {
    if (null != this.onselect) {
      return this.onselect;
    }
    ValueBinding _vb = getValueBinding("onselect");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>onselect</code> property.</p>
   */
  public void setOnselect(java.lang.String onselect) {
    this.onselect = onselect;
  }


  private boolean readonly = false;
  private boolean readonly_set = false;

  /**
   * <p>Return the value of the <code>readonly</code> property.  Contents:</p><p>
   * Flag indicating that this component will prohibit
   *           changes by the user.  The element may receive focus
   *           unless it has also been disabled.
   * </p>
   */
  public boolean isReadonly() {
    if (this.readonly_set) {
      return this.readonly;
    }
    ValueBinding _vb = getValueBinding("readonly");
    if (_vb != null) {
      Object _result = _vb.getValue(getFacesContext());
      if (_result == null) {
        return false;
      } else {
        return ((Boolean) _result).booleanValue();
      }
    } else {
        return this.readonly;
    }
  }

  /**
   * <p>Set the value of the <code>readonly</code> property.</p>
   */
  public void setReadonly(boolean readonly) {
    this.readonly = readonly;
    this.readonly_set = true;
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


  private java.lang.String tabindex;

  /**
   * <p>Return the value of the <code>tabindex</code> property.  Contents:</p><p>
   * Position of this element in the tabbing order
   *           for the current document.  This value must be
   *           an integer between 0 and 32767.
   * </p>
   */
  public java.lang.String getTabindex() {
    if (null != this.tabindex) {
      return this.tabindex;
    }
    ValueBinding _vb = getValueBinding("tabindex");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>tabindex</code> property.</p>
   */
  public void setTabindex(java.lang.String tabindex) {
    this.tabindex = tabindex;
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


  private java.lang.String type = "submit";

  /**
   * <p>Return the value of the <code>type</code> property.  Contents:</p><p>
   * Type of button to create.  Valid values are "submit" and
   *          "reset".  If not specified, or not a valid value, the default
   *          value is "submit".
   * </p>
   */
  public java.lang.String getType() {
    if (null != this.type) {
      return this.type;
    }
    ValueBinding _vb = getValueBinding("type");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>type</code> property.</p>
   */
  public void setType(java.lang.String type) {
    this.type = type;
  }


  public Object saveState(FacesContext _context) {
    Object _values[] = new Object[29];
    _values[0] = super.saveState(_context);
    _values[1] = accesskey;
    _values[2] = alt;
    _values[3] = dir;
    _values[4] = this.disabled ? Boolean.TRUE : Boolean.FALSE;
    _values[5] = this.disabled_set ? Boolean.TRUE : Boolean.FALSE;
    _values[6] = image;
    _values[7] = lang;
    _values[8] = onblur;
    _values[9] = onchange;
    _values[10] = onclick;
    _values[11] = ondblclick;
    _values[12] = onfocus;
    _values[13] = onkeydown;
    _values[14] = onkeypress;
    _values[15] = onkeyup;
    _values[16] = onmousedown;
    _values[17] = onmousemove;
    _values[18] = onmouseout;
    _values[19] = onmouseover;
    _values[20] = onmouseup;
    _values[21] = onselect;
    _values[22] = this.readonly ? Boolean.TRUE : Boolean.FALSE;
    _values[23] = this.readonly_set ? Boolean.TRUE : Boolean.FALSE;
    _values[24] = style;
    _values[25] = styleClass;
    _values[26] = tabindex;
    _values[27] = title;
    _values[28] = type;
    return _values;
  }


  public void restoreState(FacesContext _context, Object _state) {
    Object _values[] = (Object[]) _state;
    super.restoreState(_context, _values[0]);
    this.accesskey = (java.lang.String) _values[1];
    this.alt = (java.lang.String) _values[2];
    this.dir = (java.lang.String) _values[3];
    this.disabled = ((Boolean) _values[4]).booleanValue();
    this.disabled_set = ((Boolean) _values[5]).booleanValue();
    this.image = (java.lang.String) _values[6];
    this.lang = (java.lang.String) _values[7];
    this.onblur = (java.lang.String) _values[8];
    this.onchange = (java.lang.String) _values[9];
    this.onclick = (java.lang.String) _values[10];
    this.ondblclick = (java.lang.String) _values[11];
    this.onfocus = (java.lang.String) _values[12];
    this.onkeydown = (java.lang.String) _values[13];
    this.onkeypress = (java.lang.String) _values[14];
    this.onkeyup = (java.lang.String) _values[15];
    this.onmousedown = (java.lang.String) _values[16];
    this.onmousemove = (java.lang.String) _values[17];
    this.onmouseout = (java.lang.String) _values[18];
    this.onmouseover = (java.lang.String) _values[19];
    this.onmouseup = (java.lang.String) _values[20];
    this.onselect = (java.lang.String) _values[21];
    this.readonly = ((Boolean) _values[22]).booleanValue();
    this.readonly_set = ((Boolean) _values[23]).booleanValue();
    this.style = (java.lang.String) _values[24];
    this.styleClass = (java.lang.String) _values[25];
    this.tabindex = (java.lang.String) _values[26];
    this.title = (java.lang.String) _values[27];
    this.type = (java.lang.String) _values[28];
  }


}

