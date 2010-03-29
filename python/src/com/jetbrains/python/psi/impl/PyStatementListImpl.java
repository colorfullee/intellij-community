package com.jetbrains.python.psi.impl;

import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.ResolveState;
import com.intellij.psi.scope.PsiScopeProcessor;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.IncorrectOperationException;
import com.jetbrains.python.PyElementTypes;
import com.jetbrains.python.psi.PyElementVisitor;
import com.jetbrains.python.psi.PyStatement;
import com.jetbrains.python.psi.PyStatementList;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author yole
 */
public class PyStatementListImpl extends PyElementImpl implements PyStatementList {
  public PyStatementListImpl(ASTNode astNode) {
    super(astNode);
  }

  @Override
  protected void acceptPyVisitor(PyElementVisitor pyVisitor) {
    pyVisitor.visitPyStatementList(this);
  }

  public PyStatement[] getStatements() {
    return childrenToPsi(PyElementTypes.STATEMENTS, PyStatement.EMPTY_ARRAY);
  }

  @Override
  public boolean processDeclarations(@NotNull PsiScopeProcessor processor,
                                     @NotNull ResolveState substitutor,
                                     PsiElement lastParent,
                                     @NotNull PsiElement place) {
    // in if statements, process only the section in which the original expression was located
    PsiElement parent = getParent();
    if (parent instanceof PyStatement && lastParent == null && PsiTreeUtil.isAncestor(parent, place, true)) {
      return true;
    }

    PyStatement[] statements = getStatements();
    if (lastParent != null) {
      // if we're processing the statement list in which the last parent is found, scan up
      // from parent
      for (int i = 0; i < statements.length; i++) {
        if (statements[i] == lastParent) {
          for (int j = i - 1; j >= 0; j--) {
            if (!statements[j].processDeclarations(processor, substitutor, lastParent, place)) {
              return false;
            }
          }
        }
      }
    }

    for (PyStatement statement : getStatements()) {
      if (!statement.processDeclarations(processor, substitutor, lastParent, place)) {
        return false;
      }
    }
    return true;
  }

  @Override
  public PsiElement add(@NotNull PsiElement element) throws IncorrectOperationException {
    final PsiElement preprocessed = preprocessElement(element);
    if (preprocessed != null){
      final PsiElement result = super.add(preprocessed.copy());
      /*
      if (result instanceof PyFunction) {
        CodeStyleManager.getInstance(getManager()).adjustLineIndent(getContainingFile(), result.getTextRange());
      }
      */
      return result;
    }
    return super.add(element);
  }

  @Override
  public PsiElement addBefore(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
    final PsiElement preprocessed = preprocessElement(element);
    if (preprocessed != null){
      return super.addBefore(preprocessed, anchor);
    }
    return super.addBefore(element, anchor);
  }

  @Override
  public PsiElement addAfter(@NotNull PsiElement element, PsiElement anchor) throws IncorrectOperationException {
    final PsiElement preprocessed = preprocessElement(element);
    if (preprocessed != null){
      return super.addAfter(preprocessed, anchor);
    }
    return super.addAfter(element, anchor);
  }

  @Nullable
  private PsiElement preprocessElement(PsiElement element) {
    if (element instanceof PsiWhiteSpace) return element;
    return PyPsiUtils.removeIndentation(element);
    //return element;
  }
}
