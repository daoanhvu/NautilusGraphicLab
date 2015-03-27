package nautilus.lab;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class CoordinatorTest extends TestCase {
	
	public CoordinatorTest(String testName){
		super(testName);
	}

	public static Test suite(){
		return new TestSuite(CoordinatorTest.class);
	}
}
