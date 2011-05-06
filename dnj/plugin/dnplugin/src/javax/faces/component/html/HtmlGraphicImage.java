/*
 * $Id: HtmlGraphicImage.java,v 1.1 2007/01/05 01:23:00 dannyc Exp $
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
 * <p>Represents an HTML <code>img</code> element, used to retrieve
 *       and render a graphical image.</p>
 * <p>By default, the <code>rendererType</code> property must be set to "<code>javax.faces.Image</code>" This value can be changed by calling the <code>setRendererType()</code> method.</p>
*/
public class HtmlGraphicImage extends javax.faces.component.UIGraphic {


  public HtmlGraphicImage() {
    super();
    setRendererType("javax.faces.Image");
  }


  /*
   * <p>The standard component type for this component.</p>
   */
   public static final String COMPONENT_TYPE = "javax.faces.HtmlGraphicImage";


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


  private java.lang.String height;

  /**
   * <p>Return the value of the <code>height</code> property.  Contents:</p><p>
   * Override for the height of this image.
   * </p>
   */
  public java.lang.String getHeight() {
    if (null != this.height) {
      return this.height;
    }
    ValueBinding _vb = getValueBinding("height");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>height</code> property.</p>
   */
  public void setHeight(java.lang.String height) {
    this.height = height;
  }


  private boolean ismap = false;
  private boolean ismap_set = false;

  /**
   * <p>Return the value of the <code>ismap</code> property.  Contents:</p><p>
   * Flag indicating that this image is to be
   *         used as a server side image map.  Such an image
   *         must be enclosed within a hyperlink ("a").
   * </p>
   */
  public boolean isIsmap() {
    if (this.ismap_set) {
      return this.ismap;
    }
    ValueBinding _vb = getValueBinding("ismap");
    if (_vb != null) {
      Object _result = _vb.getValue(getFacesContext());
      if (_result == null) {
        return false;
      } else {
        return ((Boolean) _result).booleanValue();
      }
    } else {
        return this.ismap;
    }
  }

  /**
   * <p>Set the value of the <code>ismap</code> property.</p>
   */
  public void setIsmap(boolean ismap) {
    this.ismap = ismap;
    this.ismap_set = true;
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


  private java.lang.String longdesc;

  /**
   * <p>Return the value of the <code>longdesc</code> property.  Contents:</p><p>
   * URI to a long description of the image
   *         represented by this element.
   * </p>
   */
  public java.lang.String getLongdesc() {
    if (null != this.longdesc) {
      return this.longdesc;
    }
    ValueBinding _vb = getValueBinding("longdesc");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>longdesc</code> property.</p>
   */
  public void setLongdesc(java.lang.String longdesc) {
    this.longdesc = longdesc;
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


  private java.lang.String usemap;

  /**
   * <p>Return the value of the <code>usemap</code> property.  Contents:</p><p>
   * The name of a client side image map (an HTML "map"
   *         element) for which this element provides the image.
   * </p>
   */
  public java.lang.String getUsemap() {
    if (null != this.usemap) {
      return this.usemap;
    }
    ValueBinding _vb = getValueBinding("usemap");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>usemap</code> property.</p>
   */
  public void setUsemap(java.lang.String usemap) {
    this.usemap = usemap;
  }


  private java.lang.String width;

  /**
   * <p>Return the value of the <code>width</code> property.  Contents:</p><p>
   * Override for the width of this image.
   * </p>
   */
  public java.lang.String getWidth() {
    if (null != this.width) {
      return this.width;
    }
    ValueBinding _vb = getValueBinding("width");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>width</code> property.</p>
   */
  public void setWidth(java.lang.String width) {
    this.width = width;
  }


  public Object saveState(FacesContext _context) {
    Object _values[] = new Object[23];
    _values[0] = super.saveState(_context);
    _values[1] = alt;
    _values[2] = dir;
    _values[3] = height;
    _values[4] = this.ismap ? Boolean.TRUE : Boolean.FALSE;
    _values[5] = this.ismap_set ? Boolean.TRUE : Boolean.FALSE;
    _values[6] = lang;
    _values[7] = longdesc;
    _values[8] = onclick;
    _values[9] = ondblclick;
    _values[10] = onkeydown;
    _values[11] = onkeypress;
    _values[12] = onkeyup;
    _values[13] = onmousedown;
    _values[14] = onmousemove;
    _values[15] = onmouseout;
    _values[16] = onmouseover;
    _values[17] = onmouseup;
    _values[18] = style;
    _values[19] = styleClass;
    _values[20] = title;
    _values[21] = usemap;
    _values[22] = width;
    return _values;
  }


  public void restoreState(FacesContext _context, Object _state) {
    Object _values[] = (Object[]) _state;
    super.restoreState(_context, _values[0]);
    this.alt = (java.lang.String) _values[1];
    this.dir = (java.lang.String) _values[2];
    this.height = (java.lang.String) _values[3];
    this.ismap = ((Boolean) _values[4]).booleanValue();
    this.ismap_set = ((Boolean) _values[5]).booleanValue();
    this.lang = (java.lang.String) _values[6];
    this.longdesc = (java.lang.String) _values[7];
    this.onclick = (java.lang.String) _values[8];
    this.ondblclick = (java.lang.String) _values[9];
    this.onkeydown = (java.lang.String) _values[10];
    this.onkeypress = (java.lang.String) _values[11];
    this.onkeyup = (java.lang.String) _values[12];
    this.onmousedown = (java.lang.String) _values[13];
    this.onmousemove = (java.lang.String) _values[14];
    this.onmouseout = (java.lang.String) _values[15];
    this.onmouseover = (java.lang.String) _values[16];
    this.onmouseup = (java.lang.String) _values[17];
    this.style = (java.lang.String) _values[18];
    this.styleClass = (java.lang.String) _values[19];
    this.title = (java.lang.String) _values[20];
    this.usemap = (java.lang.String) _values[21];
    this.width = (java.lang.String) _values[22];
  }


}