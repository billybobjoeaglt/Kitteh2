/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.tac;
import compiler.type.TypePointer;
import compiler.x86.X86Emitter;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author leijurv
 */
public class TACConstStr extends TACStatement {
    private final String value;
    public TACConstStr(String variable, String value) {
        super(variable);
        this.value = value;
    }
    @Override
    protected void onContextKnown() {
        if (!(params[0].getType() instanceof TypePointer)) {
            throw new RuntimeException(params[0] + " " + params[0].getType());
        }
    }
    @Override
    public String toString0() {
        return params[0] + " = CONST STR \"" + value + "\"";
    }
    @Override
    public void printx86(X86Emitter emit) {
        emit.addStatement("leaq " + getLabel() + "(%rip), %rax");
        emit.addStatement("movq %rax, " + params[0].x86());
    }
    public String getLabel() {
        return ("str" + value.hashCode()).replace("-", "_");
    }
    public String getValue() {
        return value;
    }
    @Override
    public List<String> requiredVariables() {
        return Arrays.asList();
    }
    @Override
    public List<String> modifiedVariables() {
        return Arrays.asList(paramNames[0]);
    }
}
