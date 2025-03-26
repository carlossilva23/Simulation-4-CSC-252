public class Test_03_executeControl2 {
    public static void main(String[] args) {
        CPUMemory state = new CPUMemory();
        Sim4 sim4 = new Sim4();
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();

        // Initialize instruction memory with various MIPS instructions
        state.instMemory[0] = sim4_test_commonCode.ADDU(1, 0, 2);
        state.instMemory[1] = sim4_test_commonCode.SUBU(3, 4, 5);
        state.instMemory[2] = sim4_test_commonCode.ADDIU(6, 7, 0x1234);
        state.instMemory[3] = sim4_test_commonCode.AND(9, 10, 11);
        state.instMemory[4] = sim4_test_commonCode.OR(12, 13, 14);
        state.instMemory[5] = sim4_test_commonCode.XOR(15, 16, 17);
        // state.instMemory[6] = sim4_test_commonCode.ORI(18, 19, 0x9abc);  // Commented out in original
        state.instMemory[7] = sim4_test_commonCode.SLT(21, 22, 23);
        state.instMemory[8] = sim4_test_commonCode.SLTI(24, 25, -1);
        state.instMemory[9] = sim4_test_commonCode.LW(26, 0, 104);
        state.instMemory[10] = sim4_test_commonCode.SW(29, 0, 108);
        state.instMemory[11] = sim4_test_commonCode.BEQ(1, 0, 0x0104);
        state.instMemory[12] = sim4_test_commonCode.J(0x01234567);
        state.instMemory[13] = sim4_test_commonCode.J(0x03ffffff);

        // Iterate through possible PC values
        for (state.pc = 0x0000; state.pc < 0x1000; state.pc += 4) {
            /* save the original state, to see if the student changes it */
            CPUMemory stateOrig = new CPUMemory();
            System.arraycopy(state.instMemory, 0, stateOrig.instMemory, 0, state.instMemory.length);
            System.arraycopy(state.dataMemory, 0, stateOrig.dataMemory, 0, state.dataMemory.length);
            stateOrig.pc = state.pc;
            System.arraycopy(state.regs, 0, stateOrig.regs, 0, state.regs.length);

            // Get the instruction at current PC
            int inst = sim4.getInstruction(state.pc, state.instMemory);

            if (inst != 0) {
                InstructionFields fields = new InstructionFields();
                CPUControl control = new CPUControl();

                // Extract fields from instruction
                sim4.extractInstructionFields(inst, fields);

                // Get register values (using original state to avoid any modifications)
                int rsVal = stateOrig.regs[fields.rs];
                int rtVal = stateOrig.regs[fields.rt];

                // Fill in CPU control signals
                sim4.fillCPUControl(fields, control);

                // Display instruction, fields, and control signals
                System.out.printf("PC: 0x%04x_%04x   Instruction: 0x%04x_%04x\n",
                        (state.pc >> 16) & 0xFFFF, state.pc & 0xFFFF,
                        (inst >> 16) & 0xFFFF, inst & 0xFFFF);
                sim4_test_commonCode.dumpFields(fields);
                sim4_test_commonCode.dumpControl(rsVal, rtVal, control);
                System.out.println();
            }

            // Check if state was modified
            if (!java.util.Arrays.equals(state.instMemory, stateOrig.instMemory) ||
                    !java.util.Arrays.equals(state.dataMemory, stateOrig.dataMemory) ||
                    state.pc != stateOrig.pc ||
                    !java.util.Arrays.equals(state.regs, stateOrig.regs)) {
                System.out.printf("ERROR: When the testcase called getInstruction() or executeControl() at PC=0x%08x, the 'state' struct was modified.\n", stateOrig.pc);
            }
        }
    }
}