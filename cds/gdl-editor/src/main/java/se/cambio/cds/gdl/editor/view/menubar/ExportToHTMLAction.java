/*
 * Created on 30-ago-2006
 *


 */
package se.cambio.cds.gdl.editor.view.menubar;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import se.cambio.cds.gdl.editor.controller.EditorManager;
import se.cambio.cds.gdl.editor.controller.GDLEditor;
import se.cambio.cds.gdl.editor.util.LanguageManager;
import se.cambio.cds.gdl.editor.view.util.ExportUtils;
import se.cambio.cds.gdl.model.Guide;

public class ExportToHTMLAction extends AbstractAction {

    /**
     * 
     */
    private static final long serialVersionUID = -3561842193285119707L;

    public ExportToHTMLAction(){
	super();
	putValue(NAME, LanguageManager.getMessage("ExportToHTML")+"...");
	putValue(SMALL_ICON, null);
	putValue(SHORT_DESCRIPTION, LanguageManager.getMessage("ExportToHTMLD"));
	putValue(LONG_DESCRIPTION, LanguageManager.getMessage("ExportToHTMLD"));
	//putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_H, ActionEvent.CTRL_MASK+ActionEvent.SHIFT_MASK));
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e) {
	GDLEditor controller = EditorManager.getActiveGDLEditor();
	Guide guide = controller.getGuide();
	ExportUtils.exportToHTML(
		EditorManager.getActiveEditorWindow(),
		guide,
		controller.getCurrentGuideLanguageCode());
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