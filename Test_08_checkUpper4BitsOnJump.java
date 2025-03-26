public class Test_08_checkUpper4BitsOnJump {
    public static void main(String[] args) {
        Sim4 sim4 = new Sim4();

        final int COUNT = 10;

        int[] oldPC = new int[COUNT];
        int[] instructions = new int[COUNT];
        int[] aluZero = new int[COUNT];

        // Arrays to store fields and controls for each test
        InstructionFields[] fields = new InstructionFields[COUNT*2];
        CPUControl[] controls = new CPUControl[COUNT*2];

        // Initialize the arrays
        for (int i = 0; i < COUNT*2; i++) {
            fields[i] = new InstructionFields();
            controls[i] = new CPUControl();
        }

        /* the PC and jump address are both chosen here.  We also
         * choose what value of aluZero we'll deliver.
         */
        oldPC[0] = 0x20ec303a;
        oldPC[1] = 0x575dcd05;
        oldPC[2] = 0x749d7dd9;
        oldPC[3] = 0x4d59ef62;
        oldPC[4] = 0x034a71d6;
        oldPC[5] = 0x3a8f3eb8;
        oldPC[6] = 0x78782108;
        oldPC[7] = 0x2746e8b1;
        oldPC[8] = 0x44737f8b;
        oldPC[9] = 0x3963ccd4;

        instructions[0] = 0x08eeb9f1;
        instructions[1] = 0x09e74bcc;
        instructions[2] = 0x0801d315;
        instructions[3] = 0x0a2cd0c6;
        instructions[4] = 0x08483b7c;
        instructions[5] = 0x09a3f638;
        instructions[6] = 0x08f94c0c;
        instructions[7] = 0x0965f1c5;
        instructions[8] = 0x09b41405;
        instructions[9] = 0x0ae9dc7a;

        // All zeros for aluZero (this is fine since jump doesn't use it)
        for (int i = 0; i < COUNT; i++) {
            aluZero[i] = 0;
        }

        /* fill in each of the control structs.  We make a duplicate of each
         * one, which we'll use later to see if
         */
        for (int i = 0; i < COUNT; i++) {
            sim4.extractInstructionFields(instructions[i], fields[i]);

            // Copy fields to the duplicate array
            fields[i+COUNT].opcode = fields[i].opcode;
            fields[i+COUNT].rs = fields[i].rs;
            fields[i+COUNT].rt = fields[i].rt;
            fields[i+COUNT].rd = fields[i].rd;
            fields[i+COUNT].shamt = fields[i].shamt;
            fields[i+COUNT].funct = fields[i].funct;
            fields[i+COUNT].imm16 = fields[i].imm16;
            fields[i+COUNT].imm32 = fields[i].imm32;
            fields[i+COUNT].address = fields[i].address;

            sim4.fillCPUControl(fields[i], controls[i]);

            // Copy control values to the duplicate array
            controls[i+COUNT].ALUsrc = controls[i].ALUsrc;
            controls[i+COUNT].ALU.op = controls[i].ALU.op;
            controls[i+COUNT].ALU.bNegate = controls[i].ALU.bNegate;
            controls[i+COUNT].memRead = controls[i].memRead;
            controls[i+COUNT].memWrite = controls[i].memWrite;
            controls[i+COUNT].memToReg = controls[i].memToReg;
            controls[i+COUNT].regDst = controls[i].regDst;
            controls[i+COUNT].regWrite = controls[i].regWrite;
            controls[i+COUNT].branch = controls[i].branch;
            controls[i+COUNT].jump = controls[i].jump;
            controls[i+COUNT].extra1 = controls[i].extra1;
            controls[i+COUNT].extra2 = controls[i].extra2;
            controls[i+COUNT].extra3 = controls[i].extra3;

            System.out.printf("i=%2d:\n", i);
            System.out.printf("  instruction = 0x%04x_%04x\n",
                    (instructions[i] >> 16) & 0xffff,
                    instructions[i] & 0xffff);

            System.out.println("  control:");
            System.out.printf("    opcode  = 0x%02x\n", fields[i].opcode);
            System.out.printf("    address = 0x %03x_%04x\n",
                    (fields[i].address >> 16) & 0xffff,
                    fields[i].address & 0xffff);
            System.out.printf("    jump    = %d\n", controls[i].jump);
            System.out.printf("    branch  = %d\n", controls[i].branch);
            System.out.println();
        }

        // Check each of the outputs
        for (int i = 0; i < COUNT; i++) {
            int newPC = sim4.getNextPC(fields[i], controls[i], aluZero[i],
                    0x00000000, 0x00000000,
                    oldPC[i]);

            System.out.printf("i=%2d: oldPC=0x%04x_%04x (addr<<2)=0x%04x_%04x -> newPC=0x%04x_0x%04x\n",
                    i,
                    (oldPC[i] >> 16) & 0xffff,
                    oldPC[i] & 0xffff,
                    ((fields[i].address<<2) >> 16) & 0xffff,
                    (fields[i].address<<2) & 0xffff,
                    (newPC >> 16) & 0xffff,
                    newPC & 0xffff);
        }

        // Check if any control structs were modified
        boolean controlsModified = false;
        for (int i = 0; i < COUNT; i++) {
            if (controls[i].ALUsrc != controls[i+COUNT].ALUsrc ||
                    controls[i].ALU.op != controls[i+COUNT].ALU.op ||
                    controls[i].ALU.bNegate != controls[i+COUNT].ALU.bNegate ||
                    controls[i].memRead != controls[i+COUNT].memRead ||
                    controls[i].memWrite != controls[i+COUNT].memWrite ||
                    controls[i].memToReg != controls[i+COUNT].memToReg ||
                    controls[i].regDst != controls[i+COUNT].regDst ||
                    controls[i].regWrite != controls[i+COUNT].regWrite ||
                    controls[i].branch != controls[i+COUNT].branch ||
                    controls[i].jump != controls[i+COUNT].jump) {

                controlsModified = true;
                break;
            }
        }

        if (controlsModified) {
            System.out.println("ERROR: One or more control structs was modified *OUTSIDE* of execute_CPUControl()!");
        }
    }
}