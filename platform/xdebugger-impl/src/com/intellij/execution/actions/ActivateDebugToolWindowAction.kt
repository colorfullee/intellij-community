// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.intellij.execution.actions

import com.intellij.execution.executors.DefaultDebugExecutor
import com.intellij.execution.impl.EditConfigurationsDialog
import com.intellij.execution.ui.RunContentManager
import com.intellij.execution.ui.RunContentManagerImpl
import com.intellij.icons.AllIcons
import com.intellij.ide.actions.ToolWindowEmptyStateAction
import com.intellij.lang.LangBundle
import com.intellij.openapi.help.HelpManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindowId
import com.intellij.ui.SimpleTextAttributes
import com.intellij.util.ui.StatusText
import com.intellij.xdebugger.XDebuggerBundle

class ActivateDebugToolWindowAction : ToolWindowEmptyStateAction(ToolWindowId.DEBUG, AllIcons.Toolwindows.ToolWindowDebugger) {
  override fun ensureToolWindowCreated(project: Project) {
    val runContentManager = RunContentManager.getInstance(project) as RunContentManagerImpl
    runContentManager.registerToolWindow(DefaultDebugExecutor.getDebugExecutorInstance())
  }

  override fun setupEmptyText(project: Project, text: StatusText) {
    text.appendLine(XDebuggerBundle.message("debug.toolwindow.empty.text.1"))
    text.appendLine(XDebuggerBundle.message("debug.toolwindow.empty.text.2"))
    text.appendLine(LangBundle.message("run.toolwindow.empty.text.3") + " ")
    text.appendText(LangBundle.message("run.toolwindow.empty.text.4"), SimpleTextAttributes.LINK_ATTRIBUTES) {
      EditConfigurationsDialog(project).show()
    }
    text.appendLine("")
    text.appendLine(AllIcons.General.ContextHelp, XDebuggerBundle.message("debug.toolwindow.empty.text.help"), SimpleTextAttributes.LINK_ATTRIBUTES) {
      HelpManager.getInstance().invokeHelp("procedures.debugging")
    }
  }
}