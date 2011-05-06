
 /* 
 * Copyright (c) 2007 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *  
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *  
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *  
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *  
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */
package com.sun.dn.parser.expression;
	
import java.util.*;
import com.sun.dn.util.*;
import com.sun.dn.parser.*;


	/** A .NET expression denoting a date. Defined as <br>
	* DateLiteral ::= # [ Whitespace+ ] [ DateValue ] [ Whitespace+ ] [ TimeValue ] [ Whitespace+ ] # <br>
	* DateValue ::= MonthValue DateSeparator DayValue DateSeparator YearValue <br>
	* DateSeparator ::= / | - <br>
	* TimeValue ::= HourValue [ : MinuteValue ] [ : SecondValue ] [ WhiteSpace+ ] [ AMPM ] <br>
	* MonthValue ::= IntLiteral <br>
	* DayValue ::= IntLiteral <br>
	* YearValue ::= IntLiteral <br>
	* HourValue ::= IntLiteral <br>
	* MinuteValue ::= IntLiteral <br>
	* SecondValue ::= IntLiteral <br>
	* AMPM ::= AM | PM <br>
	* example: #1/1/2000 11:00:00 AM# <br>
	* @author danny.coward@sun.com
	**/

public class DateLiteral extends Literal {
	private String year;
	private String month;
	private String date;
	private String hour;
	private String minute;
	private String second;
	private String ampm;
        
        private DateLiteral(String code, InterpretationContext context) {
            super(code, context);
        }
        
        public static DateLiteral createVBDateLiteral(String s, InterpretationContext context) {
            DateLiteral dl = new DateLiteral(s, context);
            dl.parseVBDate(s);
            return dl;
        }

	private void parseVBDate(String s) {
		try {

			//String literal = s.substring(s.indexOf("#") + 1, s.lastIndexOf("#") - 1).trim();
			String literal = s;

			StringTokenizer st = new StringTokenizer(s);
			String dateString = st.nextToken();
			StringTokenizer dst = new StringTokenizer(dateString , "/");
			date = dst.nextToken();
			month = dst.nextToken();
			year = dst.nextToken();
			String rest = literal.substring(dateString.length(), literal.length()).trim();
			StringTokenizer tst = new StringTokenizer(rest, ":");
			hour = tst.nextToken();
			minute = tst.nextToken();
			String secondAMPM = tst.nextToken();
			StringTokenizer samapm = new StringTokenizer(secondAMPM);
			second = samapm.nextToken();
			ampm = samapm.nextToken();
		} catch (Throwable t) {
			throw new RuntimeException("Error parsiong date " + s);
		}
	}
        
        public static boolean matchesTypeChar(String type, String ch) {
            return getDType().endsWith(type) && "#".equals(ch);
        }
        
        public String getTypeName() {
            return getDType();
        }
        
        public DNType getDNType() {
            return context.getLibrary().getLibraryData().getLibraryClass(this.getTypeName());
        }
        
        private static String getDType() {
            return "System.Date";
        }

	private int asI(String s) {
		if (s.equals("")) {
			return 0;
		} else {
			return (new Integer(s)).intValue();
		}
	}	

	public String tryAsJava() {
		int hourI;
		if (ampm.equals("AM")) {
			hourI = asI(hour);
		} else {
			hourI = asI(hour) + 12;
		}
		int yearI = asI(year); //dannyc + 1900 ?
		return "(new GregorianCalendar(" + yearI + ", " + asI(month) 
				+ ", " + asI(date) + ", " + hourI + ", " + asI(minute) 
				+ ", " + asI(second) + ")).getTime()";
	}


}
 