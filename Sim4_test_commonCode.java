import java.util.Arrays;

public class Sim4_test_commonCode {
    /* these macros are useful for encoding instructions
     *
     * The first few are macros that allow us to generate some register
     * numbers.
     */
    public int S_REG (int x) { return x + 16; }
    public int T_REG (int x) { return (x < 8) ? (x + 8) : (x - 8 + 24); }    // t0 is 8; t8 is 24
    public int A_REG (int x) { return x + 4; }
    public int V_REG (int x) { return x + 2; }

    public int          RA_REG      = 31;
    public final int    SP_REG      = 29;
    public final int    FP_REG      = 30;
    public final int    REG_ZERO    = 0;

    /* The next four are macros that represent the three instruction formats.
     *
     * Note that all of the R-format instructions use R_FORMAT(); R_FORMAT_x()
     * is used only by MUL, which has a different opcode.
     */
    public int R_FORMAT(int funct, int rs, int rt, int rd, int shamt) {
        return R_FORMAT_X(0x00, funct, rs, rt, rd, shamt);
    }

    public int R_FORMAT_X(int opcode, int funct, int rs, int rt, int rd, int shamt) {
        return ((opcode & 0x3F) << 26) |
                ((rs    & 0x1F) << 21) |
                ((rt    & 0x1F) << 16) |
                ((rd    & 0x1F) << 11) |
                ((shamt & 0x1F) << 6)  |
                ((funct & 0x3F) << 0);
    }

    public int I_FORMAT(int opcode, int rs, int rt, int imm16) {
        return ((opcode & 0x3F) << 26) |
                ((rs    & 0x1F) << 21) |
                ((rt    & 0x1F) << 16) |
                (imm16  & 0xFFFF);
    }

    public int J_FORMAT(int opcode, int addr26) {
        return ((opcode & 0x3F) << 26) |
                (addr26 & 0x3FFFFFF);
    }

    /* these macros encode various instructions.  Each uses the format macros
     * above to actually do the encoding.
     */
    public final int NOP = 0;   // a 32-bit 0 constant is NOP in MIPS!

    public int ADD  (int rd, int rs, int rt)    { return R_FORMAT(32, rs, rt, rd, 0); }
    public int ADDU (int rd, int rs, int rt)    { return R_FORMAT(33, rs, rt, rd, 0); }
    public int SUB  (int rd, int rs, int rt)    { return R_FORMAT(34, rs, rt, rd, 0); }
    public int SUBU (int rd, int rs, int rt)    { return R_FORMAT(35, rs, rt, rd, 0); }
    public int ADDI (int rt, int rs, int imm16) { return I_FORMAT(8, rs, rt, imm16); }
    public int ADDIU(int rt, int rs, int imm16) { return I_FORMAT(9, rs, rt, imm16); }

    public int MUL  (int rd, int rs, int rt){ return R_FORMAT_X (0x1C, 2, rs, rt, rd, 0); }
    public int MULT (int rs, int rt)        { return R_FORMAT   (24, rs, rt, 0, 0); }
    public int MULTU(int rs, int rt)        { return R_FORMAT   (25, rs, rt, 0, 0); }
    public int DIV  (int rs, int rt)        { return R_FORMAT   (26, rs, rt, 0, 0); }
    public int DIVU (int rs, int rt)        { return R_FORMAT   (27, rs, rt, 0, 0); }

    public int MFHI (int rd) { return R_FORMAT(16, 0, 0, rd, 0); }
    public int MFLO (int rd) { return R_FORMAT(18, 0, 0, rd, 0); }

    public int AND  (int rd, int rs, int rt)    { return R_FORMAT(36, rs, rt, rd, 0); }
    public int OR   (int rd, int rs, int rt)    { return R_FORMAT(37, rs, rt, rd, 0); }
    public int XOR  (int rd, int rs, int rt)    { return R_FORMAT(38, rs, rt, rd, 0); }
    public int NOR  (int rd, int rs, int rt)    { return R_FORMAT(39, rs, rt, rd, 0); }
    public int ANDI (int rt, int rs, int imm16) { return I_FORMAT(12, rs, rt, imm16); }
    public int ORI  (int rt, int rs, int imm16) { return I_FORMAT(13, rs, rt, imm16); }
    public int XORI (int rt, int rs, int imm16) { return I_FORMAT(14, rs, rt, imm16); }

