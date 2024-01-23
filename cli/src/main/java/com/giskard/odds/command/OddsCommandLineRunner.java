package com.giskard.odds.command;

import picocli.CommandLine;
import picocli.CommandLine.IFactory;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.stereotype.Component;

@Component
public class OddsCommandLineRunner implements CommandLineRunner, ExitCodeGenerator {

	private final OddsCommand oddsCommand;

	private final IFactory factory;

	private int exitCode;

	public OddsCommandLineRunner(OddsCommand oddsCommand, IFactory factory) {
		this.oddsCommand = oddsCommand;
		this.factory = factory;
	}

	@Override
	public void run(String... args) throws Exception {
		exitCode = new CommandLine(oddsCommand, factory).execute(args);
	}

	@Override
	public int getExitCode() {
		return exitCode;
	}
}