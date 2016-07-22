/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compiler;
import compiler.command.Command;
import compiler.parse.Processor;
import compiler.preprocess.Preprocessor;
import compiler.tac.IREmitter;
import compiler.tac.TACReturn;
import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.IntStream;

/**
 *
 * @author leijurv
 */
public class Compiler {
    public static long streamTime() {
        long a = System.currentTimeMillis();
        IntStream.range(0, 5).sum();
        long b = System.currentTimeMillis();
        return b - a;
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        System.out.println("First stream: " + streamTime());
        System.out.println("Second stream: " + streamTime());
        byte[] program = Files.readAllBytes(new File("/Users/leijurv/Documents/euler.k").toPath());
        ArrayList<String> k = Preprocessor.preprocess(new String(program));
        ArrayList<Object> lol = new ArrayList<>();
        for (String l : k) {
            lol.add(l);
        }
        ArrayList<Command> commands = Processor.parse(lol, new Context());
        System.out.println(commands);
        for (Command com : commands) {
            //com.staticValues();
        }
        System.out.println(commands);
        IREmitter emit = new IREmitter();
        for (Command com : commands) {
            com.generateTAC(emit);
        }
        for (int i = 0; i < emit.getResult().size(); i++) {
            System.out.println(i + ":     " + emit.getResult().get(i));
        }
        for (int i = 0; i < 10; i++) {
            System.out.println();
        }
        System.out.println(HEADER);
        emitFunction("main", emit);
        System.out.println(FOOTER);
    }
    public static void emitFunction(String funcName, IREmitter emit) {
        X86Emitter emitter = new X86Emitter(funcName);
        for (int i = 0; i < emit.getResult().size(); i++) {
            emitter.addStatement(emitter.lineToLabel(i) + ":");
            emit.getResult().get(i).printx86(emitter);
        }
        emitter.addStatement(emitter.lineToLabel(emit.getResult().size()) + ":");
        new TACReturn().printx86(emitter);
        System.out.println("_" + funcName + ":");
        System.out.println(FUNC_HEADER);
        System.out.println(emitter.toX86());

        System.out.println(FUNC_FOOTER);
    }
    static String HEADER = "    .section    __TEXT,__text,regular,pure_instructions\n"
            + "    .macosx_version_min 10, 10\n"
            + "    .globl  _main\n"
            + "    .align  4, 0x90";
    static String FOOTER = ".subsections_via_symbols";
    static String FUNC_HEADER = "	.cfi_startproc\n"
            + "	pushq	%rbp\n"
            + "	.cfi_def_cfa_offset 16\n"
            + "	.cfi_offset %rbp, -16\n"
            + "	movq	%rsp, %rbp\n"
            + "	.cfi_def_cfa_register %rbp";
    static String FUNC_FOOTER = "	.cfi_endproc";
}
