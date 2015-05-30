package ${basePackageName}.${projectName}.vo;

import java.util.ArrayList;
import java.util.List;

import ${basePackageName}.common.dao.Expression;
import ${basePackageName}.common.dao.ExpressionChain;
import ${basePackageName}.${projectName}.model.${modelClassSimpleName};

public class ${modelClassSimpleName}VO extends ${modelClassSimpleName} {
    private static final long serialVersionUID = 1L;

    private List<ExpressionChain> expressionChainList;

    public ${modelClassSimpleName}VO() {
        expressionChainList = new ArrayList<ExpressionChain>();
    }

    public ${modelClassSimpleName}VO or(ExpressionChain expressionChain) {
        expressionChainList.add(expressionChain);
        return this;
    }

    public ${modelClassSimpleName}VO or(Expression expression) {
        expressionChainList.add(new ExpressionChain().and(expression));
        return this;
    }

    public ${modelClassSimpleName}VO and(Expression expression) {
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
