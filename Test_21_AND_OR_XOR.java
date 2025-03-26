public class Test_21_AND_OR_XOR {
    public static void main(String[] args) {
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();
        CPUMemory cpuState = new CPUMemory();

        // addi  $s0, $zero, -1
        // addi  $s1, $zero, -1000
        cpuState.instMemory[0x100] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(0), sim4_test_commonCode.REG_ZERO, -1);
        cpuState.instMemory[0x101] = sim4_test_commonCode.ADDI(sim4_test_commonCode.S_REG(1), sim4_test_commonCode.REG_ZERO, -1000);

        // and   $s4, $s0, $s1
        // or    $s5, $s1, $s0
        // xor   $s6, $s0, $s1
        cpuState.instMemory[0x102] = sim4_test_commonCode.AND(sim4_test_commonCode.S_REG(4), sim4_test_commonCode.S_REG(0), sim4_test_commonCode.S_REG(1));
        cpuState.instMemory[0x103] = sim4_test_commonCode.OR(sim4_test_commonCode.S_REG(5), sim4_test_commonCode.S_REG(1), sim4_test_commonCode.S_REG(0));
        cpuState.instMemory[0x104] = sim4_test_commonCode.XOR(sim4_test_commonCode.S_REG(6), sim4_test_commonCode.S_REG(0), sim4_test_commonCode.S_REG(1));

        // print_int($s4)
        cpuState.instMemory[0x105] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[0x106] = sim4_test_commonCode.ADD(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.S_REG(4), sim4_test_commonCode.REG_ZERO);
        cpuState.instMemory[0x107] = sim4_test_commonCode.SYSCALL();

        // print_char('\n')
        cpuState.instMemory[0x108] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 11);
        cpuState.instMemory[0x109] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 0xa);
        cpuState.instMemory[0x10a] = sim4_test_commonCode.SYSCALL();

        // print_int($s5)
        cpuState.instMemory[0x10b] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[0x10c] = sim4_test_commonCode.ADD(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.S_REG(5), sim4_test_commonCode.REG_ZERO);
        cpuState.instMemory[0x10d] = sim4_test_commonCode.SYSCALL();

        // print_char('\n')
        cpuState.instMemory[0x10e] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 11);
        cpuState.instMemory[0x10f] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 0xa);
        cpuState.instMemory[0x110] = sim4_test_commonCode.SYSCALL();

        // print_int($s6)
        cpuState.instMemory[0x111] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[0x112] = sim4_test_commonCode.ADD(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.S_REG(6), sim4_test_commonCode.REG_ZERO);
        cpuState.instMemory[0x113] = sim4_test_commonCode.SYSCALL();

        // print_char('\n')
        cpuState.instMemory[0x114] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 11);
        cpuState.instMemory[0x115] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 0xa);
        cpuState.instMemory[0x116] = sim4_test_commonCode.SYSCALL();

        // sys_exit()
        cpuState.instMemory[0x117] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 10);
        cpuState.instMemory[0x118] = sim4_test_commonCode.SYSCALL();

        cpuState.pc = 0x0400;

        // fill in the registers and data memory
        for (int i = 1; i < 34; i++)
            cpuState.regs[i] = 0x01010101 * i;
        for (int i = 1; i < cpuState.dataMemory.length; i++)
            cpuState.dataMemory[i] = 0xffff0000 + (i * 4);

        // Convert PC to a one-element array for passing by reference
        int[] pcRef = new int[1];
        pcRef[0] = cpuState.pc;

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
}