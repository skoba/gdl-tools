package se.cambio.cds.gdl.model.readable.rule.lines;

import se.cambio.cds.gdl.model.expression.BinaryExpression;
import se.cambio.cds.gdl.model.expression.ConstantExpression;
import se.cambio.cds.gdl.model.expression.ExpressionItem;
import se.cambio.cds.gdl.model.expression.OperatorKind;
import se.cambio.cds.gdl.model.expression.Variable;
import se.cambio.cds.gdl.model.readable.rule.lines.elements.ArchetypeElementRuleLineElement;
import se.cambio.cds.gdl.model.readable.rule.lines.elements.ExistenceOperatorRuleLineElement;
import se.cambio.cds.gdl.model.readable.rule.lines.elements.StaticTextRuleLineElement;
import se.cambio.cds.gdl.model.readable.rule.lines.interfaces.ConditionRuleLine;
import se.cambio.cds.openehr.model.archetypeelement.vo.ArchetypeElementVO;
import se.cambio.cds.openehr.util.OpenEHRLanguageManager;


public class ElementInitializedConditionRuleLine extends ExpressionRuleLine implements ConditionRuleLine{

    private ArchetypeElementRuleLineElement archetypeElementRuleLineElement = null;
    private ExistenceOperatorRuleLineElement existenceOperatorRuleLineElement = null;
    private String NULL_STR = "null";

    public ElementInitializedConditionRuleLine() {
	super(OpenEHRLanguageManager.getMessage("ElementExists"), 
		OpenEHRLanguageManager.getMessage("ElementExistsDesc"));
	archetypeElementRuleLineElement = new ArchetypeElementRuleLineElement(this);
	existenceOperatorRuleLineElement = new ExistenceOperatorRuleLineElement(this);
	getRuleLineElements().add(new StaticTextRuleLineElement(OpenEHRLanguageManager.getMessage("ElementRLE")));
	getRuleLineElements().add(archetypeElementRuleLineElement);
	getRuleLineElements().add(existenceOperatorRuleLineElement);
    }

    public ArchetypeElementRuleLineElement getArchetypeElementRuleLineElement(){
	return archetypeElementRuleLineElement;
    }

    public ExistenceOperatorRuleLineElement getExistenceOperatorRuleLineElement(){
	return existenceOperatorRuleLineElement;
    }

    public ExpressionItem toExpressionItem() throws IllegalStateException{
	ArchetypeElementVO archetypeElementVO = getArchetypeElementRuleLineElement().getArchetypeElementVO();
	if (archetypeElementVO!=null){
	    String gtCode = 
		    getArchetypeElementRuleLineElement().getValue().getValue();
	    OperatorKind operatorKind = getExistenceOperatorRuleLineElement().getOperator();
	    if (operatorKind==null){
		throw new IllegalStateException("No operator set");
	    }
	    return new BinaryExpression(
		    new Variable(gtCode, null, archetypeElementVO.getName()),
		    new ConstantExpression(NULL_STR),
		    operatorKind);
	}else{
	    throw new IllegalStateException("Element instance not found for"+ this.toString());
	}
    }
}/*
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