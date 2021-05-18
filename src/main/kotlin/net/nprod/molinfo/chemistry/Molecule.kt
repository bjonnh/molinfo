/*
 *
 * SPDX-License-Identifier: MIT License
 *
 * Copyright (c) 2021 Jonathan Bisson
 *
 */

package net.nprod.molinfo.chemistry

import org.openscience.cdk.interfaces.IAtomContainer
import java.awt.image.BufferedImage

/**
 * A Molecule container that wraps CDK
 */
class Molecule(private val manager: MoleculeManager) {
    internal var atomContainer: IAtomContainer? = null

    /**
     * Get an svg depiction of the molecule
     *
     * @return SVG as a string or null if atomContainer is not defined
     */
    fun svg(): String? = atomContainer?.let { manager.depictionGenerator.depict(it).toSvgStr() }

    /**
     * Get a png depiction of the molecule
     *
     * @param width Width of image
     * @param height Height of image
     * @return PNG as a BufferedImage
     */
    fun png(width: Double = 300.0, height: Double = 300.0): BufferedImage? =
        atomContainer?.let {
            manager.depictionGenerator.withSize(width, height).depict(it).toImg()
        }

    fun inchikey(): String? = atomContainer?.let { manager.inchiKey(it) }
}
