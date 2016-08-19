/*******************************************************************************
 * Copyright (c) 2016 University of York
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Horacio Hoyos - Initial API and implementation
 *******************************************************************************/
package org.eclipse.epsilon.emc.artisan.jawin;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.epsilon.emc.COM.COMCollection;
import org.eclipse.epsilon.emc.COM.COMObject;
import org.eclipse.epsilon.emc.COM.EpsilonCOMException;
import org.eclipse.epsilon.eol.dom.EqualsOperatorExpression;
import org.eclipse.epsilon.eol.dom.Expression;
import org.eclipse.epsilon.eol.dom.NameExpression;
import org.eclipse.epsilon.eol.dom.OperatorExpression;
import org.eclipse.epsilon.eol.dom.PropertyCallExpression;
import org.eclipse.epsilon.eol.dom.StringLiteral;
import org.eclipse.epsilon.eol.exceptions.EolInternalException;
import org.eclipse.epsilon.eol.exceptions.EolRuntimeException;
import org.eclipse.epsilon.eol.execute.context.IEolContext;
import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.eol.execute.operations.declarative.SelectOperation;

/**
 * The Class OptimisableCollectionSelectOperation allows the Artisan Model to provide
 * optimized execution of select queries by name using Items automation calls.  
 */
public class JawinCollectionSelectOperation extends SelectOperation {
	
	private IEolContext context;
	private Variable iterator;

	@Override
	public Object execute(Object target, Variable iterator, Expression ast, IEolContext context,
			boolean returnOnFirstMatch) throws EolRuntimeException {

//		System.err.println("OptimisableCollectionSelectOperation execute called!");
		if (!(target instanceof COMCollection)) {
			return super.execute(target, iterator, ast, context, returnOnFirstMatch);
		}
		try {
			this.context = context;
			this.iterator = iterator;
			return decomposeAST((COMCollection) target, ast,returnOnFirstMatch);

		} catch (Exception e) {
			e.printStackTrace();
			throw new EolRuntimeException("OptimisableCollectionSelectOperation: parseAST(iterator, ast) failed:", ast);
		}

	}

	@SuppressWarnings("unchecked")
	protected Collection<Object> decomposeAST(COMCollection target, Expression ast, boolean returnOnFirstMatch) throws Exception {

		if (isOptimisable(ast)) {
			return optimisedExecution(target, ast, returnOnFirstMatch);
		} else {
			// System.err.println("giving to super: "+ast.toStringTree());
			Object ret = super.execute(target, iterator, (Expression) ast, context, returnOnFirstMatch);
			// System.err.println("super returns: "+ret.getClass());
			return (Collection<Object>) ret;

		}

	}

	@SuppressWarnings("unchecked")
	private Collection<Object> optimisedExecution(COMCollection target, Expression ast, boolean returnOnFirstMatch) throws EolRuntimeException {

		// NOTE: this assumes that isOptimisable(ast) returned true
		final OperatorExpression opExp = (OperatorExpression) ast;
		final PropertyCallExpression lOperand = (PropertyCallExpression) opExp.getFirstOperand();
		final String attributename = lOperand.getPropertyNameExpression().getName();
		// FIXME Think there are some types that have another default
		if (!"name".equals(attributename.toLowerCase())) {		// Name is the default Id
			return (Collection<Object>) super.execute(target, iterator, (Expression) ast, context, returnOnFirstMatch);
		}
		final String variableName = ((NameExpression) lOperand.getTargetExpression()).getName();
		final Expression valueAST = opExp.getSecondOperand();
		Object attributevalue = null;
		try {
			attributevalue = context.getExecutorFactory().executeAST(valueAST, context);
		} catch (Exception e) {
			// if the rhs is invalid or tries to use the iterator of the select
			// (which is outside its scope) -- default to epsilon's select
			// FIXME Make message more Artisan like
			System.err.println("Warning: the RHS of the expression:\n" + ast
					+ "\ncannot be evaluated using database indexing,\nas the iterator variable of the current select operation ("
					+ iterator.getName() + ") is not used in this process.\nDefaulting to Epsion's select");
		}
		if (attributevalue != null) {
			assert attributevalue instanceof String;
			List<Object> args = new ArrayList<Object>();
			args.add(target.getAssociation());
			args.add(attributevalue);
			COMObject comresult = null;
			try {
				comresult = (COMObject) target.getOwner().invoke("Items", args);
			} catch (EpsilonCOMException e) {
				throw new EolInternalException(e);
			}
			Collection<? extends COMObject> result = comresult.wrapInFilteredColleciton(target.getAssociation());
			return (Collection<Object>) result;
			
		} else {
			return (Collection<Object>) super.execute(target, iterator, (Expression) ast, context, returnOnFirstMatch);
		}
	}
	
	/**
	 * We can only optimize select expressions in which the condition of the form:
	 * iterator.Name = "value"
	 * 
	 * @param ast
	 * @return
	 */
	private boolean isOptimisable(Expression ast) {
		try {
			if (!(ast instanceof OperatorExpression)) {
				return false;
			}

			// LEFT - we should have iterator.property
			// L1. Check for a property call expression
			final OperatorExpression opExp = (OperatorExpression) ast;
			final Expression rawLOperand = opExp.getFirstOperand();
			if (!(rawLOperand instanceof PropertyCallExpression)) {
				return false;
			}
			final PropertyCallExpression lOperand = (PropertyCallExpression) rawLOperand;

			// L2. Check that we're using the iterator
			final Expression rawTargetExpression = lOperand.getTargetExpression();
			if (!(lOperand.getTargetExpression() instanceof NameExpression)) {
				return false;
			}
			final NameExpression nameExpression = (NameExpression) rawTargetExpression;
			if (!iterator.getName().equals(nameExpression.getName())) {
				return false;
			}
			// MIDDLE - we should be using a comparison operator
			if (!(ast instanceof EqualsOperatorExpression)) {
				return false;
			}
			// RIGHT - we should have a value (String)
			final Expression rawROperand = opExp.getSecondOperand();
			return rawROperand instanceof StringLiteral;
			
			
		} catch (Exception e) {
			return false;
		}
	}

//	private String isIndexed(String attributename) {
//
//		String result = null;
//
//		try (IGraphTransaction ignored = graph.beginTransaction()) {
//
//			String indexname = metaclass.getOutgoingWithType("epackage").iterator().next().getEndNode()
//					.getProperty(IModelIndexer.IDENTIFIER_PROPERTY) + "##"
//					+ metaclass.getProperty(IModelIndexer.IDENTIFIER_PROPERTY) + "##" + attributename;
//
//			// if (indexManager == null) indexManager = graph.index();
//
//			// System.err.println(indexname);
//			// System.err.println(graph.getNodeIndexNames());
//
//			if (graph.nodeIndexExists(indexname))
//				result = indexname;
//
//			ignored.success();
//
//		} catch (Exception e) {
//			System.err
//					.println("OptimisableCollectionSelectOperation, isIndexed, suppressed exception: " + e.getCause());
//			e.printStackTrace();
//		}
//
//		return result;
//
//	}

}
