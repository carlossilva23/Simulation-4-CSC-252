public class Test_02_executeControl1 {
    public static void main(String[] args) {
        CPUMemory state = new CPUMemory();
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();
        Sim4 sim4 = new Sim4();

        state.instMemory[0] = sim4_test_commonCode.ADD(sim4_test_commonCode.S_REG(0), sim4_test_commonCode.S_REG(1), sim4_test_commonCode.S_REG(2));
        state.instMemory[1] = sim4_test_commonCode.SUB(sim4_test_commonCode.S_REG(3), sim4_test_commonCode.S_REG(4), sim4_test_commonCode.S_REG(5));
        state.instMemory[256] = sim4_test_commonCode.ADDI(sim4_test_commonCode.T_REG(2), sim4_test_commonCode.T_REG(3), -1);
        state.instMemory[512] = sim4_test_commonCode.ADDI(sim4_test_commonCode.T_REG(4), sim4_test_commonCode.T_REG(5), 16);

        for (state.pc = 0x0000; state.pc < 0x1000; state.pc += 4) {
            /* save the original state, to see if the student changes it */
            CPUMemory stateOrig = new CPUMemory();
            System.arraycopy(state.instMemory, 0, stateOrig.instMemory, 0, state.instMemory.length);
            System.arraycopy(state.dataMemory, 0, stateOrig.dataMemory, 0, state.dataMemory.length);
            stateOrig.pc = state.pc;
            System.arraycopy(state.regs, 0, stateOrig.regs, 0, state.regs.length);

            int inst = sim4.getInstruction(state.pc, state.instMemory);

            if (inst != 0) {
                InstructionFields fields = new InstructionFields();
                CPUControl control = new CPUControl();

                sim4.extractInstructionFields(inst, fields);
                sim4.fillCPUControl(fields, control);

                System.out.printf("PC: 0x%04x_%04x   Instruction: 0x%04x_%04x\n",
                        state.pc >> 16, state.pc & 0xffff,
                        inst >> 16, inst & 0xffff);

                sim4_test_commonCode.dumpFields(fields);

                System.out.println("  --");
                System.out.printf("  ALUsrc     =%d\n", control.ALUsrc);
                System.out.printf("  ALU.op     =%d\n", control.ALU.op);
                System.out.printf("  ALU.bNegate=%d\n", control.ALU.bNegate);
                System.out.printf("  memRead    =%d\n", control.memRead);
                System.out.printf("  memWrite   =%d\n", control.memWrite);
                System.out.printf("  memToReg   =%d\n", control.memToReg);
                System.out.printf("  regDst     =%d\n", control.regDst);
                System.out.printf("  regWrite   =%d\n", control.regWrite);
                System.out.printf("  branch     =%d\n", control.branch);
                System.out.printf("  jump       =%d\n", control.jump);
                System.out.println();
            }

            /* was the state struct modified? */
            if (!java.util.Arrays.equals(state.instMemory, stateOrig.instMemory) ||
                    !java.util.Arrays.equals(state.dataMemory, stateOrig.dataMemory) ||
                    state.pc != stateOrig.pc ||
                    !java.util.Arrays.equals(state.regs, stateOrig.regs)) {
                System.out.printf("ERROR: When the testcase called getInstruction() or executeControl() at PC=0x%08x, the 'state' struct was modified.\n", stateOrig.pc);
            }
        }
    }
}
