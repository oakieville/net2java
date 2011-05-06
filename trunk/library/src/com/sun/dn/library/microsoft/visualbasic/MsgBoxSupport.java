/**
* Copyright 2006 Sun Microsystems, Inc. All rights reserved.
* Use is subject to license terms.
*/


package com.sun.dn.library.Microsoft.VisualBasic;

/**
 *
 * @author localuser
 */

import javax.swing.*;

public class MsgBoxSupport {
    
  public static int showDialog(Object message, int style) {
      return showDialog(message, style, "Application Dialog");
  }
  
  public static int showDialog(Object message, int style, String title) {
      if (style == MsgBoxStyleSupport.AbortRetryIgnore) {
             int res = JOptionPane.showOptionDialog(null,
                                   message,
                                   title,
                                   JOptionPane.YES_NO_CANCEL_OPTION,
                                   JOptionPane.QUESTION_MESSAGE,
                                   null,
                                   null,
                                   null);
             if (res == JOptionPane.YES_OPTION) {
                 return MsgBoxResultSupport.Retry;
             } else if (res == JOptionPane.NO_OPTION) {
                 return MsgBoxResultSupport.Abort;
             } else  {
                 return MsgBoxResultSupport.Cancel;
             }
                            
      } else if (style == MsgBoxStyleSupport.ApplicationModal) {
          JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
          return MsgBoxResultSupport.OK;
          
      } else if (style == MsgBoxStyleSupport.Critical) {
          JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
          return MsgBoxResultSupport.OK;
      }  else if (style == MsgBoxStyleSupport.DefaultButton1) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            return MsgBoxResultSupport.OK;
      }  else if (style == MsgBoxStyleSupport.DefaultButton2) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            return MsgBoxResultSupport.OK;
      }  else if (style == MsgBoxStyleSupport.DefaultButton3) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            return MsgBoxResultSupport.OK;
      } else if (style == MsgBoxStyleSupport.Exclamation) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
            return MsgBoxResultSupport.OK;
      } else if (style == MsgBoxStyleSupport.Information) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
            return MsgBoxResultSupport.OK;
      } else if (style == MsgBoxStyleSupport.MsgBoxHelp) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
            return MsgBoxResultSupport.OK;
      } else if (style == MsgBoxStyleSupport.MsgBoxRight) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            return MsgBoxResultSupport.OK;
      } else if (style == MsgBoxStyleSupport.MsgBoxRtlReading) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            return MsgBoxResultSupport.OK;
      } else if (style == MsgBoxStyleSupport.MsgBoxSetForeground) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            return MsgBoxResultSupport.OK;
      } else if (style == MsgBoxStyleSupport.OKCancel) {
           int res = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION);
           if (res == JOptionPane.OK_OPTION) {
               return MsgBoxResultSupport.OK;
           } else {
               return MsgBoxResultSupport.Cancel;
           }
      } else if (style == MsgBoxStyleSupport.OKOnly) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.INFORMATION_MESSAGE);
            return MsgBoxResultSupport.OK;
      } else if (style == MsgBoxStyleSupport.Question) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.WARNING_MESSAGE);
            return MsgBoxResultSupport.OK;
      } else if (style == MsgBoxStyleSupport.RetryCancel) {
            int res = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.OK_CANCEL_OPTION);
            if (res == JOptionPane.OK_OPTION) {
                return MsgBoxResultSupport.Retry;
            } else {
                return MsgBoxResultSupport.Cancel;
            }
      } else if (style == MsgBoxStyleSupport.SystemModal) {
            JOptionPane.showMessageDialog(null, message, title, JOptionPane.PLAIN_MESSAGE);
            return MsgBoxResultSupport.OK;
      } else if (style == MsgBoxStyleSupport.YesNo) {
            int res = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_OPTION); 
             if (res == JOptionPane.YES_OPTION) {
                return MsgBoxResultSupport.Yes;
            } else {
                return MsgBoxResultSupport.No;
            }
      } else if (style == MsgBoxStyleSupport.YesNoCancel) {
            int res = JOptionPane.showConfirmDialog(null, message, title, JOptionPane.YES_NO_CANCEL_OPTION);
           
            if (res == JOptionPane.YES_OPTION) {
                return MsgBoxResultSupport.Yes;
            } else if (res == JOptionPane.NO_OPTION) {
                return MsgBoxResultSupport.No;
            } else {
                return MsgBoxResultSupport.Cancel;
            }
      }
            
      return -1;
  }
  
  public static int showDialog(Object message) {
      return showDialog(message, MsgBoxStyleSupport.OKOnly);
  }
    
}

