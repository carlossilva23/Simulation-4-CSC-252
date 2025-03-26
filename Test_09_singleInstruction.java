public class Test_09_singleInstruction {
    public static void main(String[] args) {
        Sim4_test_commonCode sim4_test_commonCode = new Sim4_test_commonCode();
        CPUMemory cpuState = new CPUMemory();

        cpuState.instMemory[0x40] = sim4_test_commonCode.ADD(sim4_test_commonCode.T_REG(0), sim4_test_commonCode.S_REG(3), sim4_test_commonCode.S_REG(4));

        cpuState.pc = 0x0100;

        // fill in the registers and data memory with some default values
        for (int i = 1; i < 32; i++)
            cpuState.regs[i] = 0x01010101 * i;
        for (int i = 1; i < cpuState.dataMemory.length; i++)
            cpuState.dataMemory[i] = 0xffff0000 + (i * 4);

        // Convert PC to a one-element array for passing by reference
        int[] pcRef = new int[1];
        pcRef[0] = cpuState.pc;

        sim4_test_commonCode.executeSingleCycleCPU(
                cpuState.regs,
                cpuState.instMemory,
                cpuState.dataMemory,
                pcRef,
                2);  // Debug level 2

        // Update the PC in the state from the reference
        cpuState.pc = pcRef[0];
    }
}