    public int LUI  (int rt, int imm16) { return I_FORMAT(15, 0, rt, imm16); }

    public int SLT  (int rd, int rs, int rt)    { return R_FORMAT(42, rs, rt, rd, 0); }
    public int SLTU (int rd, int rs, int rt)    { return R_FORMAT(43, rs, rt, rd, 0); }
    public int SLTI (int rt, int rs, int imm16) { return I_FORMAT(10, rs, rt, imm16); }
    public int SLTIU(int rt, int rs, int imm16) { return I_FORMAT(11, rs, rt, imm16); }

    public int SLL  (int rd, int rt, int shamt) { return R_FORMAT(0, 0, rt, rd, shamt); }
    public int SRL  (int rd, int rt, int shamt) { return R_FORMAT(2, 0, rt, rd, shamt); }
    public int SRA  (int rd, int rt, int shamt) { return R_FORMAT(3, 0, rt, rd, shamt); }
    public int SLLV (int rd, int rt, int rs)    { return R_FORMAT(4, rs, rt, rd, 0); }
    public int SRLV (int rd, int rt, int rs)    { return R_FORMAT(6, rs, rt, rd, 0); }
    public int SRAV (int rd, int rt, int rs)    { return R_FORMAT(7, rs, rt, rd, 0); }

    public int LW   (int rt, int rs, int imm16) { return I_FORMAT(35, rs, rt, imm16); }
    public int SW   (int rt, int rs, int imm16) { return I_FORMAT(43, rs, rt, imm16); }

    public int LB   (int rt, int rs, int imm16) { return I_FORMAT(32, rs, rt, imm16); }
    public int SB   (int rt, int rs, int imm16) { return I_FORMAT(40, rs, rt, imm16); }

    public int BEQ  (int rs, int rt, int imm16) { return I_FORMAT(4, rs, rt, imm16); }
    public int BNE  (int rs, int rt, int imm16) { return I_FORMAT(5, rs, rt, imm16); }
    public int J    (int address)               { return J_FORMAT(2, address); }

    public int JAL  (int address)   { return J_FORMAT(3, address); }
    public int JR   (int rs)        { return R_FORMAT(8, rs, 0, 0, 0); }

    public int SYSCALL() { return R_FORMAT(12, 0, 0, 0, 0); }


    public void dumpPCInstruction(int pc, int inst) {
        System.out.printf("PC: 0x%04x_%04x   Instruction: 0x%04x_%04x\n",
                (pc >> 16) & 0xFFFF, pc & 0xFFFF,
                (inst >> 16) & 0xFFFF, inst & 0xFFFF);
    }

    public void dumpFields(InstructionFields fields) {
        System.out.println("  --");
        System.out.printf("  opcode  (6 bits)=0x%02x\n", fields.opcode);
        System.out.printf("  rs      (5 bits)=0x%02x\n", fields.rs);
        System.out.printf("  rt      (5 bits)=0x%02x\n", fields.rt);
        System.out.printf("  rd      (5 bits)=0x%02x\n", fields.rd);
        System.out.printf("  shamt   (5 bits)=0x%02x\n", fields.shamt);
        System.out.printf("  funct   (6 bits)=0x%02x\n", fields.funct);
        System.out.printf("  imm16  (16 bits)=0x     %04x\n", fields.imm16);
        System.out.printf("  imm32  (32 bits)=0x%04x_%04x\n",
                (fields.imm32 >> 16) & 0xFFFF, fields.imm32 & 0xFFFF);
        System.out.printf("  addr   (26 bits)=0x %03x_%04x\n",
                (fields.address >> 16) & 0xFFFF, fields.address & 0xFFFF);
    }

