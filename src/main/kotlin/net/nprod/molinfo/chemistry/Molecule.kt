/*
 *
 * SPDX-License-Identifier: MIT License
 *
 * Copyright (c) 2021 Jonathan Bisson
 *
 */

package net.nprod.molinfo.chemistry

import org.openscience.cdk.depict.DepictionGenerator
import org.openscience.cdk.interfaces.IAtomContainer
import org.openscience.cdk.silent.SilentChemObjectBuilder
import org.openscience.cdk.smiles.SmilesParser
import java.awt.image.BufferedImage

/**
 * A Molecule container that wraps CDK
 */
class Molecule {
    internal var atomContainer: IAtomContainer? = null

    private fun depictionGenerator() = DepictionGenerator().withFillToFit().withAtomColors()

    /**
     * Get an svg depiction of the molecule
     *
     * @return SVG as a string
     */
    fun svg(): String = depictionGenerator().depict(atomContainer).toSvgStr()

    /**
     * Get a png depiction of the molecule
     *
     * @param width Width of image
     * @param height Height of image
     * @return PNG as a BufferedImage
     */
    fun png(width: Double = 300.0, height: Double = 300.0): BufferedImage =
        depictionGenerator().withSize(width, height).depict(atomContainer).toImg()

    companion object {
        /**
         * Generate a Molecule from a SMILES string
         *
         * @param smiles: The SMILES string
         */
        fun fromSmiles(smiles: String): Molecule =
            Molecule().apply {
                atomContainer = SmilesParser(SilentChemObjectBuilder.getInstance()).parseSmiles(smiles)
            }
    }
}
