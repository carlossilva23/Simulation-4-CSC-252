public class Test_07_fullInstructions_disabled_opcode_field_2 {
    public static void main(String[] args) {
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();
        CPUMemory state = new CPUMemory();

        /* this contains all of the instructions from tests 02 and 03 */
        state.instMemory[0] = sim4_test_commonCode.ADDU(1, 0, 2);
        state.instMemory[1] = sim4_test_commonCode.SUBU(3, 4, 5);
        state.instMemory[2] = sim4_test_commonCode.ADDIU(6, 7, 0x1234);
        state.instMemory[3] = sim4_test_commonCode.AND(9, 10, 11);

        // syscall 10 (exit)
        state.instMemory[4] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 10);
        state.instMemory[5] = sim4_test_commonCode.SYSCALL();

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
                    3);  // Debug level 3

            // Update the PC in the state from the reference
            state.pc = pcRef[0];

            if (rc != 0)
                break;
        }
    }
}