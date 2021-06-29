package net.nprod.skotliton

import net.nprod.molinfo.chemistry.MoleculeManager
import org.junit.jupiter.api.Test

internal class mainTest {
    @Test
    fun `test Main ok`() {
        assert(true)
    }

    @Test
    fun `check canonical smiles support`() {
        val mm = MoleculeManager()
        val mol = mm.moleculeFromSmiles("OCC1C2C(=CN3CCC4(C=5C=CC=CC5NC14)C3C2)C(C6=CC=C7NC8C(CO)C9CC%10N(CCC8%10C7=C6)CC9C=C)C")
        println(mol.svg())
    }
}
