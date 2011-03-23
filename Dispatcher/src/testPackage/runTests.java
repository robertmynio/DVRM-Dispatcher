package testPackage;

public class runTests {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Tests tests = new Tests();
		boolean result;
		
		result = tests.hardcodedInitTest();
		System.out.println("Result for hardcoded init test is : " + result);
		
		//this test should be called after an initialization (for example, after hardcoded init test)
		result = tests.addOneTask();
		System.out.println("Result for add one task test is : " + result);
	}

}
