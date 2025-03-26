public class Test_06_execMEM {
    public static void main(String[] args) {
        Sim4 sim4 = new Sim4();
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
        // state.instMemory[10] = sim4_test_commonCode.ORI(18, 19, 0x9abc);  // Commented out in original
        state.instMemory[11] = sim4_test_commonCode.SLT(21, 22, 23);
        state.instMemory[12] = sim4_test_commonCode.SLTI(24, 25, -1);
        state.instMemory[13] = sim4_test_commonCode.LW(26, 0, 104);
        state.instMemory[14] = sim4_test_commonCode.SW(29, 0, 108);
        state.instMemory[15] = sim4_test_commonCode.BEQ(1, 0, 0x0104);
        state.instMemory[16] = sim4_test_commonCode.J(0x01234567);
        state.instMemory[17] = sim4_test_commonCode.J(0x03ffffff);

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

        for (state.pc = 0x0000; state.pc < 0x1000; state.pc += 4) {
            /* save the original state, to see if the student changes it */
            CPUMemory stateOrig = new CPUMemory();
            System.arraycopy(state.instMemory, 0, stateOrig.instMemory, 0, state.instMemory.length);
            System.arraycopy(state.dataMemory, 0, stateOrig.dataMemory, 0, state.dataMemory.length);
            stateOrig.pc = state.pc;
            System.arraycopy(state.regs, 0, stateOrig.regs, 0, state.regs.length);

            int inst = sim4.getInstruction(state.pc, state.instMemory);

            InstructionFields fields = new InstructionFields();
            InstructionFields fieldsSave = new InstructionFields();

            CPUControl control = new CPUControl();
            CPUControl controlSave = new CPUControl();

            ALUResult aluResult = new ALUResult();
            ALUResult aluResultSave = new ALUResult();

            if (inst != 0) {
                sim4.extractInstructionFields(inst, fields);

                /* save a second copy of the fields, to detect later modifications */
                fieldsSave.opcode = fields.opcode;
                fieldsSave.rs = fields.rs;
                fieldsSave.rt = fields.rt;
                fieldsSave.rd = fields.rd;
                fieldsSave.shamt = fields.shamt;
                fieldsSave.funct = fields.funct;
                fieldsSave.imm16 = fields.imm16;
                fieldsSave.imm32 = fields.imm32;
                fieldsSave.address = fields.address;

                int rsVal = state.regs[fields.rs];
                int rtVal = state.regs[fields.rt];
                int reg32 = state.regs[32];
                int reg33 = state.regs[33];

                int ok = sim4.fillCPUControl(fields, control);

                if (ok == 0) {
                    System.out.printf("execute_CPUControl() returned 0 with opcode=0x%02x funct=0x%02x at PC 0x%08x\n",
                            fields.opcode, fields.funct, state.pc);
                    continue;
                }

                /* save a second copy of the control, to detect later modifications */
                controlSave.ALUsrc = control.ALUsrc;
                controlSave.ALU.op = control.ALU.op;
                controlSave.ALU.bNegate = control.ALU.bNegate;
                controlSave.memRead = control.memRead;
                controlSave.memWrite = control.memWrite;
                controlSave.memToReg = control.memToReg;
                controlSave.regDst = control.regDst;
                controlSave.regWrite = control.regWrite;
                controlSave.branch = control.branch;
                controlSave.jump = control.jump;
                controlSave.extra1 = control.extra1;
                controlSave.extra2 = control.extra2;
                controlSave.extra3 = control.extra3;

                /* dump out the information to the user */
                System.out.printf("PC: 0x%04x_%04x   Instruction: 0x%04x_%04x\n",
                        (state.pc >> 16) & 0xFFFF, state.pc & 0xFFFF,
                        (inst >> 16) & 0xFFFF, inst & 0xFFFF);
                sim4_test_commonCode.dumpFields(fields);
                sim4_test_commonCode.dumpControl(rsVal, rtVal, control);
                System.out.println();

                int alu1 = sim4.getALUinput1(control, fields, rsVal, rtVal, reg32, reg33, state.pc);
                int alu2 = sim4.getALUinput2(control, fields, rsVal, rtVal, reg32, reg33, state.pc);

                sim4.executeALU(control, alu1, alu2, aluResult);

                /* save a second copy of the ALU Result, to detect later modifications */
                aluResultSave.result = aluResult.result;
                aluResultSave.zero = aluResult.zero;
                aluResultSave.extra = aluResult.extra;

                MemResult memResult = new MemResult();

                sim4.executeMEM(control, aluResult, rsVal, rtVal, state.dataMemory, memResult);

                System.out.printf("  memResult.readVal=0x%04x_%04x\n",
                        (memResult.readVal >> 16) & 0xffff,
                        memResult.readVal & 0xffff);

                for (int i = 0; i < state.dataMemory.length; i++) {
                    if (state.dataMemory[i] != stateOrig.dataMemory[i]) {
                        int addr = i * 4;
                        int old = stateOrig.dataMemory[i];
                        int newVal = state.dataMemory[i];

                        System.out.printf("  MEM CHANGED.  Addr: 0x%04x_%04x  Was: 0x%04x_%04x Now: 0x%04x_%04x\n",
                                (addr >> 16) & 0xffff, addr & 0xffff,
                                (old >> 16) & 0xffff, old & 0xffff,
                                (newVal >> 16) & 0xffff, newVal & 0xffff);
                    }
                }

                System.out.println();
            }

            /* was the state modified? */
            boolean fieldsModified = fields.opcode != fieldsSave.opcode ||
                    fields.rs != fieldsSave.rs ||
                    fields.rt != fieldsSave.rt ||
                    fields.rd != fieldsSave.rd ||
                    fields.shamt != fieldsSave.shamt ||
                    fields.funct != fieldsSave.funct ||
                    fields.imm16 != fieldsSave.imm16 ||
                    fields.imm32 != fieldsSave.imm32 ||
                    fields.address != fieldsSave.address;

            boolean controlModified = control.ALUsrc != controlSave.ALUsrc ||
                    control.ALU.op != controlSave.ALU.op ||
                    control.ALU.bNegate != controlSave.ALU.bNegate ||
                    control.memRead != controlSave.memRead ||
                    control.memWrite != controlSave.memWrite ||
                    control.memToReg != controlSave.memToReg ||
                    control.regDst != controlSave.regDst ||
                    control.regWrite != controlSave.regWrite ||
                    control.branch != controlSave.branch ||
                    control.jump != controlSave.jump;

            boolean aluResultModified = aluResult.result != aluResultSave.result ||
                    aluResult.zero != aluResultSave.zero ||
                    aluResult.extra != aluResultSave.extra;

            if (fieldsModified || controlModified || aluResultModified) {
                System.out.printf("ERROR: At PC=0x%08x, the 'fields' and/or 'control' and/or 'aluResult' structs were modified.\n", stateOrig.pc);
            }
        }
    }
}