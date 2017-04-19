package com.example;

import java.text.Normalizer;
import java.text.Normalizer.Form;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Cycle {
	private final int balance;
	private static final Cycle c = new Cycle();
	// deposit
	private static final int deposit = (int) (Math.random() * 100); // Random

	public Cycle() {
		balance = deposit - 10; // Subtract processing fee
	}

	public static void main(String[] args) {
		System.out.println("The account balance is: " + c.balance);

//		normalizeBeforeUsingString();
		
		// args[0] should contain the credit card expiration date
	    // but might contain %1$tm, %1$te or %1$tY format specifiers
	    System.out.format(
	      "%1$te" + " did not match! HINT: It was issued on %1$terd of some month", c
	    );

	}

	private static void normalizeBeforeUsingString() {
		// String s may be user controllable
		// \uFE64 is normalized to < and \uFE65 is normalized to > using the
		// NFKC normalization form
		String s = "\uFE64" + "script" + "\uFE65";

		// Normalize
		s = Normalizer.normalize(s, Form.NFKC);
		// Validate
		Pattern pattern = Pattern.compile("[<>]"); // Check for angle brackets
		Matcher matcher = pattern.matcher(s);
		if (matcher.find()) {
			// Found black listed tag
			throw new IllegalStateException();
		} else {
			System.out.println("Passed");
		}
	}
}