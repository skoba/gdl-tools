package se.cambio.cds.ts;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.datatypes.text.DvCodedText;

import se.cambio.cds.util.exceptions.InternalErrorException;

public class CSVTerminologyServicePlugin implements TerminologyServicePlugin {

    private String _terminologyId = null;
    private Map<String, ArrayList<String>> _parentsMap = null;
    private Map<String, ArrayList<String>> _childrenMap = null;
    private Map<String, String> _descriptionsMap = null;

    public CSVTerminologyServicePlugin(String terminologyId){
	_terminologyId = terminologyId;
    }

    public void init(InputStream is) throws InternalErrorException{
	try {
	    CSVReader csvReader = new CSVReader(new BufferedReader(new InputStreamReader(is)));
	    _parentsMap = new HashMap<String, ArrayList<String>>();
	    _childrenMap = new HashMap<String, ArrayList<String>>();
	    _descriptionsMap = new HashMap<String, String>();
	    csvReader.readHeaders();
	    while (csvReader.readRecord()) {
		String id = csvReader.get("id");
		String description = csvReader.get("text");
		String parent = csvReader.get("parent");
		log.debug("id: " + id + ", description: " + description);
		if (id != null && !id.isEmpty() && description != null
			&& !description.isEmpty()) {
		    _descriptionsMap.put(id, description);
		    if (parent != null && !parent.isEmpty()) {
			// Add parent
			ArrayList<String> parents = new ArrayList<String>();
			parents.add(parent);
			ArrayList<String> hierarchy = _parentsMap.get(parent);
			if (hierarchy != null) {
			    parents.addAll(hierarchy);
			}
			_parentsMap.put(id, parents);
			// Add child to parent
			ArrayList<String> children = _childrenMap.get(parent);
			if (children == null) {
			    children = new ArrayList<String>();
			    _childrenMap.put(parent, children);
			}
			children.add(id);
		    }
		}
	    }
	    log.debug("Total " + _descriptionsMap.size() + " term(s) loaded..");
	} catch (Exception e) {
	    Logger.getLogger(TerminologyServiceImpl.class).warn(
		    "Failed to initialize the terminology service '"
			    + _terminologyId + "'", e);
	    throw new InternalErrorException(e);
	}
    }

    public String getTerminologyId() {
	return _terminologyId;
    }

    public boolean isSubclassOf(CodePhrase a, CodePhrase b)
	    throws UnsupportedTerminologyException, InvalidCodeException {
	checkTerminologySupported(a);
	checkTerminologySupported(b);
	return checkSubclassOf(a, b);
    }

    public boolean isSubclassOf(CodePhrase code, Set<CodePhrase> codes)
	    throws UnsupportedTerminologyException, InvalidCodeException {

	checkTerminologySupported(code);
	boolean ret = false;
	for (CodePhrase cp : codes) {
	    if( ! code.getTerminologyId().equals(cp.getTerminologyId())) {
		// simply ignore instead of stopping the rest codes
		continue;
	    }
	    if (checkSubclassOf(code, cp)) {
		ret = true;
		break;
	    }
	}
	return ret;
    }

    public Node retrieveAllSubclasses(CodePhrase concept, CodePhrase language)
	    throws UnsupportedTerminologyException,
	    UnsupportedLanguageException, InvalidCodeException {
	String code = concept.getCodeString();
	return retrieveAllSubclasses(code, language);
    }

    private Node retrieveAllSubclasses(String code, CodePhrase language) 
	    throws UnsupportedTerminologyException, UnsupportedLanguageException {
	// TODO Memory eater!VVVV
	Node node = getNodeForCode(code, language);
	ArrayList<String> children = _childrenMap.get(code);
	if (children != null) {
	    for (String childCode : children) {
		Node nodeAux = retrieveAllSubclasses(childCode, language);
		if (nodeAux != null) {
		    node.addChild(nodeAux);
		}
	    }
	}
	return node;
    }

    public List<Node> retrieve(String expression, CodePhrase language)
	    throws UnsupportedTerminologyException,
	    UnsupportedLanguageException {
	// TODO Auto-generated method stub
	return null;
    }

