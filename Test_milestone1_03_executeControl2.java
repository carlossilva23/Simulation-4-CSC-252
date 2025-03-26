public class Test_milestone1_03_executeControl2 {
    public static void main(String[] args) {
        CPUMemory               state                  = new CPUMemory();
        Sim4                    sim4                   = new Sim4();
        Sim4_test_commonCode    sim4_test_commonCode   = new Sim4_test_commonCode();

        state.instMemory[0]     = sim4_test_commonCode.ADDU (1, 0, 2);
        state.instMemory[1]     = sim4_test_commonCode.SUBU (3, 4, 5);
        state.instMemory[2]     = sim4_test_commonCode.ADDIU(6, 7, 0x1234);
        state.instMemory[3]     = sim4_test_commonCode.AND  (9, 10, 11);
        state.instMemory[4]     = sim4_test_commonCode.OR   (12, 13, 14);
        state.instMemory[5]     = sim4_test_commonCode.XOR  (15, 16, 17);
//        state.instMemory[6]     = sim4_test_commonCode.ORI  (18, 19, 0x5678);
        state.instMemory[7]     = sim4_test_commonCode.SLT  (21, 22, 23);
        state.instMemory[8]     = sim4_test_commonCode.SLTI (24, 25, -1);
        state.instMemory[9]     = sim4_test_commonCode.LW   (26, 0, 104);
        state.instMemory[10]    = sim4_test_commonCode.SW   (29, 0, 108);
        state.instMemory[11]    = sim4_test_commonCode.BEQ  (1, 0, 0x0104);
        state.instMemory[12]    = sim4_test_commonCode.J    (0x01234567);
        state.instMemory[13]    = sim4_test_commonCode.J    (0x03FFFFFF);
        
        

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