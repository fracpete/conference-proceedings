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
 * Document.java
 * Copyright (C) 2016 University of Waikato, Hamilton, NZ
 */

package nz.ac.waikato.cms.proceedings.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Represents a document.
 *
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision$
 */
public class Document
  implements YamlObject<Map<String,Object>> {

  /** the name of the compiler. */
  protected String m_Compiler;

  /** the file. */
  protected String m_File;

  /**
   * Sets the name of the compiler to use.
   *
   * @param value	the name
   */
  public void setCompiler(String value) {
    m_Compiler = value;
  }

  /**
   * Returns the compiler.
   *
   * @return		the name
   */
  public String getCompiler() {
    return m_Compiler;
  }

  /**
   * Sets the document file.
   *
   * @param value	the file
   */
  public void setFile(String value) {
    m_File = value;
  }

  /**
   * Returns the document file.
   *
   * @return		the file
   */
  public String getFile() {
    return m_File;
  }

  /**
   * Returns the document in yaml notation.
   *
   * @return		the representation
   */
  @Override
  public Map<String, Object> toYaml() {
    Map<String,Object> 	result;

    result = new HashMap<>();
    result.put("compiler", getCompiler());
    result.put("file", getFile());

    return result;
  }

  /**
   * Turns the map into a document.
   *
   * @param value	the map to use
   * @return		the document, null if failed
   */
  public static Document fromYaml(Map<String,Object> value) {
    Document 	result;

    if (!value.containsKey("compiler"))
      return null;
    if (!value.containsKey("file"))
      return null;

    result = new Document();
    result.setCompiler((String) value.get("compiler"));
    result.setFile((String) value.get("file"));

    return result;
  }

  /**
   * Returns a short string representation.
   *
   * @return		the representation
   */
  public String toString() {
    return m_Compiler + ": " + m_File;
  }
}
