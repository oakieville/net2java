/*
 * $Id: HtmlOutputLink.java,v 1.1 2007/01/05 01:23:02 dannyc Exp $
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
 * <p>Represents an HTML <code>a</code> (hyperlink) element that may be
 *       used to link to an arbitrary URL defined by the <code>value</code>
 *       property.</p>
 * <p>By default, the <code>rendererType</code> property must be set to "<code>javax.faces.Link</code>" This value can be changed by calling the <code>setRendererType()</code> method.</p>
*/
public class HtmlOutputLink extends javax.faces.component.UIOutput {


  public HtmlOutputLink() {
    super();
    setRendererType("javax.faces.Link");
  }


  /*
   * <p>The standard component type for this component.</p>
   */
   public static final String COMPONENT_TYPE = "javax.faces.HtmlOutputLink";


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


  private java.lang.String charset;

  /**
   * <p>Return the value of the <code>charset</code> property.  Contents:</p><p>
   * The character encoding of the resource designated
   *           by this hyperlink.
   * </p>
   */
  public java.lang.String getCharset() {
    if (null != this.charset) {
      return this.charset;
    }
    ValueBinding _vb = getValueBinding("charset");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>charset</code> property.</p>
   */
  public void setCharset(java.lang.String charset) {
    this.charset = charset;
  }


  private java.lang.String coords;

  /**
   * <p>Return the value of the <code>coords</code> property.  Contents:</p><p>
   * The position and shape of the hot spot on the screen
   *           (for use in client-side image maps).
   * </p>
   */
  public java.lang.String getCoords() {
    if (null != this.coords) {
      return this.coords;
    }
    ValueBinding _vb = getValueBinding("coords");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>coords</code> property.</p>
   */
  public void setCoords(java.lang.String coords) {
    this.coords = coords;
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


  private java.lang.String hreflang;

  /**
   * <p>Return the value of the <code>hreflang</code> property.  Contents:</p><p>
   * The language code of the resource designated
   *           by this hyperlink.
   * </p>
   */
  public java.lang.String getHreflang() {
    if (null != this.hreflang) {
      return this.hreflang;
    }
    ValueBinding _vb = getValueBinding("hreflang");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>hreflang</code> property.</p>
   */
  public void setHreflang(java.lang.String hreflang) {
    this.hreflang = hreflang;
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


  private java.lang.String rel;

  /**
   * <p>Return the value of the <code>rel</code> property.  Contents:</p><p>
   * The relationship from the current document
   *           to the anchor specified by this hyperlink.
   *           The value of this attribute is a space-separated       list of link types.
   * </p>
   */
  public java.lang.String getRel() {
    if (null != this.rel) {
      return this.rel;
    }
    ValueBinding _vb = getValueBinding("rel");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>rel</code> property.</p>
   */
  public void setRel(java.lang.String rel) {
    this.rel = rel;
  }


  private java.lang.String rev;

  /**
   * <p>Return the value of the <code>rev</code> property.  Contents:</p><p>
   * A reverse link from the anchor specified
   *           by this hyperlink to the current document.
   *           The value of this attribute is a space-separated
   *           list of link types.
   * </p>
   */
  public java.lang.String getRev() {
    if (null != this.rev) {
      return this.rev;
    }
    ValueBinding _vb = getValueBinding("rev");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>rev</code> property.</p>
   */
  public void setRev(java.lang.String rev) {
    this.rev = rev;
  }


  private java.lang.String shape;

  /**
   * <p>Return the value of the <code>shape</code> property.  Contents:</p><p>
   * The shape of the hot spot on the screen
   *           (for use in client-side image maps).  Valid
   *           values are:  default (entire region); rect
   *           (rectangular region); circle (circular region);
   *           and poly (polygonal region).
   * </p>
   */
  public java.lang.String getShape() {
    if (null != this.shape) {
      return this.shape;
    }
    ValueBinding _vb = getValueBinding("shape");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>shape</code> property.</p>
   */
  public void setShape(java.lang.String shape) {
    this.shape = shape;
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


  private java.lang.String target;

  /**
   * <p>Return the value of the <code>target</code> property.  Contents:</p><p>
   * Name of a frame where the resource
   *           retrieved via this hyperlink is to
   *           be displayed.
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


  private java.lang.String type;

  /**
   * <p>Return the value of the <code>type</code> property.  Contents:</p><p>
   * The content type of the resource designated
   *           by this hyperlink.
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
    Object _values[] = new Object[28];
    _values[0] = super.saveState(_context);
    _values[1] = accesskey;
    _values[2] = charset;
    _values[3] = coords;
    _values[4] = dir;
    _values[5] = hreflang;
    _values[6] = lang;
    _values[7] = onblur;
    _values[8] = onclick;
    _values[9] = ondblclick;
    _values[10] = onfocus;
    _values[11] = onkeydown;
    _values[12] = onkeypress;
    _values[13] = onkeyup;
    _values[14] = onmousedown;
    _values[15] = onmousemove;
    _values[16] = onmouseout;
    _values[17] = onmouseover;
    _values[18] = onmouseup;
    _values[19] = rel;
    _values[20] = rev;
    _values[21] = shape;
    _values[22] = style;
    _values[23] = styleClass;
    _values[24] = tabindex;
    _values[25] = target;
    _values[26] = title;
    _values[27] = type;
    return _values;
  }


  public void restoreState(FacesContext _context, Object _state) {
    Object _values[] = (Object[]) _state;
    super.restoreState(_context, _values[0]);
    this.accesskey = (java.lang.String) _values[1];
    this.charset = (java.lang.String) _values[2];
    this.coords = (java.lang.String) _values[3];
    this.dir = (java.lang.String) _values[4];
    this.hreflang = (java.lang.String) _values[5];
    this.lang = (java.lang.String) _values[6];
    this.onblur = (java.lang.String) _values[7];
    this.onclick = (java.lang.String) _values[8];
    this.ondblclick = (java.lang.String) _values[9];
    this.onfocus = (java.lang.String) _values[10];
    this.onkeydown = (java.lang.String) _values[11];
    this.onkeypress = (java.lang.String) _values[12];
    this.onkeyup = (java.lang.String) _values[13];
    this.onmousedown = (java.lang.String) _values[14];
    this.onmousemove = (java.lang.String) _values[15];
    this.onmouseout = (java.lang.String) _values[16];
    this.onmouseover = (java.lang.String) _values[17];
    this.onmouseup = (java.lang.String) _values[18];
    this.rel = (java.lang.String) _values[19];
    this.rev = (java.lang.String) _values[20];
    this.shape = (java.lang.String) _values[21];
    this.style = (java.lang.String) _values[22];
    this.styleClass = (java.lang.String) _values[23];
    this.tabindex = (java.lang.String) _values[24];
    this.target = (java.lang.String) _values[25];
    this.title = (java.lang.String) _values[26];
    this.type = (java.lang.String) _values[27];
  }


}

