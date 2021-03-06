package se.cambio.cds.model.facade.execution.vo;


public enum ExecutionMode {
    STRICT(0),
    NORMAL(1),
    FULL(2);

    private int value;

    private ExecutionMode(int value) {
	this.value = value;
    }
    public int getValue(){
	return value;
    }
    public String toString() {
	return Integer.toString(value);
    }
    public static ExecutionMode valueOf(int value) {
	if (STRICT.getValue()==value){
	    return STRICT;
	}else if (NORMAL.getValue()==value){
	    return NORMAL;
	}else if (FULL.getValue()==value){
	    return FULL;
	}else{
	    throw new IllegalArgumentException("unknown value");
	}
    }
}
/*
 *  ***** BEGIN LICENSE BLOCK *****
 *  Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 *  The contents of this file are subject to the Mozilla Public License Version
 *  1.1 (the 'License'); you may not use this file except in compliance with
 *  the License. You may obtain a copy of the License at
 *  http://www.mozilla.org/MPL/
 *
 *  Software distributed under the License is distributed on an 'AS IS' basis,
 *  WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 *  for the specific language governing rights and limitations under the
 *  License.
 *
 *
 *  The Initial Developers of the Original Code are Iago Corbal and Rong Chen.
 *  Portions created by the Initial Developer are Copyright (C) 2012-2013
 *  the Initial Developer. All Rights Reserved.
 *
 *  Contributor(s):
 *
 * Software distributed under the License is distributed on an 'AS IS' basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 *  ***** END LICENSE BLOCK *****
 */