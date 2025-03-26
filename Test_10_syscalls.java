public class Test_10_syscalls {
    public static void main(String[] args) {
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();
        CPUMemory cpuState = new CPUMemory();

        // fill in the registers and data memory with some default values
        for (int i = 1; i < 32; i++)
            cpuState.regs[i] = 0x01010101 * i;
        for (int i = 1; i < cpuState.dataMemory.length; i++)
            cpuState.dataMemory[i] = 0xffff0000 + (i * 4);

        // addi $v0, $zero, 1      # print_int(1234) - twice
        // addi $a0, $zero, 1234
        // syscall
        // syscall
        //
        // addi $v0, $zero, 1      # print_int(5678)
        // addi $a0, $zero, 5678
        // syscall
        //
        // addi $v0, $zero, 11     # print_char('\n')
        // addi $a0, $zero, 0xa
        // syscall
        //
        // addi $v0, $zero, 4
        // addi $a0, $zero, 0x1000 # print_str("TEST STRING\n")
        // syscall
        //
        // addi $v0, $zero, 10     # sys_exit
        // syscall

        cpuState.pc = 0x0100;

        cpuState.instMemory[0x40] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[0x41] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 1234);
        cpuState.instMemory[0x42] = sim4_test_commonCode.SYSCALL();
        cpuState.instMemory[0x43] = sim4_test_commonCode.SYSCALL();

        cpuState.instMemory[0x44] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[0x45] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 5678);
        cpuState.instMemory[0x46] = sim4_test_commonCode.SYSCALL();

        cpuState.instMemory[0x47] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 11);
        cpuState.instMemory[0x48] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 0xa);
        cpuState.instMemory[0x49] = sim4_test_commonCode.SYSCALL();

        cpuState.instMemory[0x4a] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 4);
        cpuState.instMemory[0x4b] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 0x1000);
        cpuState.instMemory[0x4c] = sim4_test_commonCode.SYSCALL();

        cpuState.instMemory[0x4d] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 10);
        cpuState.instMemory[0x4e] = sim4_test_commonCode.SYSCALL();

        /* set up the string in the data memory */
        // Helper method to combine 4 chars into a word
        cpuState.dataMemory[0x400] = charsToWord('T', 'E', 'S', 'T');
        cpuState.dataMemory[0x401] = charsToWord(' ', 'S', 'T', 'R');
        cpuState.dataMemory[0x402] = charsToWord('I', 'N', 'G', '\n');
        cpuState.dataMemory[0x403] = charsToWord('\0', 0, 0, 0);

        // Convert PC to a one-element array for passing by reference
        int[] pcRef = new int[1];
        pcRef[0] = cpuState.pc;

        // we break out when executeSingleCycleCPU() returns nonzero.
        while (true) {
            int rc = sim4_test_commonCode.executeSingleCycleCPU(
                    cpuState.regs,
                    cpuState.instMemory,
                    cpuState.dataMemory,
                    pcRef,
                    0);  // Debug level 0

            // Update the PC in the state from the reference
            cpuState.pc = pcRef[0];

            if (rc != 0)
                break;
        }
    }

    // Helper method to combine 4 chars into a word (equivalent to CHARS_TO_WORD macro in C)
    private static int charsToWord(int a, int b, int c, int d) {
        return ((d << 24) | (c << 16) | (b << 8) | a);
    }
}