/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.expression;
import compiler.Context;
import compiler.tac.IREmitter;
import compiler.tac.TACFunctionCall;
import compiler.tac.TempVarUsage;
import compiler.type.Type;
import compiler.type.TypeVoid;
import java.util.ArrayList;

/**
 *
 * @author leijurv
 */
public class ExpressionFunctionCall extends Expression {
    String funcName;
    ArrayList<Expression> args;
    public ExpressionFunctionCall(String funcName, ArrayList<Expression> args) {
        this.funcName = funcName;
        this.args = args;
    }
    @Override
    public Type calcType() {
        return new TypeVoid();
    }
    public String toString() {
        return funcName + args;
    }
    @Override
    public void generateTAC(Context context, IREmitter emit, TempVarUsage tempVars, String resultLocation) {
        ArrayList<String> argNames = new ArrayList<>(args.size());
        for (Expression exp : args) {
            String tempName = tempVars.getTempVar();
            exp.generateTAC(context, emit, tempVars, tempName);
            argNames.add(tempName);
        }
        emit.emit(new TACFunctionCall(resultLocation, funcName, argNames));
    }
    @Override
    public int calculateTACLength() {
        int sum = 0;
        for (Expression exp : args) {
            sum += exp.getTACLength();
        }
        return sum + 1;
    }
}