    public void dumpControl(int rsVal, int rtVal, CPUControl control) {
        System.out.println("  --");
        System.out.printf("  rsVal=0x%04x_%04x\n", (rsVal >> 16) & 0xFFFF, rsVal & 0xFFFF);
        System.out.printf("  rtVal=0x%04x_%04x\n", (rtVal >> 16) & 0xFFFF, rtVal & 0xFFFF);
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
    }

    public void dumpControlAluInputsOnly(InstructionFields fields, CPUControl control) {
        System.out.printf("  rs      (5 bits)=0x%02x\n", fields.rs);
        System.out.printf("  rt      (5 bits)=0x%02x\n", fields.rt);
        System.out.printf("  imm16  (16 bits)=0x     %04x\n", fields.imm16);
        System.out.printf("  imm32  (32 bits)=0x%04x_%04x\n",
                (fields.imm32 >> 16) & 0xFFFF, fields.imm32 & 0xFFFF);
        System.out.printf("  ALUsrc     =%d\n", control.ALUsrc);
    }

    public void dumpControlAluCalcOnly(InstructionFields fields, CPUControl control) {
        dumpControlAluInputsOnly(fields, control);
        System.out.printf("  ALU.op     =%d\n", control.ALU.op);
        System.out.printf("  ALU.bNegate=%d\n", control.ALU.bNegate);
    }

