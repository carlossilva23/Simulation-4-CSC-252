public class Test_13_invalidInstructions {
    public static void main(String[] args) {
        Sim4 sim4 = new Sim4();
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();

        final int COUNT = 4;
        int[] badInstructions = new int[COUNT];

        int[] regs = new int[34];
        for (int i = 0; i < 34; i++)
            regs[i] = 0x01010101 * i;

        badInstructions[0] = sim4_test_commonCode.J_FORMAT(24, 0x3ffffff);
        badInstructions[1] = sim4_test_commonCode.I_FORMAT(60, 31, 21, 0xabcd);
        badInstructions[2] = sim4_test_commonCode.R_FORMAT(14, 1, 2, 3, 19);
        badInstructions[3] = sim4_test_commonCode.R_FORMAT(47, 31, 31, 31, 31);

        for (int i = 0; i < COUNT; i++) {
            InstructionFields fields = new InstructionFields();
            CPUControl control = new CPUControl();

            sim4.extractInstructionFields(badInstructions[i], fields);

            int rsVal = regs[fields.rs];
            int rtVal = regs[fields.rt];

            int ok = sim4.fillCPUControl(fields, control);

            System.out.printf("Bad Instruction: 0x%04x_%04x\n",
                    (badInstructions[i] >> 16) & 0xffff,
                    badInstructions[i] & 0xffff);

            sim4_test_commonCode.dumpFields(fields);

            System.out.println("  --");

            if (ok == 0)
                System.out.println("  fill_CPUControl() returned 0, as expected.");
            else
                System.out.println("*** fill_CPUControl() returned nonzero, even though the instruction was invalid! ***");

            sim4_test_commonCode.dumpControl(rsVal, rtVal, control);

            System.out.println();
        }
    }
}