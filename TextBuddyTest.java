package textBuddy;
import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class TextBuddyTest {
	@Test
	public void testMain() {
	}
	@Test
	public void test(){
		String[] args = {"myTextFile.txt",};
		String[] input = {"","test"};
		String[] input2 = {"","snake-cat"};
		String[] input3 = {"","catdog"};
		
		// Unit test for sort method
		assertEquals("testSort","List cannot be sorted as it is empty\n",TextBuddy.sort(args[0]));
		TextBuddy.add(args[0], input);
		TextBuddy.add(args[0], input2);
		TextBuddy.add(args[0], input3);
		assertEquals("testSort","List is now sorted\n",TextBuddy.sort(args[0]));
		
		// Unit test for search method
		String[] input4 = {"","cat"};
		ArrayList<String> result = new ArrayList<String>();
		result.add("1. catdog\n");
		result.add("2. snake-cat\n");
		assertEquals("testSearch",result, TextBuddy.search(args[0], input4));
		
		TextBuddy.clear(args[0]);
		result.clear();
		result.add("List cannot be searched as it is empty\n");
		assertEquals("testSearch",result, TextBuddy.search(args[0], input4));
	}
}
