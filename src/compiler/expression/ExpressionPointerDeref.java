/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.expression;
import compiler.Context;
import compiler.command.Command;
import compiler.command.CommandSetPtr;
import compiler.tac.IREmitter;
import compiler.tac.TACJumpBoolVar;
import compiler.tac.TACPointerDeref;
import compiler.tac.TempVarUsage;
import compiler.type.Type;
import compiler.type.TypeBoolean;
import compiler.type.TypePointer;

/**
 *
 * @author leijurv
 */
public class ExpressionPointerDeref extends ExpressionConditionalJumpable implements Settable {
    final Expression deReferencing;
    public ExpressionPointerDeref(Expression deref) {
        this.deReferencing = deref;
    }
    @Override
    protected Type calcType() {
        TypePointer tp = (TypePointer) deReferencing.getType();
        return tp.pointingTo();
    }
    @Override
    public void generateTAC(IREmitter emit, TempVarUsage tempVars, String resultLocation) {
        String tmp = tempVars.getTempVar(deReferencing.getType());
        deReferencing.generateTAC(emit, tempVars, tmp);
        emit.emit(new TACPointerDeref(tmp, resultLocation));
    }
    @Override
    protected int calculateTACLength() {
        return 1 + deReferencing.getTACLength();
    }
    @Override
    public Expression calculateConstants() {
        return new ExpressionPointerDeref(deReferencing.calculateConstants());
    }
    @Override
    public Expression insertKnownValues(Context context) {
        return new ExpressionPointerDeref(deReferencing.insertKnownValues(context));
    }
    @Override
    public Command setValue(Expression rvalue, Context context) {
        return new CommandSetPtr(context, deReferencing, rvalue);
    }
    @Override
    public void generateConditionalJump(IREmitter emit, TempVarUsage tempVars, int jumpTo, boolean invert) {
        String tmp = tempVars.getTempVar(new TypeBoolean());
        generateTAC(emit, tempVars, tmp);
        emit.emit(new TACJumpBoolVar(tmp, jumpTo, invert));
    }
    @Override
    public int condLength() {
        return 1 + getTACLength();
    }
}