    public int executeSingleCycleCPU(int[] regs, int[] instMemory, int[] dataMemory, int[] pc, int debug) {
        int i;
        Sim4 sim4 = new Sim4();
        /* these are several variables which are used in the actual logic of
         * the code below.
         */
        int inst;
        InstructionFields fields = new InstructionFields();
        int rsVal, rtVal, reg32, reg33;
        CPUControl control = new CPUControl();
        int aluInput1, aluInput2;
        ALUResult aluResult = new ALUResult();
        MemResult memResult = new MemResult();

        /* set this to nonzero to indicate that the program should end */
        int fail = 0;

        /* we make duplicates all 4 of the structs (the parameter, plus the
         * three result structs) so that we can later compare them.  The
         * result structs should be *ENTIRELY* unmodified, since they are set
         * up once, and then never changed.  The 'state' variable should
         * change, but only in a few places.
         */
        int pc_save;
        int[] regs_save     = new int[34];
        int[] dataMem_save  = new int[dataMemory.length];

        InstructionFields fields_save   = new InstructionFields();
        CPUControl control_save         = new CPUControl();
        ALUResult aluResult_save        = new ALUResult();
        MemResult memResult_save        = new MemResult();

        pc_save = pc[0];
        System.arraycopy(regs, 0, regs_save, 0, regs.length);
        System.arraycopy(dataMemory, 0, dataMem_save, 0, dataMemory.length);

        // STEP 1: read the instruction from memory
        inst = sim4.getInstruction(pc[0], instMemory);

        // is this a system call?  For those, we simply skip over the student
        // implementation.
        if (inst == SYSCALL()) {
            if (debug > 0) System.out.print("[systemCall]");
            pc[0] += 4;
            return execSyscall(regs, instMemory, dataMemory, pc, debug);
        }

        // STEP 2: decode the instruction
        sim4.extractInstructionFields(inst, fields);
        fields_save         = new InstructionFields();
        fields_save.opcode  = fields.opcode;
        fields_save.rs      = fields.rs;
        fields_save.rt      = fields.rt;
        fields_save.rd      = fields.rd;
        fields_save.shamt   = fields.shamt;
        fields_save.funct   = fields.funct;
        fields_save.imm16   = fields.imm16;
        fields_save.imm32   = fields.imm32;
        fields_save.address = fields.address;

        rsVal = regs[fields.rs];
        rtVal = regs[fields.rt];
        reg32 = regs[32];
        reg33 = regs[33];

        int ok                      = sim4.fillCPUControl(fields, control);
        control_save                = new CPUControl();
        control_save.ALUsrc         = control.ALUsrc;
        control_save.ALU.op         = control.ALU.op;
        control_save.ALU.bNegate    = control.ALU.bNegate;
        control_save.memRead        = control.memRead;
        control_save.memWrite       = control.memWrite;
        control_save.memToReg       = control.memToReg;
        control_save.regDst         = control.regDst;
        control_save.regWrite       = control.regWrite;
        control_save.branch         = control.branch;
        control_save.jump           = control.jump;
        control_save.extra1         = control.extra1;
        control_save.extra2         = control.extra2;
        control_save.extra3         = control.extra3;

        if (debug > 0) {
            System.out.printf("Opcode: 0x%02x   at 0x%04x_%04x\n", fields.opcode, (pc[0] >> 16) & 0xffff, pc[0] & 0xffff);
            if (fields.opcode == 0) System.out.printf("  funct: 0x%02x\n", fields.funct);
        }

        if (ok == 0) {
            System.out.println("fill_CPUControl() returned 0");
            return 1;
        }

        if (debug >= 2) {
            dumpFields(fields);
            dumpControl(rsVal, rtVal, control);
        }

        // remove these fields, so that they cannot be used by student code
        // in later steps.
        fields      .opcode = -1;
        fields_save .opcode = -1;
        fields      .funct  = -1;
        fields_save .funct  = -1;

        // STEP 3: If this isn't a syscall, then do our normal calculations.
        // What do we drive into the ALU?
        aluInput1 = sim4.getALUinput1(control, fields, rsVal, rtVal, reg32, reg33, pc_save);
        aluInput2 = sim4.getALUinput2(control, fields, rsVal, rtVal, reg32, reg33, pc_save);

        sim4.executeALU(control, aluInput1, aluInput2, aluResult);
        aluResult_save          = new ALUResult();
        aluResult_save.result   = aluResult.result;
        aluResult_save.zero     = aluResult.zero;
        aluResult_save.extra    = aluResult.extra;

        if (debug >= 2) {
            System.out.println  ("  --");
            System.out.printf   ("  ALU.result=0x%04x_%04x\n", (aluResult.result >> 16) & 0xffff, aluResult.result & 0xffff);
            System.out.printf   ("  ALU.zero  =%d\n", aluResult.zero);
        }

        // STEP 4: handle the memory operations (if any)
        sim4.executeMEM(control, aluResult, rsVal, rtVal, dataMemory, memResult);
        memResult_save          = new MemResult();
        memResult_save.readVal  = memResult.readVal;

        if (debug >= 2) {
            System.out.println("  --");
            System.out.printf("  memResult.readVal=0x%04x_%04x\n", (memResult.readVal >> 16) & 0xffff, memResult.readVal & 0xffff);
            System.out.println("  --");
        }

        // STEP 5: update the PC
        pc[0] = sim4.getNextPC(fields, control, aluResult.zero, rsVal, rtVal, pc[0]);
        if (pc[0] == pc_save) {
            System.out.println("ERROR: The PC did not change!");
            fail = 1;
        }

        if ((pc[0] & 0x3) != 0) {
            System.out.printf("ERROR: The new Program Counter 0x%04x_%04x is not a multiple of 4!\n", (pc[0] >> 16) & 0xffff, pc[0] & 0xffff);
            return 1;
        }

        // STEP 6: update registers
        sim4.executeUpdateRegs(fields, control, aluResult, memResult, regs);

        // AFTER THE INSTRUCTION: COMPARE THE VARIOUS STRUCTS

        // Ensure that instruction fields have not changed after execution
        /* control should not have changed - except that we won't compare
         * the 'extra' fields
         */
        if (fields_save.opcode != fields.opcode || fields_save.funct != fields.funct) {
            System.out.println("ERROR: The Instruction Fields struct changed somewhere in the testcase, after the original call to extractInstructionFields().");
        }

        // Ensure control structure remains unchanged (except for extra fields)
        if (control_save.ALUsrc != control.ALUsrc ||
                control_save.ALU.op != control.ALU.op ||
                control_save.ALU.bNegate != control.ALU.bNegate ||
                control_save.memRead != control.memRead ||
                control_save.memWrite != control.memWrite ||
                control_save.memToReg != control.memToReg ||
                control_save.regDst != control.regDst ||
                control_save.regWrite != control.regWrite ||
                control_save.branch != control.branch ||
                control_save.jump != control.jump) {
            System.out.println("ERROR: The Control struct changed somewhere in the testcase, after the original call to fillCPUControl().");
        }

        // Ensure ALU result remains unchanged
        if (aluResult_save.result != aluResult.result || aluResult_save.zero != aluResult.zero) {
            System.out.println("ERROR: The ALUResult struct changed somewhere in the testcase, after the original call to executeALU().");
        }

        // Ensure memory read result remains unchanged
        if (memResult_save.readVal != memResult.readVal) {
            System.out.println("ERROR: The MemResult struct changed somewhere in the testcase, after the original call to executeMEM().");
        }

        /* help students debug spurious changes to $zero.  This would have
         * helped me debug my own code, if it had existed!   - Russ
         */
        if (regs[0] != 0) {
            System.out.printf("ERROR: The $zero register was changed to 0x%08x\n", regs[0]);
        }

        /* changes to the CPUMemory struct are not errors.  At least, not
         * necessarily
         */
        if (debug > 0) {
            System.out.printf("  PC was: 0x%04x_%04x is: 0x%04x_%04x\n",
                    (pc_save >> 16) & 0xFFFF, pc_save & 0xFFFF,
                    (pc[0] >> 16) & 0xFFFF, pc[0] & 0xFFFF);

            for (i = 0; i < 34; i++) {
                if (regs_save[i] != regs[i]) {
                    System.out.printf("  reg[%2d] was: 0x%04x_%04x is 0x%04x_%04x\n",
                            i,
                            (regs_save[i] >> 16) & 0xFFFF, regs_save[i] & 0xFFFF,
                            (regs[i] >> 16) & 0xFFFF, regs[i] & 0xFFFF);
                }
            }

            for (i = 0; i < dataMem_save.length; i++) {
                if (dataMem_save[i] != dataMemory[i]) {
                    System.out.printf("  mem[%2d] was: 0x%04x_%04x is 0x%04x_%04x\n",
                            i,
                            (dataMem_save[i] >> 16) & 0xFFFF, dataMem_save[i] & 0xFFFF,
                            (dataMemory[i] >> 16) & 0xFFFF, dataMemory[i] & 0xFFFF);
                }
            }
            System.out.println();
        }

        return fail;
    }

