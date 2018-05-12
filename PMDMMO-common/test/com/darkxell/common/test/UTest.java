package com.darkxell.common.test;

import com.darkxell.common.util.Logger;

/**
 * Abstract class that represents a unitary test. For each unitary test you want
 * to create, extend this class and implement a new test method.
 */
public abstract class UTest {

	/** The action taken if this test happens to fail. */
	public final byte ACTIONONFAIL;
	/**
	 * Action taken if this test fails.<br/>
	 * This is the default UTest behavior. It will terminate the process with an
	 * error message and the error code -1 if the test fails.
	 */
	public static final byte ACTION_BREAK = 0;
	/**
	 * Action taken if this test fails.<br/>
	 * This action will only output an error message on test failure but will
	 * not terminate the process.
	 */
	public static final byte ACTION_WARN = 1;
	/**
	 * Action taken if this test fails.<br/>
	 * This action will only notify the user that the test went wrong but will
	 * not output an error message. This value should only be used for legacy
	 * tests.
	 */
	public static final byte ACTION_NOTIFY = 2;
	/**
	 * This action will skip the test if the execute method is called..
	 */
	public static final byte ACTION_PASS = 3;
	
	/**
	 * Code to return for a successful test.
	 */
	public static final int TEST_SUCCESSFUL = 0;

	/** Creates a new unitary test. By default, this test */
	public UTest() {
		ACTIONONFAIL = ACTION_BREAK;
	}

	/** Creates a new unitary test with a custom on fail action. */
	public UTest(byte onfail) {
		if (onfail >= 0 && onfail <= 3)
			this.ACTIONONFAIL = onfail;
		else
			this.ACTIONONFAIL = ACTION_BREAK;
	}

	/** Executes this unitary test. */
	public void execute() {
		if (this.ACTIONONFAIL == ACTION_PASS)
			Logger.d("Test was skipped");
		else
		{
			int errorcode = this.test();
			if (errorcode == 0) Logger.d("Test went just fine");
			else
			{
				switch (ACTIONONFAIL)
				{
					case ACTION_BREAK:
						System.exit(-1);
					case ACTION_WARN:
						Logger.e("Test went wrong: Error code " + errorcode);
						break;
				}
			}
		}
	}

	/**
	 * Tests a feature. This method should be overridden for each unitary test
	 * created.<br/>
	 * <br/>
	 * <b>Note:</b> To log messages to the console from this method, the log()
	 * method should be used to ensure correct warning level.
	 * 
	 * @return 0 if the test want fine, an error code otherwise.
	 */
	protected abstract int test();

	/**
	 * Used by a test to log messages using the appropriate warning level to the
	 * console.
	 */
	protected void log(String message) {
		if (ACTIONONFAIL == ACTION_NOTIFY)
			Logger.i(message);
		else
			Logger.e(message);
	}

}
