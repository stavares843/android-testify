/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Shopify Inc.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.shopify.testify.actions.utility

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.search.FilenameIndex
import com.shopify.testify.baselineImageName
import org.jetbrains.kotlin.idea.util.projectStructure.module
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.uast.UElement

abstract class BaseFileAction(protected val anchorElement: UElement) : AnAction() {

    val baselineImageName = anchorElement.baselineImageName
    abstract val menuText: String

    final override fun update(anActionEvent: AnActionEvent) {

        val isInProject = anActionEvent.isBaselineInProject

        anActionEvent.presentation.apply {
            text = menuText
            isEnabled = isInProject
            isVisible = (anActionEvent.project != null)
        }
    }

    final override fun actionPerformed(event: AnActionEvent) {
        event.findBaselineImage()?.let {
            performActionOnVirtualFile(it, event.project!!, event.modifiers)
        }
    }

    private fun AnActionEvent.findBaselineImage(): VirtualFile? {
        val psiFile = anchorElement.sourcePsi?.containingFile
        if (psiFile is KtFile && psiFile.module != null) {
            val files = FilenameIndex.getVirtualFilesByName(project, baselineImageName, psiFile.module!!.moduleContentScope)
            if (files.isNotEmpty()) {
                return files.first()
            }
        }
        return null
    }

    private val AnActionEvent.isBaselineInProject: Boolean
        get() {
            return findBaselineImage() != null
        }

    abstract fun performActionOnVirtualFile(virtualFile: VirtualFile, project: Project, modifiers: Int)
}
