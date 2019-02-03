package db.jpa.client.view;

class CmdLine {

    private static final String PARAM_DELIMETER = " ";
    private Command cmd;
    private final String enteredLine;

    CmdLine(String enteredLine) {
        this.enteredLine = enteredLine;
        parseCmd(enteredLine);
    }

    Command getCmd() {
        return cmd;
    }

    String getUserInput() {
        return enteredLine;
    }

    private void parseCmd(String enteredLine) {
        int cmdNameIndex = 0;
        try {
            String[] enteredTokens = (enteredLine).split(PARAM_DELIMETER);
            cmd = Command.valueOf(enteredTokens[cmdNameIndex].toUpperCase());
        } catch (Throwable failedToReadCmd) {
            cmd = Command.ILLEGAL_COMMAND;
        }
    }
}
