public class CPUMemory {
    // INSTRUCTION & DATA MEMORY
    //
    // It is *NOT* realistic to have the instruction memory and the data
    // memory be two sepearte memories.  See "von Neumann architecture" on
    // Wikipedia to see how real computers work.  But, it simplifies the
    // code for our little toy processor to pretend that they are
    // separate.
    //
    // The instruction memory is READ ONLY.
    //
    // The data memory is read/write.

    public int[]    instMemory;
    public int[]    dataMemory;

    // REGISTERS
    public int      pc;
    public int[]    regs;

    public CPUMemory() {
        instMemory  = new int[1024];    // size of the instruction memory, in words
        dataMemory  = new int[16*1024]; // size of the data        memory, in words
        regs        = new int[34];      // 32 ordinary, plus lo+hi (if you want to support them)
    }
}