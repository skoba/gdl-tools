package se.cambio.cds.gdl.editor.view.dialog;

import java.awt.Dimension;
import java.awt.Window;

import se.cambio.cds.gdl.editor.util.LanguageManager;
import se.cambio.cds.openehr.view.trees.SelectableNode;

public class DialogElementSelection extends DialogSelection{

    /**
     * 
     */
    private static final long serialVersionUID = 1L;
    public DialogElementSelection(Window owner, SelectableNode<Object> rootNode) {
	super(owner, LanguageManager.getMessage("SelectElementInstance"), rootNode, true, new Dimension(500,500));
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