package measure;

import static org.junit.jupiter.api.Assertions.*;

import java.util.LinkedList;

import org.junit.jupiter.api.Test;

import measure.util.CompPrimaryKey;

class MessreiheTest {

	@Test
	void test() {
		int[] messungenInt = {8,7,9,10,6};
		LinkedList<Long> messungen = new LinkedList<Long>();
		for (int i : messungenInt) {
			messungen.add((long) i);
		}
		Messreihe underTest = new Messreihe(messungen);
				
		assertEquals(8, underTest.getDurchschnitt());
		assertEquals(2, underTest.getVarianz());
		assertEquals(1.4142, underTest.getStandardAbweichung());
	}

	@Test
	void testCompPrimaryKey_Equals() throws Exception {
		CompPrimaryKey ps1 = new CompPrimaryKey("a","k");
		CompPrimaryKey ps2 = new CompPrimaryKey("a","k");
		assertEquals(ps1, ps2);
	}
	
	@Test
	void testCompPrimaryKey_NotEquals() throws Exception {
		CompPrimaryKey ps1 = new CompPrimaryKey("a","k");
		CompPrimaryKey ps2 = new CompPrimaryKey("a","k2");
		assertNotEquals(ps1, ps2);
	}
}
