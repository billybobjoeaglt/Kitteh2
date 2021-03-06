/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler.tac.optimize;
import compiler.Operator;
import compiler.tac.TACConst;
import compiler.tac.TACStandard;
import compiler.tac.TACStatement;
import java.util.List;

/**
 * Remove some simple calculations that are redundant. Specifically, adding to
 * zero or multiplying by one.
 *
 * @author leijurv
 */
public class RedundantCalculations extends TACOptimization {
    @Override
    protected void run(List<TACStatement> block, int blockBegin) {
        for (int i = 0; i < block.size(); i++) {
            if (block.get(i) instanceof TACStandard) {
                TACStandard ts = (TACStandard) (block.get(i));
                if (ts.op == Operator.PLUS) {
                    if (ts.paramNames[0].equals("0")) {
                        TACConst repl = new TACConst(ts.paramNames[2], ts.paramNames[1]);
                        repl.copyFrom(ts);
                        repl.setVars();
                        block.set(i, repl);
                        continue;
                    }
                    if (ts.paramNames[1].equals("0")) {
                        TACConst repl = new TACConst(ts.paramNames[2], ts.paramNames[0]);
                        repl.copyFrom(ts);
                        repl.setVars();
                        block.set(i, repl);
                        continue;
                    }
                }
                if (ts.op == Operator.MULTIPLY) {
                    if (ts.paramNames[0].equals("1")) {
                        TACConst repl = new TACConst(ts.paramNames[2], ts.paramNames[1]);
                        repl.copyFrom(ts);
                        repl.setVars();
                        block.set(i, repl);
                        continue;
                    }
                    if (ts.paramNames[1].equals("1")) {
                        TACConst repl = new TACConst(ts.paramNames[2], ts.paramNames[0]);
                        repl.copyFrom(ts);
                        repl.setVars();
                        block.set(i, repl);
                        continue;
                    }
                }
                //TODO shifting x by 1, or shifting 0 by x
            }
        }
    }
}
