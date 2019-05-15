package ${voPackage};

import java.util.ArrayList;
import java.util.List;

import ${basePackage}.Expression;
import ${basePackage}.ExpressionChain;
import ${modelPackage}.${modelClassSimpleName};

public class ${voClassSimpleName} extends ${modelClassSimpleName} {
    private static final long serialVersionUID = 1L;

    private List<ExpressionChain> expressionChainList;

    public ${voClassSimpleName}() {
        expressionChainList = new ArrayList<ExpressionChain>();
    }

    public ${voClassSimpleName} or(ExpressionChain expressionChain) {
        expressionChainList.add(expressionChain);
        return this;
    }

    public ${voClassSimpleName} or(Expression expression) {
        expressionChainList.add(new ExpressionChain().and(expression));
        return this;
    }

    public ${voClassSimpleName} and(Expression expression) {
        if (expressionChainList.isEmpty()) {
            expressionChainList.add(new ExpressionChain());
        }
        expressionChainList.get(0).and(expression);
        return this;
    }

    public List<ExpressionChain> getExpressionChainList() {
        return expressionChainList;
    }

    public void setExpressionChainList(List<ExpressionChain> expressionChainList) {
        this.expressionChainList = expressionChainList;
    }
}
