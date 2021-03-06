package se.cambio.cds.controller.guide;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.openehr.rm.datatypes.basic.DataValue;
import org.openehr.rm.datatypes.text.CodePhrase;
import org.openehr.rm.datatypes.text.DvCodedText;
import org.openehr.rm.datatypes.text.DvText;
import org.openehr.rm.support.terminology.TerminologyService;

import se.cambio.cds.gdl.model.ArchetypeBinding;
import se.cambio.cds.gdl.model.ElementBinding;
import se.cambio.cds.gdl.model.Guide;
import se.cambio.cds.gdl.model.expression.BinaryExpression;
import se.cambio.cds.gdl.model.expression.CodedTextConstant;
import se.cambio.cds.gdl.model.expression.ConstantExpression;
import se.cambio.cds.gdl.model.expression.ExpressionItem;
import se.cambio.cds.gdl.model.expression.OrdinalConstant;
import se.cambio.cds.gdl.model.expression.QuantityConstant;
import se.cambio.cds.gdl.model.expression.StringConstant;
import se.cambio.cds.gdl.model.expression.Variable;
import se.cambio.cds.gdl.parser.DADLSerializer;
import se.cambio.cds.gdl.parser.GDLParser;
import se.cambio.cds.model.facade.execution.vo.ArchetypeReference;
import se.cambio.cds.model.facade.execution.vo.RuleReference;
import se.cambio.cds.util.exceptions.InternalErrorException;

public class GuideUtil {

    public static final DvCodedText NULL_FLAVOUR_VALUE = new DvCodedText(
	    "no information", new CodePhrase(TerminologyService.OPENEHR, "271"));

    public static Guide parseGuide(
	    byte[] guideSrc, 
	    GuideManager guideManager,
	    GeneratedElementInstanceCollection elementInstanceCollection)
		    throws InternalErrorException{
	try {
	    Guide guide = parseGuide(new ByteArrayInputStream(guideSrc));
	    List<ArchetypeBinding> abs = guide.getDefinition().getArchetypeBindings();
	    if (abs!=null){
		for (ArchetypeBinding archetypeBinding: abs) {
		    ArchetypeReference ar = 
			    new GeneratedArchetypeReference(
				    archetypeBinding.getDomain(), 
				    archetypeBinding.getArchetypeId(), 
				    archetypeBinding.getTemplateId(), 
				    archetypeBinding.getFunction());
		    if (archetypeBinding.getElements()!=null){
			for (ElementBinding elementBinding : archetypeBinding.getElements().values()) {
			    String idElement = 
				    archetypeBinding.getArchetypeId()+elementBinding.getPath();
			    new GeneratedElementInstance(
				    idElement,
				    null,
				    ar,
				    null,
				    NULL_FLAVOUR_VALUE,
				    guide.getId(),
				    elementBinding.getId());
			}
		    }
		    if (archetypeBinding.getPredicateStatements()!=null){
			for (ExpressionItem expressionItem : archetypeBinding.getPredicateStatements()) {
			    if (expressionItem instanceof BinaryExpression){
				BinaryExpression be = ((BinaryExpression)expressionItem);
				ExpressionItem l = be.getLeft();
				ExpressionItem r = be.getRight();
				if (l instanceof Variable && (r instanceof ConstantExpression)){
				    String idElement = 
					    archetypeBinding.getArchetypeId()+((Variable)l).getPath();
				    DataValue dv = getDataValue((ConstantExpression)r);
				    new PredicateGeneratedElementInstance(
					    idElement,
					    dv,
					    ar,
					    null,
					    null,
					    guide.getId(),
					    null,
					    be.getOperator());
				}
			    }
			}
		    }
		    elementInstanceCollection.add(ar);
		} 
	    }
	    return guide;
	} catch (Exception e) {
	    throw new InternalErrorException(e);
	}
    }


    private static DataValue getDataValue(ConstantExpression e){
	if (e instanceof CodedTextConstant){
	    return ((CodedTextConstant)e).getCodedText();
	} else if (e instanceof QuantityConstant){
	    return ((QuantityConstant)e).getQuantity();
	} else if (e instanceof StringConstant){
	    return new DvText(((StringConstant)e).getString());
	} else if (e instanceof OrdinalConstant){
	    return ((OrdinalConstant)e).getOrdinal();
	} else {
	    return null; //TODO Proportion, dates, count, etc
	}
    }

    public static List<RuleReference> getRuleReferences(List<String> firedRules){
	List<RuleReference> ruleReferences = new ArrayList<RuleReference>();
	if (firedRules!=null){
	    for (String firedRule : firedRules) {
		ruleReferences.add(new RuleReference(firedRule));
	    }
	}
	return ruleReferences;
    }

    public static String serializeGuide(Guide guide) throws Exception{
	StringBuffer sb = new StringBuffer();
	DADLSerializer serializer = new DADLSerializer();
	for (String line : serializer.toDADL(guide)) {
	    sb.append(line+"\n");
	}
	return sb.toString();
    }

    public static Guide parseGuide(InputStream input) throws Exception{
	GDLParser parser = new GDLParser();
	return parser.parse(input);
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