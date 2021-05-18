package net.nprod.molinfo.chemistry

import org.openscience.cdk.depict.DepictionGenerator
import org.openscience.cdk.inchi.InChIGeneratorFactory
import org.openscience.cdk.interfaces.IAtomContainer
import org.openscience.cdk.silent.SilentChemObjectBuilder
import org.openscience.cdk.smiles.SmilesParser
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator

class MoleculeManager {
    fun depictionGenerator() = DepictionGenerator().withFillToFit().withAtomColors()
    val inchiGenerator = InChIGeneratorFactory.getInstance()

    /**
     * Generate a Molecule from a SMILES string
     *
     * @param smiles: The SMILES string
     */
    fun moleculeFromSmiles(smiles: String): Molecule =
        Molecule(this, SmilesParser(SilentChemObjectBuilder.getInstance()).parseSmiles(smiles))

    /**
     * Generate an InchiKey from a SMILES string
     */
    fun inchiKey(atomContainer: IAtomContainer): String =
        inchiGenerator.getInChIGenerator(atomContainer).inchiKey

    /**
     * Get the exact mass of the given molecule
     */
    fun exactmass(it: IAtomContainer): Double =
        AtomContainerManipulator.getMass(it, AtomContainerManipulator.MonoIsotopic)

    /**
     * Get the average mass of the given molecule
     */
    fun averagemass(it: IAtomContainer): Double =
        AtomContainerManipulator.getMass(it, AtomContainerManipulator.MolWeight)
}
