public class Test_milestone1_02_executeControl1 {
    public static void main(String[] args) {
        CPUMemory state = new CPUMemory();
        Sim4 sim4 = new Sim4();
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();

        state.instMemory[0]     = sim4_test_commonCode.ADD  (sim4_test_commonCode.S_REG(0), sim4_test_commonCode.S_REG(1), sim4_test_commonCode.S_REG(2));
        state.instMemory[1]     = sim4_test_commonCode.SUB  (sim4_test_commonCode.S_REG(3), sim4_test_commonCode.S_REG(4), sim4_test_commonCode.S_REG(5));
        state.instMemory[256]   = sim4_test_commonCode.ADDI (sim4_test_commonCode.T_REG(2), sim4_test_commonCode.T_REG(3), -1);
        state.instMemory[512]   = sim4_test_commonCode.ADDI (sim4_test_commonCode.T_REG(4), sim4_test_commonCode.T_REG(5), 16);

        for (int i = 0; i < 0x400; i++) {
            int inst = state.instMemory[i];

            if (inst != 0) {
                InstructionFields   fields    = new InstructionFields();
                CPUControl          control   = new CPUControl();

                sim4.extractInstructionFields(inst, fields);
                sim4.fillCPUControl(fields, control);

                System.out.printf("Instruction: 0x%04x_%04x\n",
                        (inst >> 16) & 0xFFFF, inst & 0xFFFF);

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
        }
    }
}