    public List<Node> retrieveAll(String terminologyId, CodePhrase language)
	    throws UnsupportedTerminologyException,
	    UnsupportedLanguageException {
	if (_terminologyId.equals(terminologyId)){
	    ArrayList<Node> allNodes = new ArrayList<Node>();
	    for (String code : _descriptionsMap.keySet()) {
		if (!code.isEmpty() && _parentsMap.get(code)==null){
		    Node node = getNodeForCode(code, language);
		    ArrayList<String> children = _childrenMap.get(code);
		    if (children != null) {
			for (String childCode : children) {
			    Node nodeAux = retrieveAllSubclasses(childCode, language);
			    if (nodeAux != null) {
				node.addChild(nodeAux);
			    }
			}
		    }
		    allNodes.add(node);
		}
	    }
	    //Nodes without parent/children
	    /*
	    for (String code : _descriptionsMap.keySet()) {
		if (!_childrenMap.keySet().contains(code) &&
			!_parentsMap.containsKey(code)){
		    allNodes.add(getNodeForCode(code, language));
		}
	    }*/
	    return allNodes;
	}else{
	    throw new UnsupportedTerminologyException(terminologyId+ " not supported");
	}
    }

    private Node getNodeForCode(String code, CodePhrase language) 
	    throws UnsupportedTerminologyException, UnsupportedLanguageException{
	String desc = retrieveTerm(code, language);
	if (desc == null) {
	    desc = code;
	}
	return new Node(new DvCodedText(desc, new CodePhrase(_terminologyId, code)));
    }

    public boolean hasPropertyOfValue(CodePhrase concept, CodePhrase property,
	    CodePhrase value) throws UnsupportedTerminologyException,
	    UnknownPropertyException {
	// TODO Auto-generated method stub
	return false;
    }

    public List<DvCodedText> retrieveAllPossibleValues(CodePhrase property,
	    CodePhrase language) throws UnsupportedTerminologyException,
	    UnknownPropertyException, UnsupportedLanguageException {
	// TODO Auto-generated method stub
	return null;
    }

    public boolean isTerminologySupported(String terminologyId) {
	return _terminologyId.equalsIgnoreCase(terminologyId);
    }

    private boolean checkSubclassOf(CodePhrase a, CodePhrase b)
	    throws UnsupportedTerminologyException, InvalidCodeException {
	if (isValidTerminologyCode(a) && isValidTerminologyCode(b)) {
	    String as = a.getCodeString();
	    String bs = b.getCodeString();
	    return checkSubclassOf(as,bs);
	} else {
	    throw new UnsupportedTerminologyException(a.getTerminologyId()
		    + " not supported");
	}
    }

    protected boolean checkSubclassOf(String as, String bs)
	    throws UnsupportedTerminologyException, InvalidCodeException {
	if (invalidCode(as) || invalidCode(bs)) {
	    throw new InvalidCodeException("Invalid " + _terminologyId
		    + " code: " + as);
	}
	if (as.equals(bs)) {
	    return true;
	} else {
	    ArrayList<String> parents = _parentsMap.get(as);
	    return (parents != null && parents.contains(bs));
	}
    }

    private void checkTerminologySupported(CodePhrase code)
	    throws UnsupportedTerminologyException {
	checkTerminologySupported(code.getTerminologyId().getValue());
    }

    private void checkTerminologySupported(String terminology)
	    throws UnsupportedTerminologyException {
	if (!isTerminologySupported(terminology)) {
	    throw new UnsupportedTerminologyException(terminology
		    + " not supported");
	}
    }

    protected boolean invalidCode(String code) {
	return getDescriptionsMap().get(code) == null;
    }

    private Map<String, String> getDescriptionsMap() {
	if (_descriptionsMap == null) {
	    _descriptionsMap = new HashMap<String, String>();
	}
	return _descriptionsMap;
    }

    public void registerDescription(String code, String description) {
	getDescriptionsMap().put(code, description);
    }

    protected String getDescription(String code) {
	return getDescriptionsMap().get(code);
    }

    private boolean isValidTerminologyCode(CodePhrase code) {
	return isTerminologySupported(code.getTerminologyId().getValue());
    }

    public String retrieveTerm(CodePhrase concept, CodePhrase language)
	    throws UnsupportedTerminologyException,
	    UnsupportedLanguageException {
	return retrieveTerm(concept.getCodeString(), language);
    }

    protected String retrieveTerm(String code, CodePhrase language)
	    throws UnsupportedTerminologyException,
	    UnsupportedLanguageException {
	return getDescription(code);
    }
    private static Logger log = Logger.getLogger(CSVTerminologyServicePlugin.class);

    public Collection<String> getSupportedTerminologies() {
	Collection<String> supportedTerminologies = new ArrayList<String>();
	supportedTerminologies.add(_terminologyId);
	return supportedTerminologies;
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