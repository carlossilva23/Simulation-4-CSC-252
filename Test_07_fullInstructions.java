public class Test_07_fullInstructions {
    public static void main(String[] args) {
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();
        CPUMemory state = new CPUMemory();

        /* this contains all of the instructions from tests 02 and 03 */
        state.instMemory[0] = sim4_test_commonCode.ADD(sim4_test_commonCode.S_REG(0), sim4_test_commonCode.S_REG(1), sim4_test_commonCode.S_REG(2));
        state.instMemory[1] = sim4_test_commonCode.SUB(sim4_test_commonCode.S_REG(3), sim4_test_commonCode.S_REG(4), sim4_test_commonCode.S_REG(5));
        state.instMemory[2] = sim4_test_commonCode.ADDI(sim4_test_commonCode.T_REG(2), sim4_test_commonCode.T_REG(3), -1);
        state.instMemory[3] = sim4_test_commonCode.ADDI(sim4_test_commonCode.T_REG(4), sim4_test_commonCode.T_REG(5), 16);
        state.instMemory[4] = sim4_test_commonCode.ADDU(1, 0, 2);
        state.instMemory[5] = sim4_test_commonCode.SUBU(3, 4, 5);
        state.instMemory[6] = sim4_test_commonCode.ADDIU(6, 7, 0x1234);
        state.instMemory[7] = sim4_test_commonCode.AND(9, 10, 11);
        state.instMemory[8] = sim4_test_commonCode.OR(12, 13, 14);
        state.instMemory[9] = sim4_test_commonCode.XOR(15, 16, 17);
        state.instMemory[10] = sim4_test_commonCode.SLT(21, 22, 23);
        state.instMemory[11] = sim4_test_commonCode.SLTI(24, 25, -1);
        state.instMemory[12] = sim4_test_commonCode.LW(26, 0, 104);
        state.instMemory[13] = sim4_test_commonCode.SW(29, 0, 108);

        // this branch should be taken
        state.instMemory[14] = sim4_test_commonCode.BEQ(sim4_test_commonCode.T_REG(2), sim4_test_commonCode.T_REG(2), 1);

        // this instruction should not run, ever
        state.instMemory[15] = sim4_test_commonCode.ADD(sim4_test_commonCode.S_REG(0), sim4_test_commonCode.S_REG(1), sim4_test_commonCode.S_REG(2));

        // this is the destination of the BEQ above

        // this branch should *NOT* be taken
        state.instMemory[16] = sim4_test_commonCode.BEQ(sim4_test_commonCode.T_REG(2), sim4_test_commonCode.REG_ZERO, -10);

        // syscall 10 (exit)
        state.instMemory[17] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 10);
        state.instMemory[18] = sim4_test_commonCode.SYSCALL();

        /* fill in all of the registers, so that we can figure out if we're
         * reading the right values.
         */
        for (int i = 0; i < 32; i++)
            state.regs[i] = 0x01010101 * i;

        /* fill in the memory slots, similarly.
         *
         * NOTE: I used to use 0x00010001*i as the constant here, but that
         *       sometimes lead to weird situations where a student would
         *       read the wrong address but then mistakenly see the correct
         *       value.  This version is less error-prone.
         */
        for (int i = 0; i < state.dataMemory.length; i++)
            state.dataMemory[i] = 0xffff0000 + (i * 4);

        // Convert PC to a one-element array for passing by reference
        int[] pcRef = new int[1];
        pcRef[0] = state.pc;

        // we break out when executeSingleCycleCPU() returns nonzero.
        while (true) {
            int rc = sim4_test_commonCode.executeSingleCycleCPU(
                    state.regs,
                    state.instMemory,
                    state.dataMemory,
                    pcRef,
                    2);  // Debug level 2

            // Update the PC in the state from the reference
            state.pc = pcRef[0];

            if (rc != 0)
                break;
        }
    }
}