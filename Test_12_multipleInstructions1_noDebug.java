public class Test_12_multipleInstructions1_noDebug {
    public static void main(String[] args) {
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();
        CPUMemory cpuState = new CPUMemory();

        // fill in the registers and data memory with some default values
        for (int i = 1; i < 32; i++)
            cpuState.regs[i] = 0x01010101 * i;
        for (int i = 1; i < cpuState.dataMemory.length; i++)
            cpuState.dataMemory[i] = 0xffff0000 + (i * 4);

        // num   = 10;
        // count = 5;
        // step  = 3;
        // for (int i=0; i<count; i++)
        // {
        // num += step;
        // printf("%d\n", num);
        // }
        //
        // ---------------------
        //
        // addi   $t0, $zero, 0      # i     = 0
        // addi   $t1, $zero, 10     # num   = 10
        // addi   $t2, $zero, 5      # count = 5
        // addi   $t4, $zero, 3      # step  = 3
        //
        // RANGE_LOOP:
        // slt    $t3, $t0, $t2
        // beq    $t3, $zero, RANGE_LOOP_END
        //
        // add    $t1, $t1, $t4      # num += step
        //
        // addi   $v0, $zero, 1      # print_int(num)
        // addi   $a0, $t1, $zero
        // syscall
        //
        // addi   $v0, $zero, 11     # print_char('\n')
        // addi   $a0, $zero, 0xa
        // syscall
        //
        // addi   $t0, $t0, 1        # i++
        // j      RANGE_LOOP
        //
        // RANGE_LOOP_END:
        // addi   $v0, $zero, 10     # sys_exit
        // syscall

        cpuState.pc = 0x000;

        // init
        cpuState.instMemory[0] = sim4_test_commonCode.ADDI(sim4_test_commonCode.T_REG(0), sim4_test_commonCode.REG_ZERO, 0);
        cpuState.instMemory[1] = sim4_test_commonCode.ADDI(sim4_test_commonCode.T_REG(1), sim4_test_commonCode.REG_ZERO, 10);
        cpuState.instMemory[2] = sim4_test_commonCode.ADDI(sim4_test_commonCode.T_REG(2), sim4_test_commonCode.REG_ZERO, 5);
        cpuState.instMemory[3] = sim4_test_commonCode.ADDI(sim4_test_commonCode.T_REG(4), sim4_test_commonCode.REG_ZERO, 3);

        // RANGE_LOOP:
        cpuState.instMemory[4] = sim4_test_commonCode.SLT(sim4_test_commonCode.T_REG(3), sim4_test_commonCode.T_REG(0), sim4_test_commonCode.T_REG(2));
        cpuState.instMemory[5] = sim4_test_commonCode.BEQ(sim4_test_commonCode.T_REG(3), sim4_test_commonCode.REG_ZERO, 9);

        // num += step
        cpuState.instMemory[6] = sim4_test_commonCode.ADD(sim4_test_commonCode.T_REG(1), sim4_test_commonCode.T_REG(1), sim4_test_commonCode.T_REG(4));

        // print_int(num)
        cpuState.instMemory[7] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 1);
        cpuState.instMemory[8] = sim4_test_commonCode.ADD(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.T_REG(1), sim4_test_commonCode.REG_ZERO);
        cpuState.instMemory[9] = sim4_test_commonCode.SYSCALL();

        // print_char('\n')
        cpuState.instMemory[10] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 11);
        cpuState.instMemory[11] = sim4_test_commonCode.ADDI(sim4_test_commonCode.A_REG(0), sim4_test_commonCode.REG_ZERO, 0xa);
        cpuState.instMemory[12] = sim4_test_commonCode.SYSCALL();

        // i++
        cpuState.instMemory[13] = sim4_test_commonCode.ADDI(sim4_test_commonCode.T_REG(0), sim4_test_commonCode.T_REG(0), 1);

        // j RANGE_LOOP
        cpuState.instMemory[14] = sim4_test_commonCode.J(4);

        // RANGE_LOOP_END
        cpuState.instMemory[15] = sim4_test_commonCode.ADDI(sim4_test_commonCode.V_REG(0), sim4_test_commonCode.REG_ZERO, 10);
        cpuState.instMemory[16] = sim4_test_commonCode.SYSCALL();

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
                    0);  // Debug level 0 (no debug output)

            // Update the PC in the state from the reference
            cpuState.pc = pcRef[0];

            if (rc != 0)
                break;
        }
    }
}