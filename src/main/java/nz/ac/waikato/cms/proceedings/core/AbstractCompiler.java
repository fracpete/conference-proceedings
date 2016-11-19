/*
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

/**
 * AbstractCompiler.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package nz.ac.waikato.cms.proceedings.core;

import nz.ac.waikato.cms.core.FileUtils;

import java.io.File;

/**
 * Specification for a compiler.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public abstract class AbstractCompiler {

  /** the name of the compiler. */
  protected String m_Name;
  
  /** the executable. */
  protected String m_Executable;

  /** the additional options. */
  protected String[] m_Options;

  /**
   * Sets the name.
   *
   * @param value	the name
   */
  public void setName(String value) {
    m_Name = value;
  }

  /**
   * Returns the name.
   *
   * @return		the name
   */
  public String getName() {
    return m_Name;
  }

  /**
   * Sets the executable.
   *
   * @param value	the executable
   */
  public void setExecutable(String value) {
    m_Executable = value;
    if ((m_Executable != null) && (m_Name == null))
      m_Name = FileUtils.replaceExtension(new File(m_Executable).getName(), "");
  }

  /**
   * Returns the executable.
   *
   * @return		the executable
   */
  public String getExecutable() {
    return m_Executable;
  }

  /**
   * Sets the additional options for the compiler.
   *
   * @param value	the options
   */
  public void setOptions(String[] value) {
    m_Options = value;
  }

  /**
   * Returns the additional options for the compiler.
   *
   * @return		the options
   */
  public String[] getOptions() {
    return m_Options;
  }

  /**
   * Performs checks before compiling the document.
   *
   * @param document	the document to compile
   * @return		null if successful, otherwise error message
   */
  protected String check(String document) {
    File	file;

    if (m_Executable == null)
      return "No executable defined!";
    file = new File(m_Executable);
    if (!file.exists())
      return "Executable does not exist: " + file;
    if (file.isDirectory())
      return "Executable points to a directory: " + file;
    if (!file.canExecute())
      return "Executable cannot be executed: " + file;

    file = new File(document);
    if (!file.exists())
      return "Document does not exist: " + file;
    if (file.isDirectory())
      return "Document points to a directory: " + file;

    return null;
  }

  /**
   * Compiles the specified document.
   *
   * @param document	the document to compile
   * @return		null if successful, otherwise error message
   */
  protected abstract String doCompile(String document);

  /**
   * Compiles the specified document.
   *
   * @param document	the document to compile
   * @return		null if successful, otherwise error message
   */
  public String compile(String document) {
    String	result;

    result = check(document);
    if (result == null)
      result = doCompile(document);

    return result;
  }
}
