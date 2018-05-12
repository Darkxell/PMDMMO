package com.darkxell.common.test;

import com.darkxell.common.test.tests.DBObjecttransferTest;
import com.darkxell.common.util.Logger;

public class CommonUnitTests
{

	public static void main(String[] args)
	{
		Logger.loadServer();
		new DBObjecttransferTest().execute();
	}

}
