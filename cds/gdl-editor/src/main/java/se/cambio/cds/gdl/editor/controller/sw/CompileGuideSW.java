package se.cambio.cds.gdl.editor.controller.sw;

import org.apache.log4j.Logger;

import se.cambio.cds.gdl.editor.controller.GDLEditor;
import se.cambio.cds.gdl.editor.controller.exportplugins.GuideExportPluginDirectory;
import se.cambio.cds.gdl.model.Guide;
import se.cambio.cds.util.CDSSwingWorker;
import se.cambio.cds.util.exceptions.InternalErrorException;
import se.cambio.cds.util.handlers.ExceptionHandler;

public class CompileGuideSW extends CDSSwingWorker{
    private String _errorMsg = null;
    private GDLEditor _controller = null;
    private byte[] _compiledGuide = null;

    public CompileGuideSW(GDLEditor controller){
	_controller = controller;
    }

    @Override
    protected void executeCDSSW() throws InternalErrorException{
	try{
	    Guide guide = _controller.getGuide();
	    if (guide!=null){
		_compiledGuide = GuideExportPluginDirectory.compile(guide);
	    }
	}catch(Throwable e){
	    _errorMsg = e.getMessage();
	    Logger.getLogger(CompileGuideSW.class).warn("ERROR Compiling guide '"+_controller.getGuide().getId()+"': "+e.getMessage());
	    ExceptionHandler.handle(e);
	}
    }

    public byte[] getCompiledGuide(){
	return _compiledGuide;
    }

    public String getErrorMsg(){
	return _errorMsg;
    }

    public GDLEditor getController(){
	return _controller;
    }

    protected void done() {
	_controller.compilationFinished(_errorMsg);
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