    public int execSyscall(int[] regs, int[] instMemory, int[] dataMemory, int[] pc, int debug) {
        int v0 = regs[2];
        int a0 = regs[4];

        // syscall 10: exit
        if (v0 == 10) {
            System.out.println("--- syscall 10 executed: Normal termination of the assembly language program.");
            return 1;
        }

        // syscall 1: print_int
        if (v0 == 1) {
            System.out.printf("%d", a0);
        }
        // syscall 11: print_char
        else if (v0 == 11) {
            System.out.printf("%c", a0);
        }
        // syscall 4: print_str
        else if (v0 == 4) {
            // Extract string from memory starting at byte address a0
            System.out.print(extractString(dataMemory, a0));
        }
        // unrecognized syscall
        else {
            System.out.printf("--- ERROR: Unrecognized syscall $v0=%d\n", v0);
        }

        return 0;
    }

    /**
     * Helper method to extract a null-terminated string from memory.
     * In MIPS memory, each word contains 4 bytes/characters.
     */
    private String extractString(int[] memory, int byteAddress) {
        StringBuilder sb = new StringBuilder();
        int wordIndex = byteAddress / 4;  // Convert byte address to word index
        int byteOffset = byteAddress % 4; // Get offset within the word

        while (wordIndex < memory.length) {
            int word = memory[wordIndex++];

            // Extract characters one by one, starting from byteOffset
            for (int i = byteOffset; i < 4; i++) {
                char c = (char)((word >> (i * 8)) & 0xFF);
                if (c == '\0') {
                    return sb.toString(); // Stop at null terminator
                }
                sb.append(c);
            }

            byteOffset = 0; // Reset offset after first word
        }

        return sb.toString();
    }
}