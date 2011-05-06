/*
 * $Id: HtmlPanelGrid.java,v 1.1 2007/01/05 01:23:02 dannyc Exp $
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
 * <p>Renders child components in a table, starting a new
 *       row after the specified number of columns.</p>
 * <p>By default, the <code>rendererType</code> property must be set to "<code>javax.faces.Grid</code>" This value can be changed by calling the <code>setRendererType()</code> method.</p>
*/
public class HtmlPanelGrid extends javax.faces.component.UIPanel {


  public HtmlPanelGrid() {
    super();
    setRendererType("javax.faces.Grid");
  }


  /*
   * <p>The standard component type for this component.</p>
   */
   public static final String COMPONENT_TYPE = "javax.faces.HtmlPanelGrid";


  private java.lang.String bgcolor;

  /**
   * <p>Return the value of the <code>bgcolor</code> property.  Contents:</p><p>
   * Name or code of the background color for this table.
   * </p>
   */
  public java.lang.String getBgcolor() {
    if (null != this.bgcolor) {
      return this.bgcolor;
    }
    ValueBinding _vb = getValueBinding("bgcolor");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>bgcolor</code> property.</p>
   */
  public void setBgcolor(java.lang.String bgcolor) {
    this.bgcolor = bgcolor;
  }


  private int border = Integer.MIN_VALUE;
  private boolean border_set = false;

  /**
   * <p>Return the value of the <code>border</code> property.  Contents:</p><p>
   * Width (in pixels) of the border to be drawn
   *           around this table.
   * </p>
   */
  public int getBorder() {
    if (this.border_set) {
      return this.border;
    }
    ValueBinding _vb = getValueBinding("border");
    if (_vb != null) {
      Object _result = _vb.getValue(getFacesContext());
      if (_result == null) {
        return Integer.MIN_VALUE;
      } else {
        return ((Integer) _result).intValue();
      }
    } else {
        return this.border;
    }
  }

  /**
   * <p>Set the value of the <code>border</code> property.</p>
   */
  public void setBorder(int border) {
    this.border = border;
    this.border_set = true;
  }


  private java.lang.String cellpadding;

  /**
   * <p>Return the value of the <code>cellpadding</code> property.  Contents:</p><p>
   * Definition of how much space the user agent should
   *           leave between the border of each cell and its contents.
   * </p>
   */
  public java.lang.String getCellpadding() {
    if (null != this.cellpadding) {
      return this.cellpadding;
    }
    ValueBinding _vb = getValueBinding("cellpadding");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>cellpadding</code> property.</p>
   */
  public void setCellpadding(java.lang.String cellpadding) {
    this.cellpadding = cellpadding;
  }


  private java.lang.String cellspacing;

  /**
   * <p>Return the value of the <code>cellspacing</code> property.  Contents:</p><p>
   * Definition of how much space the user agent should
   *           leave between the left side of the table and the
   *           leftmost column, the top of the table and the top of
   *           the top side of the topmost row, and so on for the
   *           right and bottom of the table.  It also specifies
   *           the amount of space to leave between cells.
   * </p>
   */
  public java.lang.String getCellspacing() {
    if (null != this.cellspacing) {
      return this.cellspacing;
    }
    ValueBinding _vb = getValueBinding("cellspacing");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>cellspacing</code> property.</p>
   */
  public void setCellspacing(java.lang.String cellspacing) {
    this.cellspacing = cellspacing;
  }


  private java.lang.String columnClasses;

  /**
   * <p>Return the value of the <code>columnClasses</code> property.  Contents:</p><p>
   * Comma-delimited list of CSS style classes that will be applied
   *           to the columns of this table.  A space separated list of
   *           classes may also be specified for any individual column.  If
   *           the number of elements in this list is less than the number of
   *           columns specified in the "columns" attribute, no "class"
   *           attribute is output for each column greater than the number of
   *           elements in the list.  If the number of elements in the list
   *           is greater than the number of columns specified in the
   *           "columns" attribute, the elements at the posisiton in the list
   *           after the value of the "columns" attribute are ignored.
   * </p>
   */
  public java.lang.String getColumnClasses() {
    if (null != this.columnClasses) {
      return this.columnClasses;
    }
    ValueBinding _vb = getValueBinding("columnClasses");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>columnClasses</code> property.</p>
   */
  public void setColumnClasses(java.lang.String columnClasses) {
    this.columnClasses = columnClasses;
  }


  private int columns = Integer.MIN_VALUE;
  private boolean columns_set = false;

  /**
   * <p>Return the value of the <code>columns</code> property.  Contents:</p><p>
   * The number of columns to render before
   *         starting a new row.
   * </p>
   */
  public int getColumns() {
    if (this.columns_set) {
      return this.columns;
    }
    ValueBinding _vb = getValueBinding("columns");
    if (_vb != null) {
      Object _result = _vb.getValue(getFacesContext());
      if (_result == null) {
        return Integer.MIN_VALUE;
      } else {
        return ((Integer) _result).intValue();
      }
    } else {
        return this.columns;
    }
  }

  /**
   * <p>Set the value of the <code>columns</code> property.</p>
   */
  public void setColumns(int columns) {
    this.columns = columns;
    this.columns_set = true;
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


  private java.lang.String footerClass;

  /**
   * <p>Return the value of the <code>footerClass</code> property.  Contents:</p><p>
   * Space-separated list of CSS style class(es) that will be
   *           applied to any footer generated for this table.
   * </p>
   */
  public java.lang.String getFooterClass() {
    if (null != this.footerClass) {
      return this.footerClass;
    }
    ValueBinding _vb = getValueBinding("footerClass");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>footerClass</code> property.</p>
   */
  public void setFooterClass(java.lang.String footerClass) {
    this.footerClass = footerClass;
  }


  private java.lang.String frame;

  /**
   * <p>Return the value of the <code>frame</code> property.  Contents:</p><p>
   * Code specifying which sides of the frame surrounding
   *           this table will be visible.  Valid values are:
   *           none (no sides, default value); above (top side only);
   *           below (bottom side only); hsides (top and bottom sides
   *           only); vsides (right and left sides only); lhs (left
   *           hand side only); rhs (right hand side only); box
   *           (all four sides); and border (all four sides).
   * </p>
   */
  public java.lang.String getFrame() {
    if (null != this.frame) {
      return this.frame;
    }
    ValueBinding _vb = getValueBinding("frame");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>frame</code> property.</p>
   */
  public void setFrame(java.lang.String frame) {
    this.frame = frame;
  }


  private java.lang.String headerClass;

  /**
   * <p>Return the value of the <code>headerClass</code> property.  Contents:</p><p>
   * Space-separated list of CSS style class(es) that will be
   *           applied to any header generated for this table.
   * </p>
   */
  public java.lang.String getHeaderClass() {
    if (null != this.headerClass) {
      return this.headerClass;
    }
    ValueBinding _vb = getValueBinding("headerClass");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>headerClass</code> property.</p>
   */
  public void setHeaderClass(java.lang.String headerClass) {
    this.headerClass = headerClass;
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


  private java.lang.String rowClasses;

  /**
   * <p>Return the value of the <code>rowClasses</code> property.  Contents:</p><p>
   * Comma-delimited list of CSS style classes that will be applied
   *           to the rows of this table.  A space separated list of classes
   *           may also be specified for any individual row.  Thes styles are
   *           applied, in turn, to each row in the table.  For example, if
   *           the list has two elements, the first style class in the list
   *           is applied to the first row, the second to the second row, the
   *           first to the third row, the second to the fourth row, etc.  In
   *           other words, we keep iterating through the list until we reach
   *           the end, and then we start at the beginning again.
   * </p>
   */
  public java.lang.String getRowClasses() {
    if (null != this.rowClasses) {
      return this.rowClasses;
    }
    ValueBinding _vb = getValueBinding("rowClasses");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>rowClasses</code> property.</p>
   */
  public void setRowClasses(java.lang.String rowClasses) {
    this.rowClasses = rowClasses;
  }


  private java.lang.String rules;

  /**
   * <p>Return the value of the <code>rules</code> property.  Contents:</p><p>
   * Code specifying which rules will appear between cells
   *           within this table.  Valid values are:  none (no rules,
   *           default value); groups (between row groups); rows
   *           (between rows only); cols (between columns only); and
   *           all (between all rows and columns).
   * </p>
   */
  public java.lang.String getRules() {
    if (null != this.rules) {
      return this.rules;
    }
    ValueBinding _vb = getValueBinding("rules");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>rules</code> property.</p>
   */
  public void setRules(java.lang.String rules) {
    this.rules = rules;
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


  private java.lang.String summary;

  /**
   * <p>Return the value of the <code>summary</code> property.  Contents:</p><p>
   * Summary of this table's purpose and structure, for
   *           user agents rendering to non-visual media such as
   *           speech and Braille.
   * </p>
   */
  public java.lang.String getSummary() {
    if (null != this.summary) {
      return this.summary;
    }
    ValueBinding _vb = getValueBinding("summary");
    if (_vb != null) {
      return (java.lang.String) _vb.getValue(getFacesContext());
    } else {
      return null;
    }
  }

  /**
   * <p>Set the value of the <code>summary</code> property.</p>
   */
  public void setSummary(java.lang.String summary) {
    this.summary = summary;
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


  private java.lang.String width;

  /**
   * <p>Return the value of the <code>width</code> property.  Contents:</p><p>
   * Width of the entire table, for visual user agents.
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
    Object _values[] = new Object[31];
    _values[0] = super.saveState(_context);
    _values[1] = bgcolor;
    _values[2] = new Integer(this.border);
    _values[3] = this.border_set ? Boolean.TRUE : Boolean.FALSE;
    _values[4] = cellpadding;
    _values[5] = cellspacing;
    _values[6] = columnClasses;
    _values[7] = new Integer(this.columns);
    _values[8] = this.columns_set ? Boolean.TRUE : Boolean.FALSE;
    _values[9] = dir;
    _values[10] = footerClass;
    _values[11] = frame;
    _values[12] = headerClass;
    _values[13] = lang;
    _values[14] = onclick;
    _values[15] = ondblclick;
    _values[16] = onkeydown;
    _values[17] = onkeypress;
    _values[18] = onkeyup;
    _values[19] = onmousedown;
    _values[20] = onmousemove;
    _values[21] = onmouseout;
    _values[22] = onmouseover;
    _values[23] = onmouseup;
    _values[24] = rowClasses;
    _values[25] = rules;
    _values[26] = style;
    _values[27] = styleClass;
    _values[28] = summary;
    _values[29] = title;
    _values[30] = width;
    return _values;
  }


  public void restoreState(FacesContext _context, Object _state) {
    Object _values[] = (Object[]) _state;
    super.restoreState(_context, _values[0]);
    this.bgcolor = (java.lang.String) _values[1];
    this.border = ((Integer) _values[2]).intValue();
    this.border_set = ((Boolean) _values[3]).booleanValue();
    this.cellpadding = (java.lang.String) _values[4];
    this.cellspacing = (java.lang.String) _values[5];
    this.columnClasses = (java.lang.String) _values[6];
    this.columns = ((Integer) _values[7]).intValue();
    this.columns_set = ((Boolean) _values[8]).booleanValue();
    this.dir = (java.lang.String) _values[9];
    this.footerClass = (java.lang.String) _values[10];
    this.frame = (java.lang.String) _values[11];
    this.headerClass = (java.lang.String) _values[12];
    this.lang = (java.lang.String) _values[13];
    this.onclick = (java.lang.String) _values[14];
    this.ondblclick = (java.lang.String) _values[15];
    this.onkeydown = (java.lang.String) _values[16];
    this.onkeypress = (java.lang.String) _values[17];
    this.onkeyup = (java.lang.String) _values[18];
    this.onmousedown = (java.lang.String) _values[19];
    this.onmousemove = (java.lang.String) _values[20];
    this.onmouseout = (java.lang.String) _values[21];
    this.onmouseover = (java.lang.String) _values[22];
    this.onmouseup = (java.lang.String) _values[23];
    this.rowClasses = (java.lang.String) _values[24];
    this.rules = (java.lang.String) _values[25];
    this.style = (java.lang.String) _values[26];
    this.styleClass = (java.lang.String) _values[27];
    this.summary = (java.lang.String) _values[28];
    this.title = (java.lang.String) _values[29];
    this.width = (java.lang.String) _values[30];
  }


}

