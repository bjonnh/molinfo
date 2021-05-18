package net.nprod.molinfo.chemistry

import org.openscience.cdk.depict.DepictionGenerator
import org.openscience.cdk.inchi.InChIGeneratorFactory
import org.openscience.cdk.interfaces.IAtomContainer
import org.openscience.cdk.silent.SilentChemObjectBuilder
import org.openscience.cdk.smiles.SmilesParser

class MoleculeManager {
    val depictionGenerator = DepictionGenerator().withFillToFit().withAtomColors()
    val inchiGenerator = InChIGeneratorFactory.getInstance()

    /**
     * Generate a Molecule from a SMILES string
     *
     * @param smiles: The SMILES string
     */
    fun moleculeFromSmiles(smiles: String): Molecule =
        Molecule(this).apply {
            atomContainer = SmilesParser(SilentChemObjectBuilder.getInstance()).parseSmiles(smiles)
        }

    /**
     * Generate an InchiKey from a SMILES string
     */
    fun inchiKey(atomContainer: IAtomContainer): String =
        inchiGenerator.getInChIGenerator(atomContainer).inchiKey
}
