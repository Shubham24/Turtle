package com.github.shubham24;

import picocli.CommandLine;
import picocli.CommandLine.Command;

/**
 * Main class. Contains main. Executes args.
 */
@Command(name = "turtle", subcommands = { Generate.class, CLIGenClassCommand.class, CLIGenRecordCommand.class,
        CLIGenInterfaceCommand.class })
class Turtle implements Runnable {
    @Override
    public void run() {
        System.out.println("Turtle!");
    }

    public static void main(String... args) {
        new CommandLine(new Turtle()).execute(args);
    }
}
